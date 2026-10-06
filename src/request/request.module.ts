import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { RequestController } from './request.controller';
import { RequestService } from './request.service';
import { Request, RequestSchema } from 'src/user/schema/request.schema'; 
import { ClothesModule } from 'src/clothes/clothes.module';
import { AssembleModule } from 'src/assemble/assemble.module';

@Module({
  imports: [
    MongooseModule.forFeature([{ name: Request.name, schema: RequestSchema }]), ClothesModule , AssembleModule
  ],
  controllers: [RequestController],
  providers: [RequestService],
  exports: [MongooseModule,RequestService],  // If you want to export the service to use in other modules
})
export class RequestModule {}