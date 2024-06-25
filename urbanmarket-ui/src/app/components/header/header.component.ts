import { CommonModule, DOCUMENT } from '@angular/common';
import {
  Component,
  Renderer2,
  ElementRef,
  HostListener,
  Inject,
  ViewChild,
  AfterViewInit,
  inject,
  OnInit,
} from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgbOffcanvas } from '@ng-bootstrap/ng-bootstrap';
import { LoginComponent } from '../login/login.component';
import { HelperService } from '../../services/helper.service';
import { ToastService } from '../../services/toast.service';
import { Toast } from '../../models/Toast';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent implements AfterViewInit, OnInit {
  @ViewChild('header') headerDiv!: ElementRef<HTMLDivElement>;
  private offcanvasService = inject(NgbOffcanvas);
  toastService = inject(ToastService);
  isLoggedIn!:boolean;

  constructor(
    private renderer: Renderer2,
    @Inject(DOCUMENT) private document: Document,
    private service: HelperService
  ) {}
  ngOnInit(): void {
    // set login status according to token availablity
    this.isLoggedIn = this.service.getToken()!=null;
  }

  ngAfterViewInit(): void {
    // Handle window load event
    this.updateBackgroundColor();
  }

  /**
   * On scroll event
   */
  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.updateBackgroundColor();
  }

  /**
   * Update the background color of the component
   */
  private updateBackgroundColor() {
    const div = this.headerDiv.nativeElement;
    const scrollPosition =
      this.document.documentElement.scrollTop ||
      this.document.body.scrollTop ||
      0;
    if (scrollPosition > 100) {
      this.renderer.addClass(div, 'scrolled');
      this.renderer.removeClass(div, 'not-scrolled');
    } else {
      this.renderer.addClass(div, 'not-scrolled');
      this.renderer.removeClass(div, 'scrolled');
    }
  }

  openEnd() {
		const modalRef = this.offcanvasService.open(LoginComponent,{ position: 'end', backdropClass: 'bg-secondary' });
    // set logggin in status from the login component event emitter
    modalRef.componentInstance.isLoginEmitter.subscribe((state:boolean)=>{
      this.isLoggedIn = state;
    });
	}
  /**
   * Logs the user out and sets the `isLoggedIn` flag to `false`.
   * Also removes the token from the service.
   */
  logout() {
    this.isLoggedIn=false;
    this.service.removeToken();
    const toastConfig: Toast = {
      header: 'Success',
      body: 'You have successfully signed out. Shop with us again!',
      classname: 'text-bg-success',
      headerSvg: this.toastService.successSvg,
    };
    this.toastService.show(toastConfig);
  }
}
