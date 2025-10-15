import { Module } from '@nestjs/common';
import { UserService } from './user.service';
import { UserController } from './user.controller';
import { MongooseModule } from '@nestjs/mongoose';
import { User, UserSchema } from './schema/usersSchema';
import { FilesModule } from 'src/files/files.module';


@Module({
  imports : [MongooseModule.forFeature([{name :User.name,schema:UserSchema}]), 
  FilesModule,
],

  controllers: [UserController],
  providers: [UserService],
  exports : [MongooseModule,UserService] 
})
export class UserModule {}
