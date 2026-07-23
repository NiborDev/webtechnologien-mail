import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map, Observable} from 'rxjs';
import {User} from '../models/User';
import {Environment} from '../environment';
import { UserResponse } from '../dtos/UserResponse';
import { UserCreate } from '../dtos/UserCreate';

@Injectable({providedIn: 'root'})
export class UserService {
  private readonly httpClient: HttpClient = inject(HttpClient);
  private readonly apiBasePath: string = Environment.apiBasePath;

  public findOne(id: string): Observable<User> {
    return this.httpClient
      .get<UserResponse>(`${this.apiBasePath}/users/${id}`)
      .pipe(map(this.mapToUser))
  }

  public create(u: UserCreate): Observable<User> {
    return this.httpClient
      .post<UserResponse>(`${this.apiBasePath}/users`, u)
      .pipe(map(this.mapToUser));
  }

  private mapToUser(res: UserResponse): User {
    return new User(
      res.id,
      res.email,
      res.firstName,
      res.lastName
    )
  }

}
