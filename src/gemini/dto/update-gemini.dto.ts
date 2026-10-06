import { PartialType } from '@nestjs/swagger';
import { CreateGeminiDto } from './generate_outfit.dto';

export class UpdateGeminiDto extends PartialType(CreateGeminiDto) {}
