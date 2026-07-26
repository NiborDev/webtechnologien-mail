import { Injectable, inject, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Environment } from '../environment';
import { LoginData } from '../dtos/LoginData';
import { LoginResponse } from '../dtos/LoginResponse';
import { UserResponse } from '../dtos/UserResponse';
import { Observable, tap } from 'rxjs';
import { User } from '../models/User';

function loadInitialUser(): UserResponse | null {
  const userJson = localStorage.getItem('user_data');
  if (!userJson) return null;
  try {
    return JSON.parse(userJson) as UserResponse;
  } catch {
    return null;
  }
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly _token = signal<string | null>(localStorage.getItem('jwt_token'));
  private readonly _currentUser = signal<UserResponse | null>(loadInitialUser());

  public readonly token = this._token.asReadonly();
  public readonly currentUser = this._currentUser.asReadonly();
  public readonly isAuthenticated = computed(() => !!this._token());

  private readonly httpClient = inject(HttpClient);
  private readonly apiBasePath = Environment.apiBasePath;

  public getToken(): string | null {
    return this._token();
  }

  public setCurrentUser(user: UserResponse): void {
    this._currentUser.set(user);
    localStorage.setItem('user_data', JSON.stringify(user));
  }

  public loginWithCredentials(loginData: LoginData): Observable<LoginResponse> {
    return this.httpClient.post<LoginResponse>(`${this.apiBasePath}/users/login`, loginData).pipe(
      tap((response) => {
        this._token.set(response.token);
        localStorage.setItem('jwt_token', response.token);
        this.setCurrentUser(response.user);
      }),
    );
  }

  public logout(): void {
    this._token.set(null);
    this._currentUser.set(null);
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('user_data');
  }

  public mapToUser(res: LoginResponse): User {
    return new User(res.user.userId, res.user.email, res.user.firstName, res.user.lastName);
  }
}
