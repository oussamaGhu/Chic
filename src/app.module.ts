import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { UserModule } from './user/user.module';
import { JwtModule } from '@nestjs/jwt';
import { MongooseModule } from '@nestjs/mongoose';
import { UserController } from './user/user.controller';
import { AuthModule } from './auth/auth.module';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { ClothesModule } from './clothes/clothes.module';
import { AssembleModule } from './assemble/assemble.module';
import { GeminiModule } from './gemini/gemini.module';
import { FilesModule } from './files/files.module';


import config from './Config/config';
import { ServeStaticModule } from '@nestjs/serve-static';
import { join } from 'path';
import { RequestModule } from './request/request.module';
import { FirebaseModule } from './firebase/firebase.module';

@Module({
  imports: [
    ConfigModule.forRoot({
      envFilePath: '.env',
      isGlobal: true,
      cache: true,
      load: [config],
    }),
    JwtModule.registerAsync({
      imports: [ConfigModule],
      useFactory: async (config) => ({
        secret: config.get('SECRET'),
        signOptions: { expiresIn: '60s' },
      }),
      global: true,
      inject: [ConfigService],
    }),
    MongooseModule.forRootAsync({
      imports: [ConfigModule],
      useFactory: async (config) => ({
        uri: config.get('database.connectionString'),
      }),
      inject: [ConfigService],
    }),
    ServeStaticModule.forRoot({
      rootPath: join(__dirname, '..', 'uploads'), // Indiquer où sont stockés les fichiers
      serveRoot: '/uploads', // Expose le dossier uploads sous l'URL /uploads
    }),
   
    AuthModule,
   ClothesModule,
   AssembleModule,
   GeminiModule,
   FilesModule,
   RequestModule,
   FirebaseModule,
 
  
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
