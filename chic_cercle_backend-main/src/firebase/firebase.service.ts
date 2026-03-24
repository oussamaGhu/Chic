import { Injectable } from '@nestjs/common';
import * as admin from 'firebase-admin';
import * as path from 'path';
import * as fs from 'fs';
import { CreateFirebaseDto } from './dto/create-firebase.dto';

@Injectable()
export class FirebaseService {
  private static isInitialized = false; // Prevent multiple initializations

  constructor() {
    if (!FirebaseService.isInitialized) {
      try {
        const serviceAccountPath = path.resolve(process.cwd(), 'config/firebase/google-service.json');
        
        // Read the file content using fs
        fs.readFile(serviceAccountPath, 'utf8', (err, data) => {
          if (err) {
            console.error('Error reading service account file:', err);
            return;
          }

          try {
            // Parse the JSON content
            const serviceAccount = JSON.parse(data);

            // Log the service account content (for verification)
            console.log('Service Account File Loaded:', serviceAccount);

            // Initialize Firebase Admin SDK with service account credentials
         

            FirebaseService.isInitialized = true;
            console.log('Firebase Admin SDK initialized successfully');
          } catch (parseError) {
            console.error('Error parsing service account file:', parseError);
          }
        });
      } catch (error) {
        console.error('Error initializing Firebase Admin SDK:', error);
      }
    }
  }
  async sendPush(notification: CreateFirebaseDto) {
    try {
      await admin
        .messaging()
        .send({
          notification: {
            title: notification.title,
            body: notification.body,
          },
          token: notification.deviceId,
          data: {},
          android: {
            priority: 'high',
            notification: {
              sound: 'default',
              channelId: 'default',
            },
          },
          apns: {
            headers: {
              'apns-priority': '10',
            },
            payload: {
              aps: {
                contentAvailable: true,
                sound: 'default',
              },
            },
          },
        })
        .catch((error: any) => {
          console.error(error);
        });
    } catch (error) {
      console.log(error);
      return error;
    }
  }

  async sendPushNotification(token: string, message: string) {
    const messagePayload = {
      notification: {
        title: 'New Notification',
        body: message,
      },
      token: token, // Device token
    };

    try {
      const response = await admin.messaging().send(messagePayload);
      console.log('Successfully sent message:', response);
      return response;
    } catch (error) {
      console.error('Error sending message:', error);
      throw error;
    }
  }
}