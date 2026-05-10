import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { VendorService } from '../services/vendor.service';
import { Vendor } from '../models/vendor.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="navbar">
      <div class="navbar-brand">Vendor Management System</div>
      <div class="navbar-menu">
        <span>Welcome, {{ user?.fullName }} ({{ user?.role }})</span>
        <a (click)="logout()" style="cursor: pointer">Logout</a>
      </div>
    </div>

    <div class="container">
      <!-- Admin/Manager View -->
      <ng-container *ngIf="user?.role !== 'VENDOR'">
        <div class="dashboard-grid">
          <div class="stat-card">
            <h3>Total Vendors</h3>
            <div class="value">{{ vendors.length }}</div>
          </div>
          <div class="stat-card">
            <h3>Pending Approval</h3>
            <div class="value">{{ getPendingCount() }}</div>
          </div>
          <div class="stat-card">
            <h3>Approved</h3>
            <div class="value">{{ getApprovedCount() }}</div>
          </div>
          <div class="stat-card">
            <h3>Rejected</h3>
            <div class="value">{{ getRejectedCount() }}</div>
          </div>
        </div>

        <div class="card">
          <div class="card-header">All Vendors</div>
          <table>
            <thead>
              <tr>
                <th>Company</th>
                <th>Contact</th>
                <th>Email</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let vendor of vendors">
                <td>{{ vendor.companyName }}</td>
                <td>{{ vendor.contactPerson }}</td>
                <td>{{ vendor.email }}</td>
                <td>
                  <span class="status-badge" [ngClass]="'status-' + vendor.status.toLowerCase()">
                    {{ vendor.status }}
                  </span>
                </td>
                <td>
                  <button *ngIf="vendor.status === 'PENDING'" class="btn btn-success" 
                    (click)="updateStatus(vendor.id, 'APPROVED')">Approve</button>
                  <button *ngIf="vendor.status === 'PENDING'" class="btn btn-danger" 
                    (click)="updateStatus(vendor.id, 'REJECTED')" style="margin-left: 8px">Reject</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </ng-container>

      <!-- Vendor View -->
      <ng-container *ngIf="user?.role === 'VENDOR'">
        <div class="card" *ngIf="!myVendor">
          <div class="card-header">Create Vendor Profile</div>
          <form (ngSubmit)="createVendor()">
            <div class="form-group">
              <label>Company Name</label>
              <input type="text" [(ngModel)]="vendorRequest.companyName" name="companyName" required>
            </div>
            <div class="form-group">
              <label>Contact Person</label>
              <input type="text" [(ngModel)]="vendorRequest.contactPerson" name="contactPerson" required>
            </div>
            <div class="form-group">
              <label>Email</label>
              <input type="email" [(ngModel)]="vendorRequest.email" name="email" required>
            </div>
            <div class="form-group">
              <label>Phone</label>
              <input type="text" [(ngModel)]="vendorRequest.phone" name="phone">
            </div>
            <div class="form-group">
              <label>Address</label>
              <input type="text" [(ngModel)]="vendorRequest.address" name="address">
            </div>
            <div class="form-group">
              <label>City</label>
              <input type="text" [(ngModel)]="vendorRequest.city" name="city">
            </div>
            <div class="form-group">
              <label>Country</label>
              <input type="text" [(ngModel)]="vendorRequest.country" name="country">
            </div>
            <div class="form-group">
              <label>Description</label>
              <textarea [(ngModel)]="vendorRequest.description" name="description" rows="4"></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Submit Profile</button>
          </form>
        </div>

        <div class="card" *ngIf="myVendor">
          <div class="card-header">My Vendor Profile</div>
          <p><strong>Company:</strong> {{ myVendor.companyName }}</p>
          <p><strong>Contact:</strong> {{ myVendor.contactPerson }}</p>
          <p><strong>Email:</strong> {{ myVendor.email }}</p>
          <p><strong>Phone:</strong> {{ myVendor.phone }}</p>
          <p><strong>Address:</strong> {{ myVendor.address }}, {{ myVendor.city }}, {{ myVendor.country }}</p>
          <p><strong>Status:</strong> 
            <span class="status-badge" [ngClass]="'status-' + myVendor.status.toLowerCase()">
              {{ myVendor.status }}
            </span>
          </p>
          <p *ngIf="myVendor.description"><strong>Description:</strong> {{ myVendor.description }}</p>
        </div>
      </ng-container>
    </div>
  `
})
export class DashboardComponent implements OnInit {
  user = this.authService.currentUser();
  vendors: Vendor[] = [];
  myVendor: Vendor | null = null;
  vendorRequest = {
    companyName: '',
    contactPerson: '',
    email: '',
    phone: '',
    address: '',
    city: '',
    country: '',
    description: ''
  };

  constructor(
    private authService: AuthService,
    private vendorService: VendorService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.user = this.authService.currentUser();
    if (this.user?.role === 'VENDOR') {
      this.loadMyProfile();
    } else {
      this.loadAllVendors();
    }
  }

  loadAllVendors(): void {
    this.vendorService.getAllVendors().subscribe({
      next: (data) => this.vendors = data,
      error: (err) => console.error(err)
    });
  }

  loadMyProfile(): void {
    this.vendorService.getMyProfile().subscribe({
      next: (data) => this.myVendor = data,
      error: () => this.myVendor = null
    });
  }

  createVendor(): void {
    this.vendorService.createVendor(this.vendorRequest).subscribe({
      next: (data) => this.myVendor = data,
      error: (err) => console.error(err)
    });
  }

  updateStatus(vendorId: number, status: string): void {
    this.vendorService.updateVendorStatus(vendorId, status).subscribe({
      next: () => this.loadAllVendors(),
      error: (err) => console.error(err)
    });
  }

  getPendingCount(): number {
    return this.vendors.filter(v => v.status === 'PENDING').length;
  }

  getApprovedCount(): number {
    return this.vendors.filter(v => v.status === 'APPROVED').length;
  }

  getRejectedCount(): number {
    return this.vendors.filter(v => v.status === 'REJECTED').length;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}