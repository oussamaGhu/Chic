import { IsNumber, IsString } from "class-validator";

export class CreateFileDto {

    @IsString()
    name: string;
  
    @IsString()
    path: string;
  
    @IsString()
    mimetype: string;
  
    @IsNumber()
    size: number;
  }