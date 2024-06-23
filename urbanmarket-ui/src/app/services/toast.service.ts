import { Injectable } from '@angular/core';
import { Toast } from '../models/Toast';
import { DomSanitizer } from '@angular/platform-browser';

@Injectable({ providedIn: 'root' })
export class ToastService {
  toasts: Toast[] = [];
  dangerSvg = `
    <svg width="16px" height="16px" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <circle cx="12" cy="17" r="1" fill="#000000"/>
      <path d="M12 10L12 14" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
      <path d="M3.44722 18.1056L10.2111 4.57771C10.9482 3.10361 13.0518 3.10362 13.7889 4.57771L20.5528 18.1056C21.2177 19.4354 20.2507 21 18.7639 21H5.23607C3.7493 21 2.78231 19.4354 3.44722 18.1056Z" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
    </svg>
  `;
  successSvg = `
    <svg fill="#000000" xmlns="http://www.w3.org/2000/svg" 
      width="16px" height="16px" viewBox="0 0 52 52" enable-background="new 0 0 52 52" xml:space="preserve">
      <path d="M26,2C12.7,2,2,12.7,2,26s10.7,24,24,24s24-10.7,24-24S39.3,2,26,2z M39.4,20L24.1,35.5
        c-0.6,0.6-1.6,0.6-2.2,0L13.5,27c-0.6-0.6-0.6-1.6,0-2.2l2.2-2.2c0.6-0.6,1.6-0.6,2.2,0l4.4,4.5c0.4,0.4,1.1,0.4,1.5,0L35,15.5
        c0.6-0.6,1.6-0.6,2.2,0l2.2,2.2C40.1,18.3,40.1,19.3,39.4,20z"/>
    </svg>
  `;
  constructor(private sanitizer: DomSanitizer) {}
  /**
   * Shows a toast message with the given configuration.
   *
   * @param toast - The Toast object containing the configuration for the toast message.
   * @remarks
   * This method sanitizes the `headerSvg` property if it is a string and adds the toast to the `toasts` array.
   *
   * @example
   * ```typescript
   * const toast: Toast = {
   *   message: 'Hello, world!',
   *   headerSvg: '<svg><path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z"/></svg>',
   *   type: 'success'
   * };
   * toastService.show(toast);
   * ```
   */
  show(toast: Toast) {
    if (toast.headerSvg && typeof toast.headerSvg == 'string') {
      toast.headerSvg = this.sanitizer.bypassSecurityTrustHtml(toast.headerSvg);
    }
    this.toasts.push(toast);
  }

  /**
   * Removes a toast from the `toasts` array based on the provided `toast` object.
   *
   * @param toast - The Toast object to be removed from the `toasts` array.
   * @returns Nothing. This method modifies the `toasts` array in-place.
   * @remarks This method uses the `filter` method to remove the specified toast from the `toasts` array.
   * @example
   * ```typescript
   * const toast: Toast = {
   *   message: 'Hello, world!',
   *   headerSvg: '<svg><path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z"/></svg>',
   *   type: 'success'
   * };
   * toastService.show(toast);
   * // ... some time later ...
   * toastService.remove(toast);
   * ```
   */
  remove(toast: Toast) {
    this.toasts = this.toasts.filter((t) => t !== toast);
  }

  /**
   * Clears all the toasts from the `toasts` array.
   */
  clear() {
    this.toasts.splice(0, this.toasts.length);
  }

  showErrorToast(err: any) {
    const toastConfig: Toast = {
      header: err.error.title,
      body: err.error.message,
      classname: 'text-bg-danger',
      delay: 5000,
      headerSvg: this.dangerSvg,
    };
    this.show(toastConfig);
  }
}
