import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { OnboardingComponent } from './components/onboarding/onboarding.component';
import { VendorListComponent } from './components/vendor-list/vendor-list.component';
import { InvoiceComponent } from './components/invoice/invoice.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: OnboardingComponent },
  { path: 'onboarding', component: OnboardingComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },
  { path: 'vendors', component: VendorListComponent, canActivate: [authGuard] },
  { path: 'invoices', component: InvoiceComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '/home' }
];