import { Controller, Get, Post, Body, Patch, Param, Delete, UseInterceptors, UseGuards, UploadedFile, Put, Req } from '@nestjs/common';
import { UserService } from './user.service';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';
import { NotFoundException } from '@nestjs/common/exceptions';
import { ApiOperation, ApiResponse, ApiTags } from '@nestjs/swagger';

@ApiTags('Users')
@Controller('user')
export class UserController {
  constructor(private readonly userService: UserService) {}
  @ApiOperation({ summary: 'Create a new user' })
  @ApiResponse({ status: 201, description: 'The user has been successfully created.' })
  @Post()
 async  create(@Body() createUserDto: CreateUserDto) {
    return this.userService.create(createUserDto);
  }
  @ApiOperation({ summary: 'Get all users' }) // Décrit cette opération dans Swagger
  @ApiResponse({ status: 200, description: 'List of users' })
  @Get()
  findAll() {
    return this.userService.findAll();
  }

  @ApiOperation({ summary: 'Add clothes to a specific user' })
  @Post('addClothes/:id')
  async addClothes(@Param('id') id: string, @Body() clothesData: any) {
    const user = await this.userService.addClothesToUser(id, clothesData);
    if (!user) {
      throw new NotFoundException(`User with ${id} not found`);
    }
    return user;
  }

  @ApiOperation({ summary: 'Get user by Id' })
  @Get(':id')
  async findOne(@Param('id') id: string) {
    const user = await this.userService.findOne(id);
    if(!user) {
      throw new NotFoundException(`user with ${id} not found`);
    }
    return user;
  }
  
  
  @ApiOperation({ summary: 'Update user by Id' })
  @Patch(':id')
  async update(@Param('id') id: string, @Body() updateUserDto: UpdateUserDto) {
    const updateduser= await  this.userService.update(id, updateUserDto);
    if (!updateduser) {
      throw new NotFoundException(`user with ${id} not found`);

    }
    return updateduser
  }
  @ApiOperation({ summary: 'Delete user by Id' })
  @Delete(':id')
  async remove(@Param('id') id: string) {
    const result =await this.userService.remove(id)
    if (!result){
      throw new NotFoundException(`user with ${id} not found`);
    }
    return  { message: `User with name ${id} deleted successfully` } ;

  }

  

}



















