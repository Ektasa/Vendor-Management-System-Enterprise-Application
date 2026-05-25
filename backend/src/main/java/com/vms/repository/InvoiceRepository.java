package com.vms.repository;

import com.vms.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
//   public Invoice findByInvoiceNumber(String invoiceNumber);
//    public Invoice createInvoice(Invoice invoiceData);
//    public List<Invoice> getAllInvoices();
//     public Invoice getInvoiceById(Long id);
//     public void deleteInvoice(Long id);
    
}
    