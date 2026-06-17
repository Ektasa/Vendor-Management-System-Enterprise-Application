import { Component } from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { Router } from '@angular/router';
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  error = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin(email: string, password: string): void {
    this.error = '';
    console.log('LoginComponent: submitting login request', { email });

    this.authService.login({ email, password }).subscribe({
      next: () => {
        console.log('LoginComponent: login successful');
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('LoginComponent: login failed', err);
        this.error = err.error?.message || err.message || 'Login failed';
      }
    });
  }
}