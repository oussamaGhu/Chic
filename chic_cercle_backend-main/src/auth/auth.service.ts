import { BadRequestException, Injectable, InternalServerErrorException, NotFoundException, UnauthorizedException } from '@nestjs/common';

import { InjectModel } from '@nestjs/mongoose';
import { LoginDto } from './dto/login.dto';
import* as bcrypt from 'bcryptjs' ;  
import { v4 as uuidv4 } from 'uuid';
import { ConfigService } from '@nestjs/config';
import { signUpDto } from './dto/signUp.dto';
import { JwtService } from '@nestjs/jwt';
import { RefreshToken } from 'src/user/schema/refreshTokenSchema';
import { MailService } from './services/mailService';
import { ResetToken } from 'src/user/schema/resetTokn';
import { User } from 'src/user/schema/usersSchema';
import { Model } from 'mongoose';
import { Files } from 'src/user/schema/files.schema';
import { FilesService } from 'src/files/files.service';

@Injectable()
export class AuthService {
    constructor(
        @InjectModel(User.name) private UserModel: Model<User>,
        @InjectModel(Files.name) private  fileModel: Model<Files>,
        @InjectModel(RefreshToken.name) private RefreshTokenModel : Model<RefreshToken>,
        @InjectModel(ResetToken.name)
        private ResetTokenModel: Model<ResetToken>,
        private jwtService: JwtService,
        private configService: ConfigService,
        private fileService: FilesService,
        private readonly mailService: MailService
      ) {}
      async signup(signupData: signUpDto)  {
        // Check if email is already in use
        const emailInUse = await this.UserModel.findOne({ email: signupData.email });
        if (emailInUse) {
          throw new BadRequestException('Email is already in use');
        }
        // Hash the password
        const hashedPassword = await bcrypt.hash(signupData.password, 10);
    
        // Create user document and save it in MongoDB
        const user = await this.UserModel.create({
          ...signupData,
          password: hashedPassword,
        });
    
        const payload = {
          userId: user._id.toString(),
          email: user.email,
          role: user.role,
        };
    
        // Generate tokens
        const tokens = await this.generatedUserToken(payload);
    
        return {
          ...tokens,
          role: user.role,
          userId: user._id,
        };
      }


      async googleAccess(signupData: signUpDto)  {
        // Check if email is already in use
        const emailInUse = await this.UserModel.findOne({ email: signupData.email });
        const imageUrl = signupData.pictureProfile
        if (emailInUse) {
          const signUp = false
          const payload = {
            userId: emailInUse._id.toString(),
            email: emailInUse.email,
            role: emailInUse.role,
          };
      
          // Generate tokens
          const tokens = await this.generatedUserToken(payload);
        return {
          ...tokens,
          role: emailInUse.role,
          userId: emailInUse._id,
          signUp: false
        };
        }
        let fileIdFromFunction;

        try {
          // Call downloadImageAndUpload to get the fileId
          fileIdFromFunction = await this.fileService.downloadImageAndUpload(imageUrl);
        } catch (error) {
          console.error('Error uploading image:', error.message);
          throw new Error('Failed to process profile picture');
        }
        // Create user document and save it in MongoDB
        const user = await this.UserModel.create({
          ...signupData,
          pictureProfile: fileIdFromFunction
         
        });
    
        const payload = {
          userId: user._id.toString(),
          email: user.email,
          role: user.role,
        };
    
        // Generate tokens
        const tokens = await this.generatedUserToken(payload);
    
        return {
          ...tokens,
          role: user.role,
          userId: user._id,
          signUp: true
        };
      }




