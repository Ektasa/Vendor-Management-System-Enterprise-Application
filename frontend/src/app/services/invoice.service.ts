import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Invoice, InvoiceRequest } from '../models/invoice.model';

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {
  private publisherUrl = 'http://localhost:8080/invoices';
  private microserviceUrl = 'http://localhost:8081/invoices';

  constructor(private http: HttpClient) {}

  getInvoices(userId?: number, role?: string): Observable<Invoice[]> {
    if (role === 'VENDOR' && userId) {
      return this.http.get<Invoice[]>(`${this.microserviceUrl}/vendor/${userId}`);
    }
    return this.http.get<Invoice[]>(this.microserviceUrl);
  }

  getInvoiceById(id: number): Observable<Invoice> {
    return this.http.get<Invoice>(`${this.microserviceUrl}/${id}`);
  }

  createInvoice(request: InvoiceRequest): Observable<Invoice> {
    return this.http.post<Invoice>(this.publisherUrl, request);
  }

  updateInvoiceStatus(invoiceId: number, status: 'PENDING' | 'APPROVED' | 'REJECTED') {
    return this.http.put<Invoice>(`${this.publisherUrl}/${invoiceId}/status?status=${status}`, {});
  }
}
