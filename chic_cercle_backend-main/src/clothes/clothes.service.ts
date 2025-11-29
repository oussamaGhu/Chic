import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { CreateClotheDto } from './dto/create-clothe.dto';
import { UpdateClotheDto } from './dto/update-clothe.dto';
import { Clothes } from 'src/user/schema/clothesSchema';
import { CreateGeminiDto } from 'src/gemini/dto/generate_outfit.dto';
import { CreateFileDto } from 'src/files/dto/create-file.dto';
import { Files } from 'src/user/schema/files.schema';

@Injectable()
export class ClothesService {
  constructor(@InjectModel(Clothes.name) private clothesModel: Model<Clothes>)
 {}

  // Créer un nouvel article de vêtement
  async create(createClotheDto: CreateClotheDto): Promise<Clothes> {

    let fileRecord = null;
    

    const createdClothe = new this.clothesModel(createClotheDto);
  
    return createdClothe.save();
  }

  // Récupérer tous les articles de vêtements
  async findAll(): Promise<Clothes[]> {
    return this.clothesModel.find().exec();
  }

  async findByUserId(id:string): Promise<Clothes[]> {
    return this.clothesModel.find({user:id}).exec();
  }

  // Récupérer un article de vêtement par son ID
  async findOne(id: string): Promise<Clothes> {
    const clothe = await this.clothesModel.findById(id).exec();
    if (!clothe) {
      throw new NotFoundException(`Article de vêtement avec l'ID ${id} non trouvé`);
    }
    return clothe;
  }

  // Mettre à jour un article de vêtement par son ID
  async update(id: string, updateClotheDto: UpdateClotheDto): Promise<Clothes> {
    const clothe = await this.clothesModel.findByIdAndUpdate(id, updateClotheDto, { new: true }).exec();
    if (!clothe) {
      throw new NotFoundException(`Article de vêtement avec l'ID ${id} non trouvé`);
    }
    return clothe;
  }

  // Supprimer un article de vêtement par son ID
  async remove(id: string): Promise<Clothes> {
    const clothe = await this.clothesModel.findByIdAndDelete(id).exec();
    if (!clothe) {
      throw new NotFoundException(`Article de vêtement avec l'ID ${id} non trouvé`);
    }
    return clothe;
  }
  async getClothesForCriteria(outfitDto: CreateClotheDto): Promise<Clothes[]> {
    try {
      // Create a filter object
      const filter: any = {};
  
      // Dynamically add filter conditions for fields that are provided
      if (outfitDto.occasions && outfitDto.occasions.length > 0) {
        filter.occasions = { $in: outfitDto.occasions };
      }
      if (outfitDto.moods && outfitDto.moods.length > 0) {
        filter.moods = { $in: outfitDto.moods };
      }
      if (outfitDto.weather && outfitDto.weather.length > 0) {
        filter.weather = { $in: outfitDto.weather };
      }
      if (outfitDto.colors && outfitDto.colors.length > 0) {
        filter.colors = { $in: outfitDto.colors };
      }
      if (outfitDto.types && outfitDto.types.length > 0) {
        filter.types = { $in: outfitDto.types };
      }
      if (outfitDto.user) {
        filter.user = outfitDto.user;
      }
  
      // Perform the query with the constructed filter
      const cloth = await this.clothesModel.find(filter).exec();
  
      console.log('Vêtements récupérés:', cloth); // Afficher les résultats
      return cloth;
    } catch (error) {
      console.error('Erreur lors de la recherche des vêtements:', error);
      throw new Error('Impossible de récupérer les vêtements selon les critères.');
    }
  } }
