import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { VendorService } from '../../services/vendor.service';
import { Vendor } from '../../models/vendor.model';

@Component({
  selector: 'app-vendor-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './vendor-list.component.html',
  styleUrls: ['./vendor-list.component.css']
})
export class VendorListComponent implements OnInit {
  vendors: Vendor[] = [];
  filteredVendors: Vendor[] = [];
  user = this.authService.currentUser();
  statusFilter = '';
  searchTerm = '';
  isLoading = true;
  error = '';

  constructor(
    private vendorService: VendorService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.user = this.authService.currentUser();
    if (this.user?.role !== 'ADMIN' && this.user?.role !== 'MANAGER') {
      this.router.navigate(['/dashboard']);
      return;
    }
    this.loadVendors();
  }

  loadVendors(): void {
    this.isLoading = true;
    this.error = '';
    this.vendorService.getAllVendors().subscribe({
      next: (data) => {
        this.vendors = data;
        this.applyFilters();
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Failed to load vendors';
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  applyFilters(): void {
    this.filteredVendors = this.vendors.filter(vendor => {
      const matchesStatus = !this.statusFilter || vendor.status === this.statusFilter;
      const matchesSearch = !this.searchTerm ||
        vendor.companyName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        vendor.email.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        vendor.contactPerson.toLowerCase().includes(this.searchTerm.toLowerCase());
      return matchesStatus && matchesSearch;
    });
  }

  onSearchChange(): void {
    this.applyFilters();
  }

  onStatusFilterChange(): void {
    this.applyFilters();
  }

  updateStatus(vendorId: number, newStatus: string): void {
    if (!newStatus) return;

    this.vendorService.updateVendorStatus(vendorId, newStatus).subscribe({
      next: () => {
        const vendor = this.vendors.find(v => v.id === vendorId);
        if (vendor) {
          vendor.status = newStatus;
          this.applyFilters();
        }
      },
      error: (err) => {
        this.error = 'Failed to update vendor status';
        console.error(err);
      }
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

  getStatusColor(status: string): string {
    switch (status) {
      case 'PENDING':
        return 'status-pending';
      case 'APPROVED':
        return 'status-approved';
      case 'REJECTED':
        return 'status-rejected';
      default:
        return '';
    }
  }
}
