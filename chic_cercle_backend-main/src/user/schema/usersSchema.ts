import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import mongoose,{ Document} from 'mongoose'; 
import { Role } from 'src/auth/Role.enums/Role.emun';
import { Types } from 'mongoose';
import { HydratedDocument, SchemaTypes } from 'mongoose';
import { Assemble } from './assemble.schema';
import { Clothes } from './clothesSchema';
import { Files } from './files.schema';




@Schema()
export class User extends Document {

  @Prop()
  name: string;

  @Prop()
  email: string;

  @Prop()
  password: string;

  @Prop()
  phoneNumber?: number;  // Optionnel

  
  @Prop()
  birthday?: Date;  // Optionnel

  @Prop({ type: [SchemaTypes.ObjectId], ref: 'Clothes' })
  clothes?: Clothes[];

  @Prop({ type: [SchemaTypes.ObjectId], ref: 'Assemble' })
  assemble?: Assemble[];


  @Prop()
  pictureProfile?: string;   // Optionnel

  @Prop()
  height?: number;  // Optionnel

  @Prop()
  weight?: number;  // Optionnel

  @Prop()
  shape?: string;  // Optionnel

  @Prop()
  gender?: string;  // Optionnel

  @Prop()
  location?: string;  // Optionnel
  @Prop({ type: String, enum: Role, required: true })  // Définit le rôle avec enum
  role: Role;

  @Prop({ type: [String], default: [] })
  request?: string[];  

  @Prop()
     deviceId: string;
}

// Création du schéma à partir de la classe User
export const UserSchema = SchemaFactory.createForClass(User);
