import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map, Observable, of } from 'rxjs';
import { User } from '../models/User';
import { Environment } from '../environment';
import { UserResponse } from '../dtos/UserResponse';
import { UserCreate } from '../dtos/UserCreate';
import { UserUpdate } from '../dtos/UserUpdate';
import { UserPasswordUpdate } from '../dtos/UserPasswordUpdate';
import { UserListResponse } from '../dtos/UserListResponse';
import { UserPageResponse } from '../dtos/UserPageResponse';

export type { UserListResponse } from '../dtos/UserListResponse';
export type { UserPageResponse } from '../dtos/UserPageResponse';

/**
 * Service for managing user operations including fetching, creation, updating, and deletion.
 */
@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly httpClient: HttpClient = inject(HttpClient);
  private readonly apiBasePath: string = Environment.apiBasePath;

  /**
   * Fetches all users and transforms them into an email-to-fullName map Observable.
   *
   * @returns Observable emitting a Map connecting user emails to full display names.
   */
  public getUserMap(): Observable<Map<string, string>> {
    return this.getAllUsers().pipe(
      map((users) => {
        const map = new Map<string, string>();
        users.forEach((u) => {
          const fullName =
            `${u.firstName || ''} ${u.lastName || ''}`.trim() || u.firstName || u.email;
          if (u.email) {
            map.set(u.email.toLowerCase().trim(), fullName);
          }
        });
        return map;
      }),
      catchError(() => of(new Map<string, string>())),
    );
  }

  /**
   * Fetches a paginated list of users, with an optional search query.
   *
   * @param search - Optional search string to filter users.
   * @param page - Page number index (default: 0).
   * @param size - Number of records per page (default: 100).
   * @returns Observable emitting an array of UserListResponse.
   */
  public getAllUsers(search?: string, page = 0, size = 100): Observable<UserListResponse[]> {
    const params: Record<string, string> = {
      page: page.toString(),
      size: size.toString(),
    };
    if (search && search.trim().length > 0) {
      params['search'] = search.trim();
    }

    return this.httpClient
      .get<UserPageResponse>(`${this.apiBasePath}/users`, { params })
      .pipe(map((res) => res.content || []));
  }

  /**
   * Fetches a single user by ID.
   *
   * @param id - Unique user ID.
   * @returns Observable emitting domain User instance.
   */
  public getById(id: string): Observable<User> {
    return this.httpClient.get<UserResponse>(`${this.apiBasePath}/users/${id}`).pipe(map(this.mapToUser));
  }

  /**
   * Creates a new user account.
   *
   * @param u - UserCreate DTO containing new user details.
   * @returns Observable emitting created domain User instance.
   */
  public create(u: UserCreate): Observable<User> {
    return this.httpClient
      .post<UserResponse>(`${this.apiBasePath}/users`, u)
      .pipe(map(this.mapToUser));
  }

  /**
   * Updates existing user profile fields (first name, last name, email).
   *
   * @param id - ID of the target user.
   * @param userUpdate - UserUpdate DTO containing updated profile fields.
   * @returns Observable emitting updated domain User instance.
   */
  public updateUser(id: string, userUpdate: UserUpdate): Observable<User> {
    return this.httpClient.put<UserResponse>(`${this.apiBasePath}/users/${id}`, userUpdate).pipe(map(this.mapToUser));
  }

  /**
   * Updates user password credentials.
   *
   * @param id - ID of the target user.
   * @param passwordUpdate - UserPasswordUpdate DTO containing old and new passwords.
   * @returns Observable emitting updated domain User instance.
   */
  public updatePassword(id: string, passwordUpdate: UserPasswordUpdate): Observable<User> {
    return this.httpClient.patch<UserResponse>(`${this.apiBasePath}/users/${id}/password`, passwordUpdate).pipe(map(this.mapToUser));
  }

  /**
   * Deletes a user account by ID.
   *
   * @param id - ID of the target user to delete.
   * @returns Observable completing on successful deletion.
   */
  public deleteUser(id: string): Observable<void> {
    return this.httpClient.delete<void>(`${this.apiBasePath}/users/${id}`);
  }
  /**
   * Maps a UserResponse object to a User model instance.
   *
   * @param res - UserResponse DTO.
   * @returns User model instance.
   */
  private mapToUser(res: UserResponse): User {
    return new User(res.userId, res.email, res.firstName, res.lastName);
  }
}

