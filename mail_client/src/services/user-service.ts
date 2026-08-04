import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { User } from '../models/User';
import { Environment } from '../environment';
import { UserResponse } from '../dtos/UserResponse';
import { UserCreate } from '../dtos/UserCreate';
import { UserUpdate } from '../dtos/UserUpdate';
import { UserPasswordUpdate } from '../dtos/UserPasswordUpdate';
import { UserListResponseDto } from '../dtos/user-list-response.dto';
import { UserPageResponseDto } from '../dtos/user-page-response.dto';

export type { UserListResponseDto } from '../dtos/user-list-response.dto';
export type { UserPageResponseDto } from '../dtos/user-page-response.dto';

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly httpClient: HttpClient = inject(HttpClient);
  private readonly apiBasePath: string = Environment.apiBasePath;

  public getAllUsers(search?: string, page = 0, size = 100): Observable<UserListResponseDto[]> {
    const params: Record<string, string> = {
      page: page.toString(),
      size: size.toString(),
    };
    if (search && search.trim().length > 0) {
      params['search'] = search.trim();
    }

    return this.httpClient
      .get<UserPageResponseDto>(`${this.apiBasePath}/users`, { params })
      .pipe(map((res) => res.content || []));
  }

  public findOne(id: string): Observable<User> {
    return this.httpClient
      .get<UserResponse>(`${this.apiBasePath}/users/${id}`)
      .pipe(map(this.mapToUser));
  }

  public getById(id: string): Observable<User> {
    return this.httpClient.get<UserResponse>(`${this.apiBasePath}/users/${id}`).pipe(map(this.mapToUser));
  }

  public create(u: UserCreate): Observable<User> {
    return this.httpClient
      .post<UserResponse>(`${this.apiBasePath}/users`, u)
      .pipe(map(this.mapToUser));
  }

  public updateUser(id: string, userUpdate: UserUpdate): Observable<User> {
    return this.httpClient.put<UserResponse>(`${this.apiBasePath}/users/${id}`, userUpdate).pipe(map(this.mapToUser));
  }

  public updatePassword(id: string, passwordUpdate: UserPasswordUpdate): Observable<User> {
    return this.httpClient.patch<UserResponse>(`${this.apiBasePath}/users/${id}/password`, passwordUpdate).pipe(map(this.mapToUser));
  }

  public deleteUser(id: string): Observable<void> {
    return this.httpClient.delete<void>(`${this.apiBasePath}/users/${id}`);
  }

  private mapToUser(res: UserResponse): User {
    return new User(res.userId, res.email, res.firstName, res.lastName);
  }
}
