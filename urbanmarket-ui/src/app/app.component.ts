import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import {isPlatformBrowser} from "@angular/common";
import AOS from 'aos';
import { ToastContainerComponent } from './components/toast-container/toast-container.component';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, FooterComponent, ToastContainerComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  title = 'urbanmarket-ui';
  ngOnInit(): void {
    // Iniitate AOS only on browser load. not on SSR
    if(isPlatformBrowser(this.platformId)) {
      AOS.init();    
    }
  }
  constructor(@Inject(PLATFORM_ID) private platformId: Object){}
}
