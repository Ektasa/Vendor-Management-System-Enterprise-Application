package com.vms.service;

import com.vms.entity.Invoice;
import com.vms.repository.InvoiceRepository;

import jakarta.validation.constraints.NotNull;

import org.springframework.lang.NonNull;
// import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice createInvoice(@NotNull @NonNull Invoice invoiceData) {
        return invoiceRepository.save(invoiceData);
    }

    public Invoice getInvoiceById(@NotNull @NonNull Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public void deleteInvoice(@NotNull @NonNull Long id) {
        if (!invoiceRepository.existsById(id)) {
            throw new RuntimeException("Invoice not found");
        }
        invoiceRepository.deleteById(id);
    }
}