export interface Invoice {
  id: number;
  invoiceNumber: string;
  invoiceDate: string;
  productName: string;
  companyName: string;
  location: string;
  customerName: string;
  customerAddress: string;
  customerEmail: string;
  customerPhone: string;
  itemDescription: string;
  quantity: number;
  unitPrice: number;
  totalAmount: number;
  gstAmount: number;
  supportingDocumentName: string;
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
  productName: string;
  companyName: string;
  location: string;
  customerName: string;
  customerAddress: string;
  customerEmail: string;
  customerPhone: string;
  itemDescription: string;
  quantity: number;
  unitPrice: number;
  totalAmount: number;
  gstAmount: number;
  supportingDocumentName: string;
}
