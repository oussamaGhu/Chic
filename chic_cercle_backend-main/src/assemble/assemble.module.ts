import { Module } from '@nestjs/common';
import { AssembleService } from './assemble.service';
import { AssembleController } from './assemble.controller';
import { Assemble, AssembleSchema } from 'src/user/schema/assemble.schema';
import { MongooseModule } from '@nestjs/mongoose';
import { ClothesModule } from 'src/clothes/clothes.module';


@Module({
  imports: [MongooseModule.forFeature([{name :Assemble.name,schema:AssembleSchema}])
],
controllers: [AssembleController],
providers: [AssembleService],
exports:[MongooseModule,AssembleService]
})

export class AssembleModule {}
