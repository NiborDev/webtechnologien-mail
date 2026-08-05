import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth-service';

/**
 * Functional HTTP interceptor that attaches the Bearer JWT token to outgoing HTTP request headers.
 *
 * @param req - The outgoing HttpRequest object.
 * @param next - The HttpHandlerFn pipeline handler.
 * @returns An Observable emitting the HttpEvent.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const authToken = authService.getToken();
  if (!authToken) {
    return next(req);
  }
  const authenticatedRequest = req.clone({
    headers: req.headers.set('Authorization', `Bearer ${authToken}`),
  });
  return next(authenticatedRequest);
};

