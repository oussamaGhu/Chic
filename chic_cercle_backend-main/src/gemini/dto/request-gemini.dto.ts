
import { Type } from 'class-transformer';
import { ValidateNested, IsArray } from 'class-validator';
import { OptionDto } from './option-gemini.dto';
import { CreateClotheDto } from 'src/clothes/dto/create-clothe.dto';

export class RequestGeminiDto {

 
  clothe: CreateClotheDto;

  @IsArray()
  options: OptionDto[];
}