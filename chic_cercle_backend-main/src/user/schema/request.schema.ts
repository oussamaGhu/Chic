import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import mongoose, { Document } from 'mongoose';
@Schema({ versionKey: false, timestamps: true })
export class Request extends Document {
  @Prop({ required: false })
  sellerId?: string;

  @Prop({ required: false })
  clientId?: string;

  @Prop({ required: false })
  nameClient?: string;

  @Prop({ required: false })
  clientPhone?: number;

  @Prop({ required: false })
  clientMail?: string;

  @Prop({ required: false })
  itemId?: string;

  @Prop({ required: false })
  isClothes?: boolean;

  @Prop({ required: false })
  isSold?: boolean;

  @Prop({ required: false })
  nameSeller?: string;

  @Prop({ required: false })
  nameClothes?: string;

  @Prop({ required: false })
  clothesId?: string;

  @Prop({ required: false })
  date?: Date;

  @Prop({ required: false })
  price?: number;

}

export const RequestSchema = SchemaFactory.createForClass(Request);