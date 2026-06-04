import { Component, OnInit } from '@angular/core';

import { CommonModule } from '@angular/common';

import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { VendorService } from '../../services/vendor.service';
import { Vendor } from '../../models/vendor.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html'
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