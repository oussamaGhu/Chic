import { Module } from '@nestjs/common';
import { FilesService } from './files.service';
import { FilesController } from './files.controller';
import { Files, FilesSchema } from 'src/user/schema/files.schema';
import { MongooseModule } from '@nestjs/mongoose';
import { MulterModule } from '@nestjs/platform-express';
import { ServeStaticModule } from '@nestjs/serve-static';
import { join } from 'path';
@Module({

  imports: [
    MongooseModule.forFeature([{ name: Files.name, schema: FilesSchema }]), // Déclarez le schéma File
    MulterModule.register({ // Optionnel, configuration globale pour Multer
      dest: './uploads', // Répertoire de téléchargement
    }),
   
  ],


  controllers: [FilesController],
  providers: [FilesService],
  exports: [FilesService, MongooseModule],
})
export class FilesModule {}
