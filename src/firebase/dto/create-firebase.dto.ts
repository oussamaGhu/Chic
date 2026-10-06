import { IsString } from "class-validator";

export class CreateFirebaseDto {
    @IsString()
    title: string;
    @IsString()
    body: string;
    @IsString()
    deviceId: string;
}
