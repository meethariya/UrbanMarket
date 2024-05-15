import { CanActivateChildFn } from '@angular/router';

export const customerGuard: CanActivateChildFn = (childRoute, state) => {
  return true;
};
