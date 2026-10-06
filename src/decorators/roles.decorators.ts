// role.decorator.ts
import { SetMetadata } from '@nestjs/common';
import { Role } from 'src/auth/Role.enums/Role.emun';


export const ROLES_KEY = 'roles';
export const Roles = (...roles: Role[]) => SetMetadata(ROLES_KEY, roles);
