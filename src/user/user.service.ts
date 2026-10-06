import { Injectable, UnauthorizedException } from '@nestjs/common';
import { CreateUserDto } from './dto/create-user.dto';
import { UpdateUserDto } from './dto/update-user.dto';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { User } from './schema/usersSchema';
import { CreateFileDto } from 'src/files/dto/create-file.dto';

@Injectable()
export class UserService {

  constructor(
    @InjectModel(User.name) private  userModel: Model<User>,
   
  ) {}

  async create(dto: CreateUserDto): Promise<User> {
    const createdUser = new this.userModel({
      name: dto.name,
      email: dto.email,
      password: dto.password,
      role:dto.role,
      phoneNumber:dto.phoneNumber,
      pictureProfile:dto.pictureProfile,
      birthday:dto.birthday,
      height:dto.height,
      weight:dto.weight,
      shape:dto.shape,
      location:dto.location,
      request:dto.request,
      clothes:dto.clothes,
      assemble:dto.assemble,

    });
    return createdUser.save();
  }

  async addClothesToUser(id: string, clothesData: any) {
    const user = await this.userModel.findById(id).populate('assemble').populate('clothes').exec();
    if (!user) return null;

    user.clothes.push(clothesData);
    return user;
  }

  async findAll(): Promise<User[]> {
    return this.userModel.find().exec(); 
  }

  async findOne(id: string) {
    var user = await this.userModel.findById(id).exec();
    if ( user.role == "banned"){
      throw new UnauthorizedException('your account is banned');
    }


    return user;
  }

  async update(id: string, updateUserDto: UpdateUserDto): Promise<User> {
    return this.userModel.findByIdAndUpdate(id, updateUserDto, { new: true }).exec(); 
  }

  async remove(id: string): Promise<User> {
    return this.userModel.findByIdAndDelete(id).exec(); 
  }

}
