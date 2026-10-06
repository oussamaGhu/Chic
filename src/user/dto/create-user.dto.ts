import { IsArray, IsDate, IsEmail,IsEnum,IsNumber,IsOptional,IsString ,Matches,MinLength,} from "class-validator";
import { Role } from "src/auth/Role.enums/Role.emun";
import { Types } from 'mongoose';

    export class CreateUserDto {
     
      @IsString()
      name: string;
      @IsString()
      password: string;
    
      @IsEmail()
      email: string;

      @IsOptional()
      @IsNumber()
      phoneNumber: number;

      @IsOptional()
      pictureProfile: Types.ObjectId;
   
      @IsEnum(Role)
      role: Role;
      
      @IsOptional()
      @IsDate()
      birthday: Date;

      @IsOptional()
      @IsArray()
      clothes: Types.ObjectId[];

      @IsOptional()
      @IsArray()
      assemble: Types.ObjectId[];

      @IsOptional()
      @IsString()
      height: number;

      @IsOptional()
      @IsString()
      weight: number;

      @IsOptional()
      @IsString()
     shape: string;

     @IsOptional()
      @IsString()
     location: string;

     @IsOptional()
     @IsArray()
     @IsString()
     request: string[];

     @IsOptional()
     @IsString()
     deviceId: string;

     constructor(
      name: string,
      password: string,
      email: string,
      role: Role,
      phoneNumber?: number,
      pictureProfile?: Types.ObjectId,
      birthday?: Date,
      clothes?: Types.ObjectId[],
      assemble?: Types.ObjectId[],
      height?: number,
      weight?: number,
      shape?: string,
      location?: string,
      request?: string[]
    ) {
      this.name = name;
      this.password = password;
      this.email = email;
      this.role = role;
      this.phoneNumber = phoneNumber;
      this.pictureProfile = pictureProfile;
      this.birthday = birthday;
      this.clothes = clothes;
      this.assemble = assemble;
      this.height = height;
      this.weight = weight;
      this.shape = shape;
      this.location = location;
      this.request = request;
    }
  }