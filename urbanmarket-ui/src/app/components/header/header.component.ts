import { DOCUMENT } from '@angular/common';
import {
  Component,
  Renderer2,
  ElementRef,
  HostListener,
  Inject,
  ViewChild,
  AfterViewInit,
  inject,
} from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgbOffcanvas } from '@ng-bootstrap/ng-bootstrap';
import { LoginComponent } from '../login/login.component';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent implements AfterViewInit {
  @ViewChild('header') headerDiv!: ElementRef<HTMLDivElement>;
  private offcanvasService = inject(NgbOffcanvas);
  constructor(
    private renderer: Renderer2,
    @Inject(DOCUMENT) private document: Document
  ) {}

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
		this.offcanvasService.open(LoginComponent,{ position: 'end', backdropClass: 'bg-secondary' });
	}
}
