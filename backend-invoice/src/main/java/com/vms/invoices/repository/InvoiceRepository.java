package com.vms.invoices.repository;


import com.vms.invoices.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByVendorUserId(Long vendorUserId);
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}
