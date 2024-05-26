import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './components/home/home.component';
import { AboutComponent } from './components/about/about.component';
import { PartnersComponent } from './components/partners/partners.component';
import { NgbTooltipModule } from '@ng-bootstrap/ng-bootstrap';
import { BlockComponent } from './components/block/block.component';

/**
 * Module for home pages such as /home, /about, /contact, /auth, etc.
 */
@NgModule({
  declarations: [HomeComponent, AboutComponent, PartnersComponent, BlockComponent],
  imports: [CommonModule, HomeRoutingModule, NgbTooltipModule],
})
export class HomeModule {}
