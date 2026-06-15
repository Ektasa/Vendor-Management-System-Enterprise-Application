import { Component,  OnInit } from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { VendorService } from '../../services/vendor.service';
import { Vendor, VendorRequest } from '../../models/vendor.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './dashboard.component.html'
})
// @NgModule({
//   imports: [CommonModule, FormsModule, RouterModule],
//   declarations: [DashboardComponent]
// })
export class DashboardComponent implements OnInit {
  user = this.authService.currentUser();
  vendors: Vendor[] = [];
  myVendor: Vendor | null = null;
  isEditMode = false;
  vendorRequest: VendorRequest = {
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
      next: (data) => {
        this.myVendor = data;
        this.vendorRequest = {
          companyName: data.companyName,
          contactPerson: data.contactPerson,
          email: data.email,
          phone: data.phone,
          address: data.address,
          city: data.city,
          country: data.country,
          description: data.description
        };
      },
      error: () => {
        this.myVendor = null;
        this.resetVendorRequest();
      }
    });
  }

  createVendor(): void {
    this.vendorService.createVendor(this.vendorRequest).subscribe({
      next: (data) => {
        this.myVendor = data;
        this.isEditMode = false;
      },
      error: (err) => console.error(err)
    });
  }

  editProfile(): void {
    this.isEditMode = true;
  }

  cancelEdit(): void {
    this.isEditMode = false;
    if (this.myVendor) {
      this.vendorRequest = {
        companyName: this.myVendor.companyName,
        contactPerson: this.myVendor.contactPerson,
        email: this.myVendor.email,
        phone: this.myVendor.phone,
        address: this.myVendor.address,
        city: this.myVendor.city,
        country: this.myVendor.country,
        description: this.myVendor.description
      };
    } else {
      this.resetVendorRequest();
    }
  }

  updateProfile(): void {
    this.vendorService.updateMyProfile(this.vendorRequest).subscribe({
      next: (data) => {
        this.myVendor = data;
        this.isEditMode = false;
      },
      error: (err) => console.error(err)
    });
  }

  private resetVendorRequest(): void {
    this.vendorRequest = {
      companyName: '',
      contactPerson: '',
      email: '',
      phone: '',
      address: '',
      city: '',
      country: '',
      description: ''
    };
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

// function NgModule(arg0: { imports: any[]; declarations: (typeof DashboardComponent)[]; }): (target: typeof DashboardComponent) => void | typeof DashboardComponent {
//   throw new Error('Function not implemented.');
// }


// function NgModule(arg0: { imports: any[]; declarations: (typeof DashboardComponent)[]; }): (target: typeof DashboardComponent) => void | typeof DashboardComponent {
//   throw new Error('Function not implemented.');
// }
