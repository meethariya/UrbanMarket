import { Component, inject } from '@angular/core';
import { NgbToastModule } from '@ng-bootstrap/ng-bootstrap';
import { ToastService } from '../../services/toast.service';
/**
 * ToastContainerComponent is a custom Angular component that displays a container for toast messages.
 *
 * It uses the `NgbToastModule` from `@ng-bootstrap/ng-bootstrap` to render each toast message.
 *
 * The component iterates over the `toasts` array from the `ToastService` and renders each toast message as an `NgbToast` component.
 *
 * Each toast message is rendered with a header, a body, and an optional SVG icon. The header and body content are extracted from the `Toast` object.
 *
 * The `ToastContainerComponent` listens for the `hidden` event of each `NgbToast` component and removes the corresponding toast from the `toasts` array when it is hidden.
 *
 * The component's template includes CSS styles to position the toast container at the bottom-right corner of the screen and to limit the height of the toast message body.
 */
@Component({
  selector: 'app-toast-container',
  standalone: true,
  imports: [NgbToastModule],
  template: `
    <div class="toast-container">
      @for (toast of toastService.toasts; track toast) {
        <ngb-toast
          [class]="toast.classname"
          [autohide]="toast.autohide || true"
          [delay]="toast.delay || 5000"
          (hidden)="toastService.remove(toast)"
        >
          <ng-template ngbToastHeader>
            <span [innerHTML]="toast.headerSvg"></span>
            <i class="bi bi-info-circle me-2"></i>
            <strong class="me-auto">{{ toast.header }}</strong>
          </ng-template>
          <div class="toast-message-body"> {{ toast.body }} </div>
        </ngb-toast>
      }
    </div>
  `,
  styles: `
  .toast-container {
    position: fixed;
    bottom: 0;
    right: 0;
    padding: 2rem;
    z-index: 1050;
    .toast-message-body {
      display: -webkit-box;
      -webkit-line-clamp: 3;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }
  }
  `
})
export class ToastContainerComponent {
  toastService = inject(ToastService);
}
