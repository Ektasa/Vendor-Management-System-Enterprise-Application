package com.vms.invoices.controller;

import com.vms.invoices.model.Invoice;
import com.vms.invoices.repository.InvoiceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;

    public InvoiceController(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getInvoices() {
        return ResponseEntity.ok(invoiceRepository.findAll());
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<Invoice>> getInvoicesByVendor(@PathVariable Long vendorId) {
        return ResponseEntity.ok(invoiceRepository.findByVendorUserId(vendorId));
    }
}