    async login ( Credentials : LoginDto) {
      const {email, password} = Credentials;
      // find if user exist by email
       const user = await this.UserModel.findOne({email})
       if(!user) {
        throw new  UnauthorizedException('wrong credentials');}
       //compare entered password with existing password
       const passwordMatch = await bcrypt.compare(password ,user.password ) 
       if(!passwordMatch) {
        throw new UnauthorizedException('wrong credentials');

    }

    if(user.role == "banned"){
      throw new UnauthorizedException('your account is banned');
    }
    const payload = {
      userId: user._id.toString(),
      email: user.email,
      role: user.role,  // Ajouter le rôle dans le payload
    };
  // if everything is correct generate accestoken and refreshtoken
  const tokens = await this.generatedUserToken(payload);

  // Retourne les tokens avec le rôle de l'utilisateur
  return {
    ...tokens,
    role: user.role,
    userId: user._id  // Ajoute le rôle dans la réponse
  };
}
async refreshTokens(refreshToken : string) {
  const token = await this.RefreshTokenModel.findOne({
  
    token: refreshToken,
    expiryDate: { $gte: new Date() },
  });

  if (!token) {
    throw new UnauthorizedException('Refresh Token is invalid');
  }
  const user = await this.UserModel.findById(token.userId);
  if (!user) {
    throw new UnauthorizedException('User not found');
  }
  return this.generatedUserToken({userId: user._id.toString(),   // ID de l'utilisateur
  email: user.email,      // Email de l'utilisateur
  role: user.role,        // Le rôle de l'utilisateur (client/seller)
});
}

async generatedUserToken(payload: { userId: string, email: string, role: string }) {
  // Créer un access token avec le payload
  const accessToken = this.jwtService.sign(payload, { expiresIn: '3d' });
  
  // Générer un refresh token
  const refreshToken = uuidv4(); 
  await this.storeRefreshToken(refreshToken, payload.userId); 
  
  return {
    accessToken,  
    refreshToken, 
  };
}
  // save the refreshToken in the data base
  async storeRefreshToken(token : string,userId) {
   const expiryDate = new Date(); //corrent date
expiryDate.setDate(expiryDate.getDate()+3); // the expiry date is after 3 day from the current date
await this.RefreshTokenModel.create({token,userId,expiryDate})
  }
 
  async changePassword(userId, oldPassword: string, newPassword: string) {
    //Find the user
    const user = await this.UserModel.findById(userId);
    if (!user) {
      throw new NotFoundException('User not found...');
    }

    //Compare the old password with the password in DB
    const passwordMatch = await bcrypt.compare(oldPassword, user.password);
    if (!passwordMatch) {
      throw new UnauthorizedException('Wrong credentials');
    }

    //Change user's password
    const newHashedPassword = await bcrypt.hash(newPassword, 10);
    user.password = newHashedPassword;
    await user.save();

    const payload = {
      userId: user._id.toString(),
      email: user.email,
      role: user.role,  // Ajouter le rôle dans le payload
    };
    const tokens = await this.generatedUserToken(payload);
    
      // Retourne les tokens avec le rôle de l'utilisateur
      return {
        ...tokens,
        role: user.role,
        userId: user._id  // Ajoute le rôle dans la réponse
      };
  }
  async forgotPassword(email: string) {
    //Check that user exists
    const user = await this.UserModel.findOne({ email });

    if (user) {
      //If user exists, generate password reset link
      const expiryDate = new Date();
      expiryDate.setHours(expiryDate.getHours() + 3);

      const resetToken = uuidv4();
      await this.ResetTokenModel.create({
        token: resetToken,
        userId: user._id,
        expiryDate,
      });
      //Send the link to the user by email
      await this.mailService.sendResetPasswordEmail(email, resetToken);
    }

    return { message: 'If this email exists, they will receive an email' };
  }
  async resetPassword(newPassword: string, resetToken: string) {
    //Find a valid reset token document
    const token = await this.ResetTokenModel.findOneAndDelete({
      token: resetToken,
      //expiryDate: { $gte: new Date() },
    });

    if (!token) {
      throw new UnauthorizedException('Invalid link');
    }

    //Change user password (MAKE SURE TO HASH!!)
    const user = await this.UserModel.findById(token.userId);
    if (!user) {
      throw new InternalServerErrorException();
    }

    user.password = await bcrypt.hash(newPassword, 10);
    await user.save();

    const payload = {
      userId: user._id.toString(),
      email: user.email,
      role: user.role,  // Ajouter le rôle dans le payload
    };
    const tokens = await this.generatedUserToken(payload);
    
      // Retourne les tokens avec le rôle de l'utilisateur
      return {
        ...tokens,
        role: user.role,
        userId: user._id  // Ajoute le rôle dans la réponse
      };
  }
}