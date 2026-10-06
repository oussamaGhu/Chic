import {
  Controller,
  Get,
  Post,
  Body,
  Patch,
  Param,
  Delete,
  Put,
  Req,
  UploadedFile,
  UseGuards,
  UseInterceptors,
} from '@nestjs/common';
import { ClothesService } from './clothes.service';
import { CreateClotheDto } from './dto/create-clothe.dto';
import { UpdateClotheDto } from './dto/update-clothe.dto';
import { Clothes } from 'src/user/schema/clothesSchema';
import { FileInterceptor } from '@nestjs/platform-express';
import { diskStorage } from 'multer';
import { extname } from 'path';
import { AuthenticationGuard } from 'src/auth/Guard/guard';
import { v4 as uuidv4 } from 'uuid';

@Controller('clothes')
export class ClothesController {
  constructor(private readonly clothesService: ClothesService) {}

  // Créer un nouvel article de vêtement
  @Post('/addclothes')
  create(@Body() createClotheDto: CreateClotheDto): Promise<Clothes> {
    
    return this.clothesService.create(createClotheDto);
  }


  
  // Récupérer tous les articles de vêtements
  @Get()
  findAll(): Promise<Clothes[]> {
    return this.clothesService.findAll();
  }

  @Get("getByUser/:id")  
  findByUser(@Param('id') id: string,): Promise<Clothes[]> {
    return this.clothesService.findByUserId(id);
    }

    @Post('filter')
    async getOutfitFiltred(@Body() outfitDto: CreateClotheDto): Promise<Clothes[]> {
      return await this.clothesService.getClothesForCriteria(outfitDto);
    }
  

  // Récupérer un article de vêtement par son ID
  @Get(':id')
  findOne(@Param('id') id: string): Promise<Clothes> {
    return this.clothesService.findOne(id);
  }

  // Mettre à jour un article de vêtement par son ID
  @Patch(':id')
  update(
    @Param('id') id: string,
    @Body() updateClotheDto: UpdateClotheDto,
  ): Promise<Clothes> {
    return this.clothesService.update(id, updateClotheDto);
  }

  // Supprimer un article de vêtement par son ID
  @Delete(':id')
  remove(@Param('id') id: string): Promise<Clothes> {
    return this.clothesService.remove(id);
  }
  
 /* @UseGuards(AuthenticationGuard)
  @Put('complete_profile')
  @UseInterceptors(
    FileInterceptor('profilePicture', {  // Assurez-vous que 'profilePicture' est le champ du formulaire
      storage: diskStorage({
        destination: './uploads/users',  // Dossier de stockage des images
        filename: (req, file, callback) => {
          const uniqueSuffix = `${uuidv4()}${extname(file.originalname)}`;
          callback(null, uniqueSuffix);
        },
      }),
    }),
  )
  async completeProfile(
    @Body()  profileData: CreateClotheDto,
    @UploadedFile() pictureProfile: Express.Multer.File,@Req() req
  ) {
    if (pictureProfile) {
      profileData.images = pictureProfile;
    }
    let id: string = req.userId
    const updatedProfile = await this.clothesService.updateImage(id,profileData);

    return {
      message: 'Profil complété avec succès',
      data: updatedProfile,
    };
  }*/


}


