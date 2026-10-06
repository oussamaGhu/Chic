import * as fs from 'fs';
import * as path from 'path';
import * as mime from 'mime-types';
import { Injectable } from '@nestjs/common';
import { NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';

import { ConfigService } from '@nestjs/config';
import { HttpService } from '@nestjs/axios';

 // Assurez-vous d'importer la bibliothèque correcte
import { CreateClotheDto } from 'src/clothes/dto/create-clothe.dto';
import { Clothes } from 'src/user/schema/clothesSchema';
import { ClothesService } from 'src/clothes/clothes.service';
import { FilesService } from 'src/files/files.service';
import { GoogleGenerativeAI } from '@google/generative-ai';
import { OptionDto } from './dto/option-gemini.dto';
import { RequestGeminiDto } from './dto/request-gemini.dto';

@Injectable()
export class GeminiService {
  private readonly geminiKey: string;
  private readonly geminiModel = 'gemini-1.5-flash'; // Modèle utilisé

  constructor(
    private readonly clothesService: ClothesService,
    private readonly configService: ConfigService,
    private readonly httpService: HttpService,
    private readonly fileService: FilesService,
    @InjectModel('Clothes') private clothesModel: Model<Clothes>
  ) {
    this.geminiKey = this.configService.get<string>('GEMINI_API_KEY');
  }

  async generateOutfit(outfitDto: RequestGeminiDto): Promise<Clothes[]> {
    console.log('Critères reçus:', outfitDto);

    // 1. Récupérer les vêtements correspondant aux critères
    const clothes = await this.clothesService.getClothesForCriteria(outfitDto.clothe);
    console.log('Vêtements trouvés:', clothes);

    if (!clothes.length) {
      throw new NotFoundException('Aucun vêtement ne correspond aux critères donnés.');
    }

    // 2. Créer le prompt pour Gemini
    const prompt = this.createPromptOption(outfitDto.options, clothes);

    // 3. Préparer les images en base64
    const imageParts = await Promise.all(
      clothes.map(async (cloth) => {
        if (cloth.images) {
          const fileId = typeof cloth.images === 'string' ? cloth.images : cloth.images;
          const fileRecord = await this.fileService.findOne(fileId); // Trouver le fichier par ID
          const imagePath = path.resolve('/Users/ahmeddouss/Documents/GitHub/ChicCircle_Backend/', fileRecord.path);

 // Utiliser le chemin absolu

          if (!fs.existsSync(imagePath)) {
            console.error('Fichier introuvable:', imagePath);
            return null; // Si l'image n'existe pas
          }

          // Vérifier le type MIME de l'image
          const mimeType = mime.lookup(imagePath) || 'image/*'; // Par défaut 'image/jpeg'

          // Convertir l'image en base64
          const base64Data = Buffer.from(fs.readFileSync(imagePath)).toString('base64');
          return {
            inlineData: {
              data: base64Data,
              mimeType,
            },
          };
        }
        return null; // Si l'image n'est pas disponible
      })
    );
    const validImageParts = imageParts.filter(part => part !== null);

    // 4. Appeler l'API Gemini avec le prompt et les images
    const result = await this.generateTextWithImages(prompt, validImageParts);
    console.log('Réponse Gemini:', result);

    // Assuming the result is a list of outfit IDs
    const outfitIds = result; // This should be the array of outfit IDs

   // 5. Fetch the actual clothes from the database using the IDs
   const clothesList = await this.clothesService.findAll(); // Get all clothes
   const selectedClothes = clothesList.filter(cloth => outfitIds.includes(cloth.id)); // Filter by outfit IDs

   console.log('Vêtements sélectionnés:', selectedClothes);

    // 6. Return the clothes list
    return selectedClothes;


   
  }





  private async generateTextWithImages(prompt: string, imageParts: any[]): Promise<any> {
    const genAI = new GoogleGenerativeAI(this.geminiKey);
    const model = genAI.getGenerativeModel({ model: this.geminiModel });

    try {
      console.log(prompt)
      const result = await model.generateContent([prompt, ...imageParts]);
      if (result && result.response && result.response.text) {
        return result.response.text();
      } else {
        throw new Error('Réponse invalide de Gemini');
      }
    } catch (error) {
      console.error('Erreur lors de l’appel à l’API Gemini :', error);
      throw new Error('Impossible de générer une réponse depuis Gemini.');
    }
  }

  private extractIdsFromResponse(response: string): string[] {
    // Extract IDs assuming response format is ['id1', 'id2', ...]
    try {
      const ids = JSON.parse(response);
      if (Array.isArray(ids)) {
        return ids.filter(id => typeof id === 'string');
      }
      throw new Error('Réponse non conforme : attendue liste d\'IDs');
    } catch (error) {
      console.error('Erreur d\'analyse de la réponse Gemini:', error);
      throw new Error('Impossible de parser la réponse Gemini.');
    }
  }

  private createPromptOption(optionDtos: OptionDto[], clothes: Clothes[]): string {
    // Generate descriptions for clothes
    const clothesDescriptions = clothes
        .map((clothing, index) => 
            `- ${index} (index to link with image the same order)  clothes with idClothes = ${clothing.id} which is a ${clothing.types} of color ${clothing.colors}.`
        )
        .join('\n');

    // Generate combination parts based on options
    const generateCombinationPart = (type: string, option: OptionDto | null): string => {
        if (!option.enable) {
            return ''; // Skip this type if enable is false or option is null
        }

        if (option.id) {
            return `The ${type} with id "${option.id}" and the corresponding index.`;
        } else if (option.color) {
            return `1 single ${type} of color "${option.color}".`;
        } else {
            return `1 single ${type}.`;
        }
    };
console.log(optionDtos)
    // Iterate over optionDtos to generate all parts
    const combinationParts = optionDtos
        .map((option) => generateCombinationPart(option.type, option))
        .filter((part) => part) // Filter out empty parts
        .join('\n');

    // Return the final prompt
    return `Based on the following list of clothes and images (in the same order):
${clothesDescriptions}

I want a suggestion for a combination of:
${combinationParts}

Return only the idClothes of the suggested outfit in list string no discription only list string`;
}





}
