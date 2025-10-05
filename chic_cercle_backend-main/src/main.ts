import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { setupSwagger } from './Config/swagger.config';
import * as cors from 'cors';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  // Appeler la fonction pour configurer Swagger
  setupSwagger(app);

    // Enable CORS
    app.enableCors({
      origin: '*', // Replace with your Flutter web app's URL
      methods: 'GET,HEAD,PUT,PATCH,POST,DELETE',
      credentials: true,
    });
  
  await app.listen(process.env.PORT ?? 3000);
}
bootstrap();
