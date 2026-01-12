import { Controller, Get, Post, Body, Param, Patch, Delete } from '@nestjs/common';
import { AssembleService } from './assemble.service';
import { CreateAssembleDto } from './dto/create-assemble.dto';
import { UpdateAssembleDto } from './dto/update-assemble.dto';
import { Assemble } from 'src/user/schema/assemble.schema';



@Controller('assemble')
export class AssembleController {
  constructor(private readonly assembleService: AssembleService) {}

  @Post()
  create(@Body() createAssembleDto: CreateAssembleDto): Promise<Assemble> {
    return this.assembleService.create(createAssembleDto);
  }

  @Get()
  findAll(): Promise<Assemble[]> {
    return this.assembleService.findAll();
  }

  @Get(':id')
  findOne(@Param('id') id: string): Promise<Assemble | null> {
    return this.assembleService.findOne(id);
  }

  @Patch(':id')
  update(@Param('id') id: string, @Body() updateAssembleDto: UpdateAssembleDto): Promise<Assemble | null> {
    return this.assembleService.update(id, updateAssembleDto);
  }

  @Get('getByUser/:id')
  getByUser(@Param('id') id: string): Promise<Assemble | null> {
    return this.assembleService.getByUser(id);
  }

  @Delete(':id')
  remove(@Param('id') id: string): Promise<Assemble> {
    return this.assembleService.remove(id);
  }
}
