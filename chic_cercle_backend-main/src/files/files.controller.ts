import {
  Controller,
  Get,
  Post,
  Param,
  Res,
  HttpException,
  HttpStatus,
  UseInterceptors,
  UploadedFile,
} from '@nestjs/common';
import { FileInterceptor } from '@nestjs/platform-express';
import { diskStorage } from 'multer';
import { extname } from 'path';
import { v4 as uuidv4 } from 'uuid';
import { CreateFileDto } from './dto/create-file.dto';
import { FilesService } from './files.service';
import { Response } from 'express';
import * as fs from 'fs';
import { Model } from 'mongoose';
import * as path from 'path';
import axios from 'axios';

@Controller('file')
export class FilesController {
  constructor(private readonly fileService: FilesService) {}

  
  @Post('upload/:type')
  @UseInterceptors(
    FileInterceptor('file', {
      storage: diskStorage({
        destination: (req, file, callback) => {
          const { type } = req.params;
          let folder = 'uploads/';
          if (type === 'user') {
            folder += 'profile/';
          } else if (type === 'clothes') {
            folder += 'clothes/';
          } else if (type === 'assemble') {
            folder += 'assemble/';
          }
          fs.mkdirSync(folder, { recursive: true }); // Ensure folder exists
          callback(null, folder);
        },
        filename: (req, file, callback) => {
          const uniqueSuffix = `${uuidv4()}${extname(file.originalname)}`;
          callback(null, uniqueSuffix);
        },
      }),
    }),
  )



  

  async uploadFile(
    @Param('type') type: string,
    @UploadedFile() file: Express.Multer.File,
  ) {
    try {
      const result = await this.fileService.uploadFileToService(file, type);
      return result;
    } catch (error) {
      console.error('Error in uploadFile:', error.message);
      throw new Error('Failed to process file');
    }
  }

  


  @Get(':id')
  async getImage(@Param('id') id: string, @Res() res: Response) {
    try {
      const file = await this.fileService.findOne(id);
      if (!file) {
        throw new HttpException('File not found', HttpStatus.NOT_FOUND);
      }

      const imagePath = file.path;
      return res.sendFile(imagePath, { root: './' }); // Send the file as a response
    } catch (error) {
      console.error(error);
      throw new HttpException('Failed to retrieve file', HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Get()
  async getAllFiles(@Res() res: Response) {
    try {
      const files = await this.fileService.findAll();
      return res.json(files);
      } catch (error) {
        console.error(error);
        throw new HttpException('Failed to retrieve files', HttpStatus.INTERNAL_SERVER_ERROR);
        }
        }



  
}