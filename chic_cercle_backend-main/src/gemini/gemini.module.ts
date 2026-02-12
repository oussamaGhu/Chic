import { Module } from '@nestjs/common';
import { GeminiService } from './gemini.service';
import { GeminiController } from './gemini.controller';
import { ClothesModule } from 'src/clothes/clothes.module';
import { HttpModule } from '@nestjs/axios';
import { ConfigModule } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';

import { Clothes, ClothesSchema } from 'src/user/schema/clothesSchema';
import { FilesModule } from 'src/files/files.module';

@Module({
  imports: [
    MongooseModule.forFeature([{ name: Clothes.name, schema: ClothesSchema }]),
    HttpModule,
    ConfigModule,ClothesModule,FilesModule],
  controllers: [GeminiController],
  providers: [GeminiService],
})
export class GeminiModule {}
