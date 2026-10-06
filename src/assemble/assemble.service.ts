import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';

import { CreateAssembleDto } from './dto/create-assemble.dto';
import { UpdateAssembleDto } from './dto/update-assemble.dto';
import { Assemble } from 'src/user/schema/assemble.schema';


@Injectable()
export class AssembleService {
  constructor(
    @InjectModel(Assemble.name) private readonly assembleModel: Model<Assemble>,
  ) {}

  async create(createAssembleDto: CreateAssembleDto): Promise<Assemble> {
    const newAssemble = new this.assembleModel(createAssembleDto);
    return newAssemble.save();
  }

  async findAll(): Promise<Assemble[]> {
    return this.assembleModel.find().populate('clothes').exec();
  }

  async findOne(id: string): Promise<Assemble | null> {
    const assemble = await this.assembleModel.findById(id).populate('clothes').exec();
    if (!assemble) {
      throw new NotFoundException(`Assemble with ID "${id}" not found`);
    }
    return assemble;
  }

  async getByUser(id: string): Promise<Assemble | null> {
    const assemble = await this.assembleModel.findOne({ user: id }).exec();
    return assemble;
  }

  async update(id: string, updateAssembleDto: UpdateAssembleDto): Promise<Assemble | null> {
    const updatedAssemble = await this.assembleModel
      .findByIdAndUpdate(id, updateAssembleDto, { new: true })
      .populate('clothes')
      .exec();
    if (!updatedAssemble) {
      throw new NotFoundException(`Assemble with ID "${id}" not found`);
    }
    return updatedAssemble;
  }

  async remove(id: string): Promise<Assemble> {
    const result = await this.assembleModel.findByIdAndDelete(id).exec();
    if (!result) {
      throw new NotFoundException(`Assemble with ID "${id}" not found`);
    }
    return result;
  }
}
