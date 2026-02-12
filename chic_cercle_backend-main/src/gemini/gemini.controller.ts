import { Controller, Get, Post, Body, Patch, Param, Delete } from '@nestjs/common';
//import { GeminiService } from './gemini.service';
import { CreateGeminiDto } from './dto/generate_outfit.dto';
import { UpdateGeminiDto } from './dto/update-gemini.dto';
import { GeminiService } from './gemini.service';
import { CreateAssembleDto } from 'src/assemble/dto/create-assemble.dto';
import { CreateClotheDto } from 'src/clothes/dto/create-clothe.dto';
import { Clothes } from 'src/user/schema/clothesSchema';
import { RequestGeminiDto } from './dto/request-gemini.dto';

@Controller('gemini')
export class GeminiController {
  constructor(private readonly geminiService: GeminiService) {}
  

 
  
   @Post('generate')
    async generateOutfit(@Body() outfitDto: RequestGeminiDto): Promise<Clothes[]> {
      return await this.geminiService.generateOutfit(outfitDto);
    }



  
  
  }
  