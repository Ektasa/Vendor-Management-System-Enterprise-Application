package com.vms.service;

import com.vms.dto.InvoiceRequest;
import com.vms.dto.InvoiceSummaryDTO;
import com.vms.entity.Invoice;
import com.vms.entity.Invoice.InvoiceStatus;
import com.vms.entity.User;
import com.vms.kafka.InvoiceEvent;
import com.vms.repository.InvoiceRepository;
import com.vms.repository.UserRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, InvoiceEvent> kafkaTemplate;
    private final String invoiceTopic = "invoice-submitted";

    public InvoiceService(InvoiceRepository invoiceRepository,
                          UserRepository userRepository,
                          KafkaTemplate<String, InvoiceEvent> kafkaTemplate) {
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Invoice createInvoice(@NonNull InvoiceRequest invoiceData, @NonNull UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != User.Role.VENDOR) {
            throw new RuntimeException("Only vendor users can submit invoices");
        }

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceData.getInvoiceNumber());
        invoice.setInvoiceDate(invoiceData.getInvoiceDate() != null && !invoiceData.getInvoiceDate().isBlank()
                ? LocalDate.parse(invoiceData.getInvoiceDate())
                : LocalDate.now());
        invoice.setProductName(invoiceData.getProductName());
        invoice.setCompanyName(invoiceData.getCompanyName());
        invoice.setLocation(invoiceData.getLocation());
        invoice.setCustomerName(invoiceData.getCustomerName());
        invoice.setCustomerAddress(invoiceData.getCustomerAddress());
        invoice.setCustomerEmail(invoiceData.getCustomerEmail());
        invoice.setCustomerPhone(invoiceData.getCustomerPhone());
        invoice.setItemDescription(invoiceData.getItemDescription());
        invoice.setQuantity(invoiceData.getQuantity());
        invoice.setUnitPrice(invoiceData.getUnitPrice());
        invoice.setTotalAmount(invoiceData.getTotalAmount());
        invoice.setGstAmount(invoiceData.getGstAmount());
        invoice.setSupportingDocumentName(invoiceData.getSupportingDocumentName());
        invoice.setStatus(InvoiceStatus.PENDING);
        invoice.setVendorUserId(user.getId());
        invoice.setVendorName(user.getFullName());
        invoice.setVendorEmail(user.getEmail());
        invoice.setSubmittedAt(LocalDateTime.now());
        invoice.setCreatedAt(LocalDateTime.now());

        publishInvoiceEvent(invoice);
        return invoice;
    }

    public List<Invoice> getInvoicesForCurrentUser(@NonNull UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == User.Role.VENDOR) {
            return invoiceRepository.findByVendorUserId(user.getId());
        }
        return invoiceRepository.findAll();
    }

    public Invoice getInvoiceById(@NonNull Long id, @NonNull UserDetails userDetails) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == User.Role.VENDOR && !invoice.getVendorUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return invoice;
    }

    public Invoice updateInvoiceStatus(@NonNull Long invoiceId, @NonNull InvoiceStatus status) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        invoice.setStatus(status);
        invoice.setSubmittedAt(invoice.getSubmittedAt() != null ? invoice.getSubmittedAt() : LocalDateTime.now());
        invoice.setCreatedAt(invoice.getCreatedAt() != null ? invoice.getCreatedAt() : LocalDateTime.now());
        invoiceRepository.save(invoice);
        publishInvoiceEvent(invoice);
        return invoice;
    }

    public List<InvoiceSummaryDTO> getInvoiceSummary() {
        List<Invoice> invoices = invoiceRepository.findAll();

        Map<Long, List<Invoice>> groupedByVendor = invoices.stream()
                .collect(Collectors.groupingBy(Invoice::getVendorUserId));

        return groupedByVendor.entrySet().stream()
                .map(entry -> {
                    List<Invoice> vendorInvoices = entry.getValue();
                    InvoiceSummaryDTO summary = new InvoiceSummaryDTO();
                    summary.setVendorUserId(entry.getKey());
                    summary.setVendorFullName(vendorInvoices.get(0).getVendorName());
                    summary.setVendorEmail(vendorInvoices.get(0).getVendorEmail());
                    summary.setInvoiceCount(vendorInvoices.size());
                    summary.setTotalAmount(vendorInvoices.stream().mapToDouble(Invoice::getTotalAmount).sum());
                    summary.setPendingCount((int) vendorInvoices.stream().filter(i -> i.getStatus() == InvoiceStatus.PENDING).count());
                    summary.setApprovedCount((int) vendorInvoices.stream().filter(i -> i.getStatus() == InvoiceStatus.APPROVED).count());
                    summary.setRejectedCount((int) vendorInvoices.stream().filter(i -> i.getStatus() == InvoiceStatus.REJECTED).count());
                    return summary;
                })
                .collect(Collectors.toList());
    }

    private void publishInvoiceEvent(@NonNull Invoice invoice) {
        InvoiceEvent event = new InvoiceEvent();
        event.setInvoiceId(invoice.getId());
        event.setInvoiceNumber(invoice.getInvoiceNumber());
        event.setInvoiceDate(invoice.getInvoiceDate() != null ? invoice.getInvoiceDate().toString() : null);
        event.setProductName(invoice.getProductName());
        event.setCompanyName(invoice.getCompanyName());
        event.setLocation(invoice.getLocation());
        event.setCustomerName(invoice.getCustomerName());
        event.setCustomerAddress(invoice.getCustomerAddress());
        event.setCustomerEmail(invoice.getCustomerEmail());
        event.setCustomerPhone(invoice.getCustomerPhone());
        event.setItemDescription(invoice.getItemDescription());
        event.setQuantity(invoice.getQuantity());
        event.setUnitPrice(invoice.getUnitPrice());
        event.setTotalAmount(invoice.getTotalAmount());
        event.setGstAmount(invoice.getGstAmount());
        event.setSupportingDocumentName(invoice.getSupportingDocumentName());
        event.setVendorUserId(invoice.getVendorUserId());
        event.setVendorName(invoice.getVendorName());
        event.setVendorEmail(invoice.getVendorEmail());
        event.setStatus(invoice.getStatus().name());
        event.setSubmittedAt(invoice.getSubmittedAt() != null ? invoice.getSubmittedAt().toString() : null);

        kafkaTemplate.send(invoiceTopic, Long.toString(invoice.getId()), event);
    }
}
