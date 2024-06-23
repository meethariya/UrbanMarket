import {
  AfterViewInit,
  Component,
  ElementRef,
  Renderer2,
  ViewChild,
  inject,
} from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { SlickCarouselComponent } from 'ngx-slick-carousel';
import { HomeService } from '../../service/home.service';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { RequestUserDto } from '../../../../models/RequestUserDto';
import { Address } from '../../../../models/Address';
import { HttpResponse } from '@angular/common/http';
import { JwtTokenDto } from '../../../../models/JwtTokenDto';
import { ToastService } from '../../../../services/toast.service';
import { Toast } from '../../../../models/Toast';
import { Router } from '@angular/router';
import { UtilService } from '../../../../services/util.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent implements AfterViewInit {
  toastService = inject(ToastService);

  @ViewChild('slickModal', { static: true })
  slickModal!: SlickCarouselComponent; // slick model to swipe slides
  @ViewChild('otpCol') otpCol!: ElementRef<HTMLDivElement>; // Col containing the OTP input
  @ViewChild('otpButton') otpButton!: ElementRef<HTMLButtonElement>; // Send OTP button

  maxDate: NgbDateStruct; // Max Date for DOB
  minDate: NgbDateStruct; // Min Date for DOB

  progressValue: number = 33; // Default value for progressBar
  private _progressMessages: Array<string> = [
    'You are just 2 steps away from joining the hype',
    'Almost there...',
    'Lets begin!',
  ]; // Progress bar messages
  progressMessage: string = this._progressMessages[0]; // Progress bar currentMessage
  private otpValue: string | null = null; // received OTP value

  // Slick slide configuration
  slideConfig = {
    slidesToShow: 1,
    slidesToScroll: 1,
    arrows: false,
    dots: false,
    infinite: false,
    autoplay: false,
    pauseOnHover: false,
    swipe: false,
    zindex: 0,
  };

  // Registration form
  registerForm = new FormGroup({
    firstName: new FormControl('', [Validators.required]),
    lastName: new FormControl('', [Validators.required]),
    dob: new FormControl({ year: 2000, month: 1, day: 1 }, [
      Validators.required,
    ]),
    phone: new FormControl('', [
      Validators.required,
      Validators.pattern('[6-9][0-9]{9}'),
    ]),
    gender: new FormControl('m'),
    addressType: new FormControl('', [Validators.required]),
    houseNo: new FormControl('', [
      Validators.required,
      Validators.maxLength(30),
    ]),
    addressLine1: new FormControl('', [
      Validators.required,
      Validators.maxLength(50),
    ]),
    addressLine2: new FormControl('', [Validators.maxLength(50)]),
    city: new FormControl('', [Validators.required, Validators.maxLength(30)]),
    state: new FormControl('', [Validators.required, Validators.maxLength(30)]),
    pincode: new FormControl('', [
      Validators.required,
      Validators.pattern('[1-9][0-9]{5}'),
    ]),
    email: new FormControl('', [
      Validators.required,
      Validators.email,
      Validators.maxLength(50),
    ]),
    otp: new FormControl('', [
      Validators.required,
      Validators.pattern('[1-9][0-9]{5}'),
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
    ]),
    termConditions: new FormControl(false, [Validators.requiredTrue])
  });

  //Toast svgs
  dangerSvg: string;
  successSvg: string;

  /**
   * Constructor
   * @param renderer page renderer
   * @param service homeService
   */
  constructor(private renderer: Renderer2, private service: HomeService, private router: Router, private utilService:UtilService) {
    // set min max date for DOB
    const today = new Date();
    const maxDate = new Date(
      today.getFullYear() - 18,
      today.getMonth(),
      today.getDate()
    );
    const minDate = new Date(
      today.getFullYear() - 100,
      today.getMonth(),
      today.getDate()
    );
    this.maxDate = {
      year: maxDate.getFullYear(),
      month: maxDate.getMonth() + 1, // NgbDateStruct months are 1-based
      day: maxDate.getDate(),
    };
    this.minDate = {
      year: minDate.getFullYear(),
      month: minDate.getMonth() + 1, // NgbDateStruct months are 1-based
      day: minDate.getDate(),
    };

    this.dangerSvg = `
                    <svg width="16px" height="16px" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                      <circle cx="12" cy="17" r="1" fill="#000000"/>
                      <path d="M12 10L12 14" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                      <path d="M3.44722 18.1056L10.2111 4.57771C10.9482 3.10361 13.0518 3.10362 13.7889 4.57771L20.5528 18.1056C21.2177 19.4354 20.2507 21 18.7639 21H5.23607C3.7493 21 2.78231 19.4354 3.44722 18.1056Z" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                    </svg>
    `;
    this.successSvg = `
                    <svg fill="#000000" xmlns="http://www.w3.org/2000/svg" 
                      width="16px" height="16px" viewBox="0 0 52 52" enable-background="new 0 0 52 52" xml:space="preserve">
                      <path d="M26,2C12.7,2,2,12.7,2,26s10.7,24,24,24s24-10.7,24-24S39.3,2,26,2z M39.4,20L24.1,35.5
                        c-0.6,0.6-1.6,0.6-2.2,0L13.5,27c-0.6-0.6-0.6-1.6,0-2.2l2.2-2.2c0.6-0.6,1.6-0.6,2.2,0l4.4,4.5c0.4,0.4,1.1,0.4,1.5,0L35,15.5
                        c0.6-0.6,1.6-0.6,2.2,0l2.2,2.2C40.1,18.3,40.1,19.3,39.4,20z"/>
                    </svg>
    `;
  }
  /**
   * After initialization callback
   */
  ngAfterViewInit(): void {
    // Hide OTP input on load
    this.renderer.setStyle(this.otpCol.nativeElement, 'display', 'none');
    // On OTP input value change, validate OTP
    this.registerForm.get('otp')?.valueChanges.subscribe((value) => {
      this.onOtpValue(value);
    });
  }

  /**
   * Validate OTP input on each value change.
   * @param value user input OTP
   */
  private onOtpValue(value: string | null) {
    value = String(value); // IDK why but is required
    // If conditions fail, show error message
    if (
      this.otpValue != null &&
      value != null &&
      (value.length > this.otpValue.length ||
        (value.length == this.otpValue?.length && this.otpValue != value))
    ) {
      alert('Incorrect OTP');
    } else if (
      this.otpValue != null &&
      value != null &&
      this.otpValue.length == value.length &&
      this.otpValue === value
    ) {
      // If conditions pass, approve OTP and disable email, otp inputs
      this.renderer.setProperty(
        this.otpButton.nativeElement.parentElement,
        'hidden',
        true
      );
      setTimeout(() => {
        this.registerForm.get('email')?.disable();
        this.registerForm.get('otp')?.disable();
      }, 100);
    }
  }

  /**
   * Validate current slide fields before going to next slide
   * @param slideNo active slide
   */
  goToNextSlide(slideNo: string) {
    // slide 1 constraints
    const firstSlideResult: boolean =
      this.f.firstName.valid &&
      this.f.lastName.valid &&
      this.f.dob.valid &&
      this.f.phone.valid;
    // slide 2 constraints
    const secondSlideResult: boolean =
      this.f.addressType.valid &&
      this.f.houseNo.valid &&
      this.f.addressLine1.valid &&
      this.f.city.valid &&
      this.f.state.valid &&
      this.f.pincode.valid;
    // slide 3 contraints
    const thirdSlideResult: boolean =
      this.f.email.valid && this.f.password.valid && this.f.otp.valid;
    if (
      (slideNo === 'slide1' && firstSlideResult) ||
      (slideNo === 'slide2' && secondSlideResult) ||
      (slideNo === 'slide3' && thirdSlideResult)
    ) {
      this.slickModal.slickNext();
    } else {
      const toastConfig: Toast = {
        header: 'Incomplete fields',
        body: 'Please fill all the required fields',
        classname: 'text-bg-danger',
        delay: 5000,
        headerSvg: this.dangerSvg,
      };
      this.toastService.show(toastConfig);
    }
  }

  /**
   * Before slide change callback
   * @param e slide change event
   */
  beforeChange(e: any) {
    this.upadateProgressBar(e.nextSlide);
    this.progressMessage = this._progressMessages[e.nextSlide];
  }

  /**
   * Change progress bar status based on slide change
   * @param nextSlide slide number
   */
  upadateProgressBar(nextSlide: number) {
    this.progressValue = ((nextSlide + 1) * 100) / 3;
  }

  /**
   * On 'send otp' button click.
   * Disable button for 2mins, send request to fetch OTP. Show the OTP input field.
   */
  sendOtp() {
    this.renderer.setProperty(this.otpButton.nativeElement, 'disabled', true);
    const firstName: string = this.registerForm.value.firstName
      ? this.registerForm.value.firstName
      : '';
    const lastName: string = this.registerForm.value.lastName
      ? this.registerForm.value.lastName
      : '';
    const email: string = this.registerForm.value.email
      ? this.registerForm.value.email
      : '';

    // Start countdown of 2 mins to resend OTP request.
    var countdown = 120;
    var interval = setInterval(() => {
      this.renderer.setProperty(
        this.otpButton.nativeElement,
        'innerHTML',
        'Wait for ' + countdown-- + 's'
      );
      if (countdown === 0) {
        this.renderer.setProperty(
          this.otpButton.nativeElement,
          'disabled',
          false
        );
        this.renderer.setProperty(
          this.otpButton.nativeElement,
          'innerHTML',
          'Resend OTP'
        );
        clearInterval(interval);
      }
    }, 1000);

    // send request to receive OTP
    this.service
      .requestOtp({ email: email, name: firstName + ' ' + lastName })
      .subscribe({
        next: (otp) => {
          const toastConfig: Toast = {
            header: 'Otp sent',
            body: 'An OTP has been sent to you mail address. Kindly verify account to proceed',
            classname: 'text-bg-success',
            delay: 5000,
            headerSvg: this.successSvg,
          };
          this.toastService.show(toastConfig);
          this.otpValue = String(otp);
          this.renderer.setStyle(this.otpCol.nativeElement, 'display', 'block');
        },
        error: (err) => {
          this.showErrorToast(err);
        },
      });
  }

  /**
   * Validate form again and send request to create new user.
   */
  register() {
    this.registerForm.get('email')?.enable();
    if (
      this.registerForm.value.addressType != null &&
      this.registerForm.value.houseNo != null &&
      this.registerForm.value.addressLine1 != null &&
      this.registerForm.value.city != null &&
      this.registerForm.value.state != null &&
      this.registerForm.value.pincode != null &&
      this.registerForm.value.firstName != null &&
      this.registerForm.value.lastName != null &&
      this.registerForm.value.email != null &&
      this.registerForm.value.password != null &&
      this.registerForm.value.phone != null &&
      this.registerForm.value.gender != null &&
      this.registerForm.value.dob != null
    ) {
      // form the address json
      var address: Address = {
        houseNo: this.registerForm.value.houseNo,
        addressLine1: this.registerForm.value.addressLine1,
        addressLine2: this.registerForm.value.addressLine2,
        city: this.registerForm.value.city,
        state: this.registerForm.value.state,
        pincode: Number(this.registerForm.value.pincode),
      };
      const addressMap = new Map<string, Address>([
        [this.registerForm.value.addressType, address],
      ]);
      const convMap: Record<string, Address> = {};
      addressMap.forEach((val: Address, key: string) => {
        convMap[key] = val;
      });

      var ngbDate = this.registerForm.value.dob;
      var formData: RequestUserDto = {
        name:
          this.registerForm.value.firstName +
          ' ' +
          this.registerForm.value.lastName,
        email: this.registerForm.value.email,
        password: this.registerForm.value.password,
        role: 'CUSTOMER',
        dob: new Date(ngbDate.year, ngbDate.month - 1, ngbDate.day),
        phone: this.registerForm.value.phone,
        gender: this.registerForm.value.gender,
        profilePicPath: '',
        address: convMap,
      };
      // send registrations request
      this.service.registerUser(formData).subscribe({
        next: (customerId: number) => {
          // send login request to generate token
          const basicToken = window.btoa(
            formData.email + ':' + formData.password
          );
          this.utilService.login(basicToken).subscribe({
            next: (response: HttpResponse<JwtTokenDto>) => {
              const status = response.status;
              const token = response.body?.token;
              if (status === 201 && token) {
                this.router.navigate(['/']);
                // this.utilService.setToken(token);
                const toastConfig: Toast = {
                  header: 'Registered',
                  body: 'You have successfully registered to our service. Login to start shopping now!',
                  classname: 'text-bg-success',
                  delay: 5000,
                  headerSvg: this.successSvg,
                };
                this.toastService.show(toastConfig);
                // send welcome email
                this.service.welcomeUser(customerId, token).subscribe({
                  next: (nothing: void) => {},
                  error: (err) => {
                    this.showErrorToast(err);
                  },
                });
              } else {
                const toastConfig: Toast = {
                  header: 'Unknown Error',
                  body: 'An unknown error occurred. Please try again',
                  classname: 'text-bg-danger',
                  delay: 5000,
                  headerSvg: this.dangerSvg,
                };
                this.toastService.show(toastConfig);
              }
            },
            error: (err) => {
              this.showErrorToast(err);
            },
          });
        },
        error: (err) => {
          this.showErrorToast(err);
        },
      });
    } else {
      const toastConfig: Toast = {
        header: 'Incomplete fields',
        body: 'Please fill all the required fields',
        classname: 'text-bg-danger',
        delay: 5000,
        headerSvg: this.dangerSvg,
      };
      this.toastService.show(toastConfig);
    }
  }

  /**
   * @returns form control
   */
  get f() {
    return this.registerForm.controls;
  }

  showErrorToast(err: any) {
    const toastConfig: Toast = {
      header: err.error.title,
      body: err.error.message,
      classname: 'text-bg-danger',
      delay: 5000,
      headerSvg: this.dangerSvg,
    };
    this.toastService.show(toastConfig);
  }
}
