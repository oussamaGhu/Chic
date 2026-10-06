import { IsArray, IsDate, IsString } from "class-validator";
import { Types } from "mongoose";

export class CreateAssembleDto {
@IsString()
price : string;
@IsString()
name:string;
@IsDate()
date:string;
@IsArray()
clothes: Types.ObjectId[];
}

