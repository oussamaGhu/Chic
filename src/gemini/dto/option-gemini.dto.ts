import { IsString, IsOptional, IsArray, IsEnum, IsBoolean } from "class-validator";
import { ObjectId ,Types} from "mongoose";
import { Moods } from "src/clothes/enums/moods.eum";
import { Occasion } from "src/clothes/enums/occation.enum";
import { TypesC } from "src/clothes/enums/TypesC.enum";
import { Weather } from "src/clothes/enums/weather.enum";


export class OptionDto {

  @IsBoolean()
  enable: boolean;

  @IsEnum(TypesC, { each: true }) 
  type: string;

  @IsOptional()
  id: string;

  @IsOptional()
  color: string;

}