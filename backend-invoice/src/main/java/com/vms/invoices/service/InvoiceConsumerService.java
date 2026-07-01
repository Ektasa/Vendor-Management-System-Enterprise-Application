package com.vms.invoices.service;

import com.vms.invoices.model.Invoice;
import com.vms.invoices.model.InvoiceEvent;
import com.vms.invoices.repository.InvoiceRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class InvoiceConsumerService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceConsumerService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @KafkaListener(topics = "invoice-submitted", groupId = "invoice-service-group")
    public void consumeInvoiceEvent(InvoiceEvent event) {
        Invoice invoice = null;
        if (event.getInvoiceId() != null) {
            invoice = invoiceRepository.findById(event.getInvoiceId()).orElse(null);
        }
        if (invoice == null && event.getInvoiceNumber() != null) {
            invoice = invoiceRepository.findByInvoiceNumber(event.getInvoiceNumber()).orElse(null);
        }
        if (invoice == null) {
            invoice = new Invoice();
            invoice.setCreatedAt(LocalDateTime.now());
        }

        if (event.getInvoiceNumber() != null) {
            invoice.setInvoiceNumber(event.getInvoiceNumber());
        }
        if (event.getInvoiceDate() != null && !event.getInvoiceDate().isBlank()) {
            invoice.setInvoiceDate(LocalDate.parse(event.getInvoiceDate()));
        }
        if (event.getProductName() != null) {
            invoice.setProductName(event.getProductName());
        }
        if (event.getCompanyName() != null) {
            invoice.setCompanyName(event.getCompanyName());
        }
        if (event.getLocation() != null) {
            invoice.setLocation(event.getLocation());
        }
        if (event.getCustomerName() != null) {
            invoice.setCustomerName(event.getCustomerName());
        }
        if (event.getCustomerAddress() != null) {
            invoice.setCustomerAddress(event.getCustomerAddress());
        }
        if (event.getCustomerEmail() != null) {
            invoice.setCustomerEmail(event.getCustomerEmail());
        }
        if (event.getCustomerPhone() != null) {
            invoice.setCustomerPhone(event.getCustomerPhone());
        }
        if (event.getItemDescription() != null) {
            invoice.setItemDescription(event.getItemDescription());
        }
        if (event.getQuantity() > 0) {
            invoice.setQuantity(event.getQuantity());
        }
        if (event.getUnitPrice() > 0) {
            invoice.setUnitPrice(event.getUnitPrice());
        }
        if (event.getTotalAmount() > 0) {
            invoice.setTotalAmount(event.getTotalAmount());
        }
        if (event.getGstAmount() > 0) {
            invoice.setGstAmount(event.getGstAmount());
        }
        if (event.getSupportingDocumentName() != null) {
            invoice.setSupportingDocumentName(event.getSupportingDocumentName());
        }
        if (event.getStatus() != null) {
            invoice.setStatus(Invoice.Status.valueOf(event.getStatus()));
        }
        if (event.getVendorUserId() != null) {
            invoice.setVendorUserId(event.getVendorUserId());
        }
        if (event.getVendorName() != null) {
            invoice.setVendorName(event.getVendorName());
        }
        if (event.getVendorEmail() != null) {
            invoice.setVendorEmail(event.getVendorEmail());
        }
        if (event.getSubmittedAt() != null) {
            invoice.setSubmittedAt(LocalDateTime.parse(event.getSubmittedAt()));
        }
        if (invoice.getCreatedAt() == null) {
            invoice.setCreatedAt(LocalDateTime.now());
        }

        invoiceRepository.save(invoice);
    }
}
