import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';
import { RequestUserDto } from '../../../models/RequestUserDto';
import { JwtTokenDto } from '../../../models/JwtTokenDto';

@Injectable({
  providedIn: 'root',
})
export class HomeService {
  constructor(private http: HttpClient) {}

  private backend: string = environment.backend;

  /**
   * Sends request to backend to send OTP.
   * @request GET
   * @param params email and name of user
   * @returns OTP
   */
  requestOtp(params: { email: string; name: string }): Observable<number> {
    return this.http.get<number>(this.backend + '/api/email/verify-email', {
      params: params,
    });
  }

  /**
   * Sends request to backend to save user
   * @request POST
   * @param customerDto user details
   * @returns customerId
   */
  registerUser(customerDto: RequestUserDto): Observable<number> {
    return this.http.post<number>(this.backend + '/api/user/create', customerDto);
  }

  /**
   * Send request to send welcome mail to user
   * @request GET
   * @param customerId id of customer to whom email has to be sent
   * @returns void
   */
  welcomeUser(customerId: number, token: string): Observable<void> {
    return this.http.get<void>(this.backend + '/api/email/welcome/'+customerId,{ headers: { Authorization: 'Bearer ' + token }});
  }

}
