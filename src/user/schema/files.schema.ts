import { Prop, Schema, SchemaFactory } from "@nestjs/mongoose";
import mongoose, { HydratedDocument } from "mongoose";
import { Document } from "mongoose";

@Schema()
export class Files extends Document {
  @Prop()
  filename: string;

  @Prop()
  path: string;

  @Prop()
  mimetype: string;

  @Prop()
  size: number;

}



export const FilesSchema = SchemaFactory.createForClass(Files);