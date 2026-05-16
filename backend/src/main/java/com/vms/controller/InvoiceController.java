package com.vms.controller;

import com.vms.entity.Invoice;
import com.vms.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController{

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(invoice);
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoicedata)
    {
        Invoice createdInvoice=invoiceService.createInvoice(invoicedata);
        return ResponseEntity.ok(createdInvoice);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Invoice>> getAllInvoices()
    {
        List<Invoice> invoices=invoiceService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }

}