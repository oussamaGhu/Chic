import { Module } from '@nestjs/common';
import { AuthService } from './auth.service';
import { AuthController } from './auth.controller';

import { PassportModule } from '@nestjs/passport';
import { MongooseModule } from '@nestjs/mongoose';

import * as dotenv from 'dotenv';
import { JwtModule } from '@nestjs/jwt';

import { User, UserSchema } from 'src/user/schema/usersSchema';
import { UserModule } from 'src/user/user.module';
import { RefreshToken, RefreshTokenSchema } from 'src/user/schema/refreshTokenSchema';
import { MailService } from './services/mailService';
import { ResetToken, ResetTokenSchema } from 'src/user/schema/resetTokn';
import { FilesModule } from 'src/files/files.module';
//dotenv.config();
@Module({
  
    imports: [UserModule,
      MongooseModule.forFeature([{ name :User.name,schema:UserSchema },
        {
          name: RefreshToken.name,    // ajouter le refreshtoken schema
          schema: RefreshTokenSchema,
      
        },
        {
          name: ResetToken.name,
          schema: ResetTokenSchema,
        },
       
      ]),
      FilesModule, // Enregistrer le schéma 
    ],
 
  providers: [AuthService,MailService],
  controllers: [AuthController],
  exports : [AuthService]
})
export class AuthModule {}
