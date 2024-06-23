import { HttpClient, HttpResponse } from '@angular/common/http';
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { environment } from '../../environments/environment';
import { JwtTokenDto } from '../models/JwtTokenDto';
import { Observable } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class UtilService {
  constructor(private http: HttpClient, @Inject(PLATFORM_ID) private platformId: Object) {}

  private backend: string = environment.backend;

  /**
   * Logs the user in by generating a JWT token.
   *
   * @param token The Basic token to be used for authentication.
   * @returns An Observable that emits a HttpResponse containing the generated JWT token.
   */
  login(token: string): Observable<HttpResponse<JwtTokenDto>> {
    return this.http.get<JwtTokenDto>(
      this.backend + '/api/authentication/generateToken',
      { headers: { Authorization: 'Basic ' + token }, observe: 'response' }
    );
  }

  /**
   * Retrieves the JWT token from the session storage.
   *
   * @returns {string | null} The JWT token stored in the session storage.
   * If no token is found, it returns null.
   */
  getToken(): string | null {
    // Iniitate sessionstorage only on browser load. not on SSR
    if(isPlatformBrowser(this.platformId)) {
      return sessionStorage.getItem('token');
    } else {
      return null;
    }
  }

  /**
   * Sets the JWT token in the session storage.
   *
   * @param token The JWT token to be stored in the session storage.
   */
  setToken(token: string) {
    if(isPlatformBrowser(this.platformId)) {
      sessionStorage.setItem('token', token);
    }
  }

  /**
   * Removes the JWT token from the session storage.
   */
  removeToken() {
    if(isPlatformBrowser(this.platformId)) {
      sessionStorage.removeItem('token');
    }
  }
}
