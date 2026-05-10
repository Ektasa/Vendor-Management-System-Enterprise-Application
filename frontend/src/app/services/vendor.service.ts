import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vendor, VendorRequest } from '../models/vendor.model';

@Injectable({
  providedIn: 'root'
})
export class VendorService {
  private apiUrl = 'http://localhost:8080/api/vendors';

  constructor(private http: HttpClient) {}

  createVendor(request: VendorRequest): Observable<Vendor> {
    return this.http.post<Vendor>(this.apiUrl, request);
  }

  getMyProfile(): Observable<Vendor> {
    return this.http.get<Vendor>(`${this.apiUrl}/my-profile`);
  }

  updateMyProfile(request: VendorRequest): Observable<Vendor> {
    return this.http.put<Vendor>(`${this.apiUrl}/my-profile`, request);
  }

  getAllVendors(): Observable<Vendor[]> {
    return this.http.get<Vendor[]>(this.apiUrl);
  }

  getVendorsByStatus(status: string): Observable<Vendor[]> {
    const params = new HttpParams().set('status', status);
    return this.http.get<Vendor[]>(`${this.apiUrl}/status/${status}`);
  }

  updateVendorStatus(vendorId: number, status: string): Observable<Vendor> {
    return this.http.put<Vendor>(`${this.apiUrl}/${vendorId}/status?status=${status}`, {});
  }
}