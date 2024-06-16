import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './components/home/home.component';
import { AboutComponent } from './components/about/about.component';
import { PartnersComponent } from './components/partners/partners.component';
import { NgbDatepickerModule, NgbProgressbarModule, NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { BlockComponent } from './components/block/block.component';
import { HeroBannerComponent } from '../../components/hero-banner/hero-banner.component';
import { OurServicesComponent } from './components/our-services/our-services.component';
import { RegisterComponent } from './components/register/register.component';
import { SlickCarouselModule } from 'ngx-slick-carousel';
import { ReactiveFormsModule } from '@angular/forms';

/**
 * Module for home pages such as /home, /about, /contact, /auth, etc.
 */
@NgModule({
  declarations: [HomeComponent, AboutComponent, PartnersComponent, BlockComponent, OurServicesComponent, RegisterComponent],
  imports: [CommonModule, HomeRoutingModule, NgbTooltipModule, HeroBannerComponent, SlickCarouselModule, ReactiveFormsModule, NgbDatepickerModule, NgbProgressbarModule],
})
export class HomeModule {}
