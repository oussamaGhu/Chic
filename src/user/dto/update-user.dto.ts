import { PartialType } from '@nestjs/mapped-types';
import { CreateUserDto } from './create-user.dto';
import { IsDateString } from 'class-validator';
import { Transform } from 'class-transformer';
import { IsArray, IsDate, IsEmail,IsEnum,IsNumber,IsOptional,IsString ,Matches,MinLength,} from "class-validator";
import { Role } from "src/auth/Role.enums/Role.emun";
import { Types } from 'mongoose';
export class UpdateUserDto extends PartialType(CreateUserDto) {
    
       
        
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
      }
