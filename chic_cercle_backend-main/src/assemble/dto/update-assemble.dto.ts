import { PartialType } from '@nestjs/mapped-types';
import { CreateAssembleDto } from './create-assemble.dto';

export class UpdateAssembleDto extends PartialType(CreateAssembleDto) {}
