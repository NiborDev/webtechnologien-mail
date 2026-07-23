import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Environment } from '../environment';
import { LoginData } from '../dtos/LoginData';
import { LoginResponse } from '../dtos/LoginResponse';
import { Observable, tap } from 'rxjs';
import { User } from '../models/User';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private _isAuthenticated = false;
  private _token: string | null = null;
  private readonly httpClient = inject(HttpClient);
  private readonly apiBasePath = Environment.apiBasePath;

  constructor() {
    const token = localStorage.getItem('jwt_token');
    if (token) {
      this._token = token;
      this._isAuthenticated = true;
    }
  }

  public get isAuthenticated(): boolean {
    return this._isAuthenticated;
  }

  public getToken(): string | null {
    return this._token;
  }

  public loginWithCredentials(loginData: LoginData): Observable<LoginResponse> {
    return this.httpClient.post<LoginResponse>(`${this.apiBasePath}/users/login`, loginData).pipe(
      tap((response) => {
        this._isAuthenticated = true;
        this._token = response.token;
        localStorage.setItem('jwt_token', response.token);
      }),
    );
  }

  public logout(): void {
    this._isAuthenticated = false;
    this._token = null;
    localStorage.removeItem('jwt_token');
  }

  public mapToUser(res: LoginResponse): User {
    return new User(res.user.id, res.user.email, res.user.firstName, res.user.lastName);
  }
}
