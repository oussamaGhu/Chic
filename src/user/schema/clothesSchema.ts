import { Prop, Schema, SchemaFactory } from "@nestjs/mongoose";
import { Document, Types } from "mongoose";
import { Moods } from "src/clothes/enums/moods.eum";
import { Occasion } from "src/clothes/enums/occation.enum";
import { TypesC } from "src/clothes/enums/TypesC.enum";
import { Weather } from "src/clothes/enums/weather.enum";
import { Files } from './files.schema';
import { HydratedDocument, SchemaTypes } from 'mongoose';
import { User } from "./usersSchema";


@Schema()
export class Clothes extends Document {

  @Prop()
  user: string; 

  @Prop({ type: [String], enum: Occasion })
  occasions: Occasion[]; 

  @Prop()
  images?: string; 

  @Prop({ type: [String], enum: Moods })
  moods: Moods[]; 

  @Prop({ type: [String], enum: Weather })
  weather: Weather[]; 

  @Prop({ type: [String], enum: TypesC })
  types: TypesC[]; 
  
  @Prop({ type: [String] })
  colors: string[]; 

  @Prop({ type: String })
  name: string; 

  @Prop()
  price: number;

  @Prop()
  size?: string;

  @Prop()
  description?: string;
}

export const ClothesSchema = SchemaFactory.createForClass(Clothes);
