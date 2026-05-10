import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="auth-container">
      <div class="auth-box">
        <h2>Register</h2>
        <form (ngSubmit)="onRegister()">
          <div class="form-group">
            <label>Full Name</label>
            <input type="text" [(ngModel)]="fullName" name="fullName" required>
          </div>
          <div class="form-group">
            <label>Email</label>
            <input type="email" [(ngModel)]="email" name="email" required>
          </div>
          <div class="form-group">
            <label>Password</label>
            <input type="password" [(ngModel)]="password" name="password" required>
          </div>
          <div class="form-group">
            <label>Role</label>
            <select [(ngModel)]="role" name="role" required>
              <option value="VENDOR">Vendor</option>
              <option value="MANAGER">Manager</option>
              <option value="ADMIN">Admin</option>
            </select>
          </div>
          <button type="submit" class="btn btn-primary" style="width: 100%">Register</button>
        </form>
        <p style="text-align: center; margin-top: 16px">
          Already have an account? <a routerLink="/login">Login</a>
        </p>
        <div *ngIf="error" class="alert alert-error">{{ error }}</div>
      </div>
    </div>
  `
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
    this.authService.register({
      email: this.email,
      password: this.password,
      fullName: this.fullName,
      role: this.role
    }).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: (err) => this.error = err.error?.message || 'Registration failed'
    });
  }
}