package com.vms.controller;

import com.vms.dto.InvoiceRequest;
import com.vms.entity.Invoice;
import com.vms.entity.Invoice.InvoiceStatus;
import com.vms.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<Invoice> createInvoice(
            @Valid @RequestBody InvoiceRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Invoice createdInvoice = invoiceService.createInvoice(request, userDetails);
        return ResponseEntity.ok(createdInvoice);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'VENDOR')")
    public ResponseEntity<List<Invoice>> getInvoices(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(invoiceService.getInvoicesForCurrentUser(userDetails));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'VENDOR')")
    public ResponseEntity<Invoice> getInvoiceById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Invoice invoice = invoiceService.getInvoiceById(id, userDetails);
        return ResponseEntity.ok(invoice);
    }

    @PutMapping("/{invoiceId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Invoice> updateInvoiceStatus(
            @PathVariable Long invoiceId,
            @RequestParam InvoiceStatus status) {
        return ResponseEntity.ok(invoiceService.updateInvoiceStatus(invoiceId, status));
    }
}
