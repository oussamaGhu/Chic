import { IsString, IsOptional, IsArray, IsEnum } from "class-validator";
import { ObjectId ,Types} from "mongoose";
import { Moods } from "src/clothes/enums/moods.eum";
import { Occasion } from "src/clothes/enums/occation.enum";
import { TypesC } from "src/clothes/enums/TypesC.enum";
import { Weather } from "src/clothes/enums/weather.enum";


export class CreateGeminiDto {
  @IsArray()
  @IsEnum(Occasion, { each: true }) 
  occasions: Occasion[];

  @IsString()
  images: Types.ObjectId;

  @IsArray()
  @IsEnum(Moods, { each: true })
  moods: Moods[];

  @IsArray()
  @IsEnum(Weather, { each: true }) 
  weather: Weather[];

  @IsArray()
  @IsEnum(TypesC, { each: true }) 
  types: TypesC[];

  @IsArray()
  @IsString({ each: true })
  colors: string[];

  
}


  

