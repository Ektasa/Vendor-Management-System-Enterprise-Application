export interface Invoice {
  id: number;
  invoiceNumber: string;
  invoiceDate: string;
  customerName: string;
  customerAddress: string;
  customerEmail: string;
  customerPhone: string;
  itemDescription: string;
  quantity: number;
  unitPrice: number;
  totalAmount: number;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  vendorUserId: number;
  vendorName: string;
  vendorEmail: string;
  submittedAt: string;
  createdAt: string;
}

export interface InvoiceRequest {
  invoiceNumber: string;
  invoiceDate: string;
  customerName: string;
  customerAddress: string;
  customerEmail: string;
  customerPhone: string;
  itemDescription: string;
  quantity: number;
  unitPrice: number;
  totalAmount: number;
}
