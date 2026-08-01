import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { User } from '../models/User';
import { Environment } from '../environment';
import { UserResponse } from '../dtos/UserResponse';
import { UserCreate } from '../dtos/UserCreate';
import { UserUpdate } from '../dtos/UserUpdate';
import { UserPasswordUpdate } from '../dtos/UserPasswordUpdate';

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly httpClient: HttpClient = inject(HttpClient);
  private readonly apiBasePath: string = Environment.apiBasePath;

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
