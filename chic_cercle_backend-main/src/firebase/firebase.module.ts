import { Module } from '@nestjs/common';
import { FirebaseService } from './firebase.service';
import { FirebaseController } from './firebase.controller';
import { firebaseAdminProvider } from './firebase-admin.provider';

@Module({
  controllers: [FirebaseController],
  providers: [firebaseAdminProvider, FirebaseService],
})
export class FirebaseModule {}
