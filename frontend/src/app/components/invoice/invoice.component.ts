import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { InvoiceService } from '../../services/invoice.service';
import { Invoice, InvoiceRequest } from '../../models/invoice.model';

@Component({
  selector: 'app-invoice',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.css']
})
export class InvoiceComponent implements OnInit {
  user = this.authService.currentUser();
  invoices: Invoice[] = [];
  invoiceRequest: InvoiceRequest = {
    invoiceNumber: '',
    invoiceDate: '',
    customerName: '',
    customerAddress: '',
    customerEmail: '',
    customerPhone: '',
    itemDescription: '',
    quantity: 1,
    unitPrice: 0,
    totalAmount: 0
  };
  isSubmitting = false;
  error = '';

  constructor(
    private authService: AuthService,
    private invoiceService: InvoiceService
  ) {}

  ngOnInit(): void {
    this.user = this.authService.currentUser();
    this.loadInvoices();
  }

  get isVendor(): boolean {
    return this.user?.role === 'VENDOR';
  }

  get isAdminOrManager(): boolean {
    return this.user?.role === 'ADMIN' || this.user?.role === 'MANAGER';
  }

  loadInvoices(): void {
    this.invoiceService.getInvoices().subscribe({
      next: (data) => {
        this.invoices = data;
      },
      error: (err) => {
        this.error = 'Could not load invoices.';
        console.error(err);
      }
    });
  }

  calculateTotal(): void {
    this.invoiceRequest.totalAmount = this.invoiceRequest.quantity * this.invoiceRequest.unitPrice;
  }

  submitInvoice(): void {
    this.error = '';
    this.isSubmitting = true;
    this.calculateTotal();
    this.invoiceService.createInvoice(this.invoiceRequest).subscribe({
      next: (invoice) => {
        this.invoices.unshift(invoice);
        this.invoiceRequest = {
          invoiceNumber: '',
          invoiceDate: '',
          customerName: '',
          customerAddress: '',
          customerEmail: '',
          customerPhone: '',
          itemDescription: '',
          quantity: 1,
          unitPrice: 0,
          totalAmount: 0
        };
        this.isSubmitting = false;
      },
      error: (err) => {
        this.error = 'Invoice submission failed.';
        console.error(err);
        this.isSubmitting = false;
      }
    });
  }

  updateStatus(invoice: Invoice, status: 'PENDING' | 'APPROVED' | 'REJECTED'): void {
    this.invoiceService.updateInvoiceStatus(invoice.id, status).subscribe({
      next: (updated) => {
        invoice.status = updated.status;
      },
      error: (err) => {
        this.error = 'Could not update invoice status.';
        console.error(err);
      }
    });
  }
}
