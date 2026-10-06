import { Schema, Prop, SchemaFactory } from '@nestjs/mongoose';
import { Document, Types } from 'mongoose';

@Schema()
export class Assemble extends Document {
  @Prop()
 name : string; 
 @Prop()
 price : number; 

 @Prop()
 image : string;

 @Prop()
 user : string;


  @Prop()
  date: Date; 
  @Prop({ type: [{ type: Types.ObjectId, ref: 'Clothes' }] })
  clothes: Types.ObjectId[];

}

export const AssembleSchema = SchemaFactory.createForClass(Assemble);