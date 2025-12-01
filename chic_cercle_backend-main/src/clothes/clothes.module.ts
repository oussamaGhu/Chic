import { Module } from '@nestjs/common';
import { ClothesService } from './clothes.service';
import { ClothesController } from './clothes.controller';
import { MongooseModule } from '@nestjs/mongoose';
import { Clothes, ClothesSchema } from 'src/user/schema/clothesSchema';
import { FilesModule } from 'src/files/files.module';

@Module({
  imports: [
    MongooseModule.forFeature([
        {
            name: Clothes.name,
            schema:ClothesSchema,
        }
    ]),
    FilesModule
],
  controllers: [ClothesController],
  providers: [ClothesService],
  exports:[MongooseModule,ClothesService]
})
export class ClothesModule {}
