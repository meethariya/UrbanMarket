import { DOCUMENT } from '@angular/common';
import {
  Component,
  OnInit,
  Renderer2,
  ElementRef,
  HostListener,
  Inject,
} from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent implements OnInit {
  ngOnInit(): void {
    // Handle window load event
    this.updateBackgroundColor();
  }

  constructor(
    private renderer: Renderer2,
    private el: ElementRef,
    @Inject(DOCUMENT) private document: Document
  ) {}

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
    const div = this.el.nativeElement.querySelector('.header');
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
}
