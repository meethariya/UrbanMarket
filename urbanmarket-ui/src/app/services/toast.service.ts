import { Injectable } from '@angular/core';
import { Toast } from '../models/Toast';
import { DomSanitizer } from '@angular/platform-browser';

@Injectable({ providedIn: 'root' })
export class ToastService {
  toasts: Toast[] = [];
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
}
