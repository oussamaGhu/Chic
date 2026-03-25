import { Controller, Post, Body } from '@nestjs/common';
import { FirebaseService } from './firebase.service';
import { CreateFirebaseDto } from './dto/create-firebase.dto';

@Controller('firebase')
export class FirebaseController {
  constructor(private readonly firebaseService: FirebaseService) {}
  @Post()
  sendNotification(@Body() pushNotification: CreateFirebaseDto) {
    this.firebaseService.sendPush(pushNotification);
  }
  @Post('send-notification')
  async sendNotifications(@Body() body: { token: string; message: string }) {
    const { token, message } = body;
    return this.firebaseService.sendPushNotification(token, message);
  }
}