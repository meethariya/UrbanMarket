import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output, inject } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { NgbActiveOffcanvas } from '@ng-bootstrap/ng-bootstrap';
import { UtilService } from '../../services/util.service';
import { HttpResponse } from '@angular/common/http';
import { JwtTokenDto } from '../../models/JwtTokenDto';
import { ToastService } from '../../services/toast.service';
import { Toast } from '../../models/Toast';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterModule, ReactiveFormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  activeOffcanvas = inject(NgbActiveOffcanvas);
  toastService = inject(ToastService);
  @Output() isLoginEmitter : EventEmitter<Boolean> = new EventEmitter();
  constructor(private service: UtilService, private router: Router) {}

  // Login form
  loginForm = new FormGroup({
    email: new FormControl('', [
      Validators.required,
      Validators.email,
      Validators.maxLength(50),
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
    ]),
  });

  /**
   * Method to handle login form submission.
   * It constructs a basic token from the email and password,
   * then attempts to login using the `UtilService`.
   * If successful, it navigates to the home page, sets the token,
   * and shows a success toast. If unsuccessful, it shows an error toast.
   * @param none
   * @returns none
   */
  login() {
    const basicToken = window.btoa(
      this.loginForm.value.email + ':' + this.loginForm.value.password
    );
    if (this.service.getToken() == null) {
      this.service.login(basicToken).subscribe({
        next: (response: HttpResponse<JwtTokenDto>) => {
          const status = response.status;
          const token = response.body?.token;
          // login success
          if (status === 201 && token) {
            this.router.navigate(['/']);
            // set token
            this.service.setToken(token);
            this.isLoginEmitter.emit(true);
            const toastConfig: Toast = {
              header: 'Success',
              body: 'You have successfully signed in. Happy shopping',
              classname: 'text-bg-success',
              delay: 5000,
              headerSvg: this.toastService.successSvg,
            };
            this.toastService.show(toastConfig);
            this.activeOffcanvas.dismiss('Successs login');
          } else {
            const toastConfig: Toast = {
              header: 'Invalid credentials',
              body: 'Kindly recheck your credentials and try again.',
              classname: 'text-bg-danger',
              delay: 5000,
              headerSvg: this.toastService.dangerSvg,
            };
            this.toastService.show(toastConfig);
          }
        },
        error: (error) => {
          if (error.status === 401) {
            // unauthorized login
            const toastConfig: Toast = {
              header: 'Invalid credentials',
              body: 'Kindly recheck your credentials and try again.',
              classname: 'text-bg-danger',
              delay: 5000,
              headerSvg: this.toastService.dangerSvg,
            };
            this.toastService.show(toastConfig);
          } else {
            this.toastService.showErrorToast(error);
          }
        },
      });
    }
  }

  /**
   * @returns form control
   */
  get f() {
    return this.loginForm.controls;
  }
}
