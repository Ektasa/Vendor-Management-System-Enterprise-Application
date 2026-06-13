import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './register.component.html'
})
export class RegisterComponent {
  fullName = '';
  email = '';
  password = '';
  role: 'ADMIN' | 'MANAGER' | 'VENDOR' = 'VENDOR';
  error = '';

  constructor(private authService: AuthService, private router: Router) {}

  onRegister(): void {
    this.error = '';
    console.log('RegisterComponent: submitting register request', {
      email: this.email,
      fullName: this.fullName,
      role: this.role
    });

    this.authService.register({
      email: this.email,
      password: this.password,
      fullName: this.fullName,
      role: this.role
    }).subscribe({
      next: () => {
        console.log('Registration successful, navigating to dashboard');
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('RegisterComponent: registration failed', err);
        this.error = err.error?.message || err.message || 'Registration failed';
      }
    });
  }
}