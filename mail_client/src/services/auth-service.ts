import {Injectable, inject} from '@angular/core';
import {UserService} from './user-service';

@Injectable({providedIn: 'root'})
export class AuthService {
  private _isAuthenticated = false;
  private userService = inject(UserService);

  public get isAuthenticated(): boolean {
    return this._isAuthenticated;
  }

  public getToken(): string | null {
    return this._isAuthenticated ? 'mock-token' : null;
  }

  public login(userId: string): void {
    const observable = this.userService.findOne(userId);
    observable.subscribe({
      next: (user) => {
        console.log('User logged in:', user);
        this._isAuthenticated = true;
      },
      error: (err) => {
        console.error('Error fetching user:', err);
        this._isAuthenticated = false;
      },
      complete: () => console.log('Completed')
    });
  }

  public logout(): void {
    this._isAuthenticated = false;
  }

  
}
