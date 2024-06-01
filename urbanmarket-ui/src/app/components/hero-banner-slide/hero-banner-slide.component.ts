import { CommonModule, DOCUMENT } from '@angular/common';
import {
  AfterViewInit,
  Component,
  ElementRef,
  HostListener,
  Inject,
  Input,
  Renderer2,
  ViewChild,
} from '@angular/core';

@Component({
  selector: 'app-hero-banner-slide',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './hero-banner-slide.component.html',
  styleUrl: './hero-banner-slide.component.scss',
})
export class HeroBannerSlideComponent implements AfterViewInit {
  @Input() subtitle!: string;
  @Input() title!: string;
  @Input() description!: string;
  @Input() buttonText!: string;
  @Input() image!: string;
  @Input() backgroundColor!: string;
  @Input() color!: string;

  @ViewChild('textarea') textArea!: ElementRef<HTMLDivElement>;
  constructor(
    private renderer: Renderer2,
    @Inject(DOCUMENT) private document: Document
  ) {}
  ngAfterViewInit(): void {
    // Handle window load event
    this.translateTextArea();
  }

  /**
   * On scroll event
   */
  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.translateTextArea();
  }
  /**
   * Update the translate of the textarea of component
   */
  private translateTextArea() {
    const div = this.textArea.nativeElement;
    var scrollPosition = this.document.documentElement.scrollTop || this.document.body.scrollTop || 0;
    if(scrollPosition < 300) {
      this.renderer.setStyle(div, "transform",`translateY(${-1*scrollPosition/1.5}px)`);
      this.renderer.setStyle(div, "opacity",1 - scrollPosition/300);
    }
  }
}
