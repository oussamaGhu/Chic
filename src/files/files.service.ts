import { Injectable } from '@nestjs/common';
import { CreateFileDto } from './dto/create-file.dto';
import { UpdateFileDto } from './dto/update-file.dto';
import { Files } from 'src/user/schema/files.schema';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import * as fs from 'fs';
import { v4 as uuidv4 } from 'uuid';
import * as path from 'path';
import axios from 'axios';

@Injectable()
export class FilesService {
  constructor(@InjectModel(Files.name) private readonly fileModel: Model<Files>) {}

  async create(fileData: CreateFileDto){
    const newFile = new this.fileModel(fileData);
   
    return newFile.save();
  }


  async removeBg(buffer: Buffer): Promise<Buffer> {
    const formData = new FormData();
  
    // Convert Buffer to Blob
    const blob = new Blob([buffer], { type: 'image/jpeg' });
  
    formData.append('size', 'auto');
    formData.append('image_file', blob, 'file.jpg');
  
    const response = await fetch('https://api.remove.bg/v1.0/removebg', {
      method: 'POST',
      headers: { 'X-Api-Key': process.env.REMOVE_BG_API_KEY },
      body: formData,
    });
  
    if (response.ok) {
      const arrayBuffer = await response.arrayBuffer();
      return Buffer.from(arrayBuffer);
    } else {
      throw new Error(`${response.status}: ${response.statusText}`);
    }
  }


  async downloadImageAndUpload(imageUrl : string) {
    //const imageUrl = 'https://lh3.googleusercontent.com/a/ACg8ocIrw_m_OUrcy8UZMoQQlC2CEQEOO2Sn-UAcAm3aG1VVjgDbIg=s200';
    const filePath = path.join("uploads/profile", `${uuidv4()}.png`);
  
    try {
      // Step 1: Download image from the URL
      const response = await axios({
        url: imageUrl,
        method: 'GET',
        responseType: 'stream',
      });
  
      // Step 2: Save the image to the local file system
      await new Promise((resolve, reject) => {
        response.data.pipe(fs.createWriteStream(filePath))
          .on('finish', resolve)
          .on('error', reject);
      });
  
      // Step 3: Create a file object with the required fields
      const file: Express.Multer.File = {
        fieldname: 'file', // Dummy value, can be anything
        originalname: 'image.png',
        encoding: '7bit', // Default encoding
        mimetype: 'image/png',
        size: fs.statSync(filePath).size,
        destination: path.dirname(filePath),
        filename: path.basename(filePath),
        path: filePath,
        buffer: fs.readFileSync(filePath), // Optional, can be used if needed
        stream: fs.createReadStream(filePath), // Required if you want to use stream
      };
  
      // Step 4: Call the uploadFileToService function and return the fileId
      const type = 'user'; // Example type for the upload
      const result = await this.uploadFileToService(file, type);
      console.log(result); // The result will contain the fileId
      return result.fileId; // Return the fileId from the upload result
    } catch (error) {
      console.error('Error:', error.message);
      throw new Error('Failed to download and upload image');
    }
  }




  async uploadFileToService(file: Express.Multer.File, type: string): Promise<{ fileId: string }> {
    if (!file) {
      throw new Error('File is required');
    }

    try {
      // Ensure the file exists
      if (!fs.existsSync(file.path)) {
        throw new Error(`File not found at path: ${file.path}`);
      }

      let filePath = file.path;

      // Process image if type is 'assemble'
      if (type !== 'user') {
        const fileBuffer = fs.readFileSync(file.path);
        const transparentBuffer = await this.removeBg(fileBuffer);
        
        // Generate a new file path for the processed image
        filePath = `${file.destination}${uuidv4()}-no-bg.png`;
        fs.writeFileSync(filePath, transparentBuffer);
      }

      // Prepare the DTO to save the file record in the database
      const createFileDto: CreateFileDto = {
        name: file.originalname,
        path: filePath,
        mimetype: file.mimetype,
        size: fs.statSync(filePath).size,
      };

      // Save file record and return the file ID
      const fileRecord = await this.create(createFileDto);
      return { fileId: (fileRecord._id as string) };
    } catch (error) {
      throw new Error('Failed to process file');
    }
  }


  async findOne(id: string):Promise<Files> {
    return await this.fileModel.findById(id).exec();
  }


  async findAll(): Promise<Files[]> {
    return this.fileModel.find().exec();
  }

}