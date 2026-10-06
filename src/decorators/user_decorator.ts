import { createParamDecorator, ExecutionContext } from '@nestjs/common';
import { User } from 'src/user/schema/usersSchema';
 // Assure-toi d'importer ton modèle User

export const GetUser = createParamDecorator(
  (data: unknown, ctx: ExecutionContext): User => {
    const request = ctx.switchToHttp().getRequest();
    return request.user; // L'utilisateur est ajouté dans `request.user` par le Guard
  },
);
