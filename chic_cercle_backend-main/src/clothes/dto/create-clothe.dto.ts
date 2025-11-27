import { IsArray, IsEnum, IsOptional, IsString } from 'class-validator';
import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { HydratedDocument, SchemaTypes, Types } from 'mongoose';

import { Occasion } from "src/clothes/enums/occation.enum";
import { TypesC } from "src/clothes/enums/TypesC.enum";
import { Weather } from "src/clothes/enums/weather.enum";
import { Moods } from '../enums/moods.eum';
import { User } from 'src/user/schema/usersSchema';


export class CreateClotheDto {
  user: string; // Reference to the user
  @IsArray()
  @IsEnum(Occasion, { each: true }) 
  occasions: Occasion[];

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

  @IsString()
  description: string;

  @IsString()
  size: string;
}

