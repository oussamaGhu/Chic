import { PartialType } from '@nestjs/swagger';
import { CreateRequestDto } from './create-request.dto';
import { IsString, IsBoolean, IsNumber, IsOptional, IsEmail } from 'class-validator';

export class UpdateRequestDto extends PartialType(CreateRequestDto) {

    @IsString()
    sellerId: string;
  
    @IsString()
    clientId: string;
  
    @IsString()
    nameClient: string;
  
    @IsNumber()
    clientPhone: number;
  
    @IsEmail()
    clientMail: string;
  
    @IsString()
    itemId: string;
  
    @IsBoolean()
    isClothes: boolean;
  
    @IsBoolean()
    isSold: boolean;
  
    @IsString()
    nameSeller: string;
  
    @IsString()
    nameClothes: string;
  
    @IsString()
    clothesId: string;

    @IsNumber()
  price: number;
}
