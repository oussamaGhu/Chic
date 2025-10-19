import { IsArray, IsString ,IsEmail,IsDate,IsNumber, IsOptional, IsEnum} from "class-validator";
import { Role } from "../Role.enums/Role.emun";
import { Types } from 'mongoose';
export  class signUpDto {
   
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
  pictureProfile: string;
  
  @IsOptional()
  @IsDate()
  birthday: Date;

  @IsOptional()
  @IsArray()
  clothes: Types.ObjectId[];
  
  @IsEnum(Role)
  role: Role;

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
}
