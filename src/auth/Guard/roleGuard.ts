import { Injectable, CanActivate, ExecutionContext, ForbiddenException } from '@nestjs/common';

@Injectable()
export class RoleGuard implements CanActivate {
  constructor() {}

  canActivate(
    context: ExecutionContext,
  ): boolean {
    const request = context.switchToHttp().getRequest();
    const user = request.user; // L'utilisateur est déjà attaché à la requête par AuthenticationGuard

    // Vérifie si l'utilisateur a le rôle requis
    if (!user || !user.role || !this.hasRole(user.role)) {
      throw new ForbiddenException('Access denied: Insufficient role');
    }

    return true;
  }

  // Vérification du rôle (tu peux modifier cette logique en fonction des rôles possibles)
  private hasRole(role: string): boolean {
    const allowedRoles = ['SELLER', 'CLIENT']; // Exemple de rôles autorisés
    return allowedRoles.includes(role);
  }
}
