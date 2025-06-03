import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs'; // ✅ Agregar throwError
import { catchError } from 'rxjs/operators'; // ✅ Agregar catchError
import { UserStorageService } from 'src/app/services/storage/user-storage.service';

const BASIC_URL = 'http://localhost:8080/';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  constructor(private http: HttpClient) {}

  addCategory(categoryDto: any): Observable<any> {
    console.log('Sending category:', categoryDto); // Debug
    console.log('Token:', UserStorageService.getToken()); // Debug

    return this.http
      .post(BASIC_URL + 'api/admin/category', categoryDto, {
        headers: this.createAuthorizationHeader(),
      })
      .pipe(
        catchError((error) => {
          console.error('Service error:', error);
          return throwError(() => error);
        })
      );
  }

  private createAuthorizationHeader(): HttpHeaders {
    const token = UserStorageService.getToken();
    return new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: 'Bearer ' + token,
    });
  }
}
