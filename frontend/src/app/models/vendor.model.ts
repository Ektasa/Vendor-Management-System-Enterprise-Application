export interface Vendor {
  id: number;
  companyName: string;
  contactPerson: string;
  email: string;
  phone: string;
  address: string;
  city: string;
  country: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'SUSPENDED';
  userId: number;
  userFullName: string;
  documentsUrl: string;
  description: string;
  createdAt: string;
  updatedAt: string;
}

export interface VendorRequest {
  companyName: string;
  contactPerson: string;
  email: string;
  phone: string;
  address: string;
  city: string;
  country: string;
  description: string;
}