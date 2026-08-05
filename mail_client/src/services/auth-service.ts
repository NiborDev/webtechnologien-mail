import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Environment } from '../environment';
import { LoginData } from '../dtos/LoginData';
import { LoginResponse } from '../dtos/LoginResponse';
import { UserResponse } from '../dtos/UserResponse';
import { Observable, tap } from 'rxjs';
import { User } from '../models/User';

/**
 * Loads stored user metadata from browser local storage if present.
 *
 * @returns UserResponse object parsed or null if absent or invalid.
 */
function loadInitialUser(): UserResponse | null {
  const userJson = localStorage.getItem('user_data');
  if (!userJson) return null;
  try {
    return JSON.parse(userJson) as UserResponse;
  } catch {
    return null;
  }
}

/**
 * Service managing user authentication state, JWT token storage, and login/logout procedures.
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly _token = signal<string | null>(localStorage.getItem('jwt_token'));
  private readonly _currentUser = signal<UserResponse | null>(loadInitialUser());

  /** Readonly signal holding the active JWT token string or null. */
  public readonly token = this._token.asReadonly();
  /** Readonly signal holding the currently authenticated user details or null. */
  public readonly currentUser = this._currentUser.asReadonly();

  /**
   * Returns whether a user is currently authenticated with a token.
   *
   * @returns True if a valid token exists, false otherwise.
   */
  public get isAuthenticated(): boolean {
    return !!this._token();
  }

  private readonly httpClient = inject(HttpClient);
  private readonly apiBasePath = Environment.apiBasePath;

  /**
   * Retrieves the current JWT token string.
   *
   * @returns The active JWT token or null.
   */
  public getToken(): string | null {
    return this._token();
  }

  /**
   * Updates the current user state in memory and persists to local storage.
   *
   * @param user - UserResponse object to set as active user.
   */
  public setCurrentUser(user: UserResponse): void {
    this._currentUser.set(user);
    localStorage.setItem('user_data', JSON.stringify(user));
  }

  /**
   * Authenticates user using email and password credentials.
   *
   * @param loginData - Login credentials (email and password).
   * @returns Observable emitting LoginResponse containing JWT token and user info.
   */
  public loginWithCredentials(loginData: LoginData): Observable<LoginResponse> {
    return this.httpClient.post<LoginResponse>(`${this.apiBasePath}/users/login`, loginData).pipe(
      tap((response) => {
        this._token.set(response.token);
        localStorage.setItem('jwt_token', response.token);
        this.setCurrentUser(response.user);
      }),
    );
  }

  /**
   * Clears active authentication tokens and stored user data, logging the user out.
   */
  public logout(): void {
    this._token.set(null);
    this._currentUser.set(null);
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('user_data');
  }

  /**
   * Maps a LoginResponse object to a User model instance.
   *
   * @param res - LoginResponse DTO from authentication.
   * @returns User model instance.
   */
  public mapToUser(res: LoginResponse): User {
    return new User(res.user.userId, res.user.email, res.user.firstName, res.user.lastName);
  }
}

