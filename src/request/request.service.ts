import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { CreateRequestDto } from './dto/create-request.dto';
import { UpdateRequestDto } from './dto/update-request.dto';
import { Request } from 'src/user/schema/request.schema'; // Adjust the import path as needed
import { ClothesService } from 'src/clothes/clothes.service';
import { AssembleService } from 'src/assemble/assemble.service';

@Injectable()
export class RequestService {
  constructor(@InjectModel(Request.name) private requestModel: Model<Request>,    private clothesService: ClothesService,private assembleService: AssembleService // Inject ClothesService
) {}

  // Créer une nouvelle demande
  async create(createRequestDto: CreateRequestDto): Promise<Request> {
    const createdRequest = new this.requestModel(createRequestDto);
    return createdRequest.save();
  }

  // Récupérer toutes les demandes
  async findAll(): Promise<Request[]> {
    return this.requestModel.find().exec();
  }
 // Trouver une demande par un champ unique
 async findOneByUniqueFields(field1: string, field2: string): Promise<Request | null> {
  
  return this.requestModel.findOne({ clientId:field1, itemId: field2 }).exec();
}
  // Récupérer une demande par son ID
  async findOne(id: string): Promise<Request> {
    const request = await this.requestModel.findById(id).exec();
    if (!request) {
      throw new NotFoundException(`Demande avec l'ID ${id} non trouvée`);
    }
    return request;
  }


  async update(id: string, updateRequestDto: UpdateRequestDto): Promise<Request> {
    // Step 1: Retrieve the request by its ID
    const request = await this.requestModel.findById(id).exec();
    if (!request) {
      console.log(`Request not found with ID ${id}`);
      throw new NotFoundException(`Demande avec l'ID ${id} non trouvée`);
    }
    console.log(`Request found with ID ${id}:`, request);

    if(updateRequestDto.isClothes){
    // Step 2: Get the clothes item using the clothesId from the UpdateRequestDto
    const clothe = await this.clothesService.findOne(updateRequestDto.itemId);
    if (!clothe) {
      console.log(`Clothes not found with ID ${updateRequestDto.clothesId}`);
      throw new NotFoundException(`Article de vêtement avec l'ID ${updateRequestDto.clothesId} non trouvé`);
    }
    console.log(`Clothes found with ID ${updateRequestDto.clothesId}:`, clothe);
  
    // Step 3: Update the clothe with the new user (clientId) and price = 0
    clothe.user = updateRequestDto.clientId;
    clothe.price = 0;
    await clothe.save();
    console.log(`Clothes updated with new user ${updateRequestDto.clientId} and price set to 0`);
  
    }else{
      const assemble = await this.assembleService.findOne(updateRequestDto.itemId);
      if (!assemble) {
        console.log(`Clothes not found with ID ${updateRequestDto.clothesId}`);
        throw new NotFoundException(`Article de vêtement avec l'ID ${updateRequestDto.clothesId} non trouvé`);
      }
      //console.log(`Clothes found with ID ${updateRequestDto.clothesId}:`, assemble);
    
      // Step 3: Update the clothe with the new user (clientId) and price = 0
      assemble.user = updateRequestDto.clientId;
      assemble.price = 0;
      await assemble.save();
      //console.log(`Clothes updated with new user ${updateRequestDto.clientId} and price set to 0`);
    }
  

    // Step 4: Delete all requests with clothesId not equal to clientId
    await this.requestModel.deleteMany({
      clothesId: updateRequestDto.clothesId,
      clientId: { $ne: updateRequestDto.clientId }
    }).exec();
   // console.log(`Deleted requests with clothesId not equal to ${updateRequestDto.clothesId} and clientId not equal to ${updateRequestDto.clientId}`);
  
    // Step 5: Update the request to set isSold to true
    request.isSold = true;
    await request.save();
    console.log(`Request with ID ${id} updated. isSold set to true`);
  
    return request;
  }


  // Supprimer une demande par son ID
  async remove(id: string): Promise<Request> {
    const request = await this.requestModel.findByIdAndDelete(id).exec();
    if (!request) {
      throw new NotFoundException(`Demande avec l'ID ${id} non trouvée`);
    }
    return request;
  }

}