import { Component } from '@angular/core';

import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { Router } from '@angular/router';
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="auth-container">
      <div class="auth-box">
        <h2>Vendor Management System</h2>
        <form (submit)="onLogin(emailInput.value, passwordInput.value)">
          <div class="form-group">
            <label>Email</label>
            <input #emailInput type="email" name="email" required>
          </div>
          <div class="form-group">
            <label>Password</label>
            <input #passwordInput type="password" name="password" required>
          </div>
          <button type="submit" class="btn btn-primary" style="width: 100%">Login</button>
        </form>
        <p style="text-align: center; margin-top: 16px">
          Don't have an account? <a routerLink="/register">Register</a>
        </p>
        <div *ngIf="error" class="alert alert-error">{{ error }}</div>
      </div>
    </div>
  `
})
export class LoginComponent {
  error = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin(email: string, password: string): void {
    this.error = '';
    this.authService.login({ email, password }).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: (err) => this.error = err.error?.message || 'Login failed'
    });
  }
}