import { Component } from '@angular/core';
import { SlickCarouselModule } from 'ngx-slick-carousel';
import { HeroBannerSlideComponent } from '../hero-banner-slide/hero-banner-slide.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-hero-banner',
  standalone: true,
  imports: [CommonModule, SlickCarouselModule, HeroBannerSlideComponent],
  templateUrl: './hero-banner.component.html',
  styleUrl: './hero-banner.component.scss',
})
export class HeroBannerComponent {
  slides = [
    {
      title: 'Inspire Your Mind',
      subtitle: 'evergreen',
      description:
        'Unleash your creativity and expand your horizons with our curated collection of books and stationery.',
      buttonText: 'shop now',
      image: 'assets/images/hero-banner/books.png',
      backgroundColor: '#F1F1E6',
      color: '#000000'
    },
    {
      title: "it's a special gift",
      subtitle: 'summer 2024',
      description: 'Celebrate her elegance and grace with a timeless gift that shines as brightly as she does.',
      buttonText: 'shop now',
      image: 'assets/images/hero-banner/jewellery.png',
      backgroundColor: '#637B84',
      color: '#FFFFFF'
    },
    {
      title: 'Unleash Your Boldness',
      subtitle: 'summer 2024',
      description:
        'Step into the spotlight with shoes that make a statement and showcase your unique style.',
      buttonText: 'explore more',
      image: 'assets/images/hero-banner/shoes.png',
      backgroundColor: '#7DB3C6',
      color: '#FFFFFF'
    },
    {
      title: 'Embrace the Beat',
      subtitle: 'season exclsive',
      description:
        'Rock your world with cool headphones that match your edgy style, bringing powerful sound and unmatched flair to every moment.',
      buttonText: 'shop now',
      image: 'assets/images/hero-banner/electronics.png',
      backgroundColor: '#1C343C',
      color: '#FFFFFF'
    },
    
  ];
  slideConfig = {
    slidesToShow: 1,
    slidesToScroll: 1,
    arrows: false,
    dots: false,
    infinte: true,
    // autoplay: true,
    // autoplaySpeed: 3000,
    pauseOnHover: false,
    zindex: 0
  };

  trackBySlideTitle(index: number, slide: any): string {
    return slide.title;
  }
}
