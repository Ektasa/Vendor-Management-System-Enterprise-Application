package com.vms.service;

import com.vms.dto.InvoiceRequest;
import com.vms.entity.Invoice;
import com.vms.entity.User;
import com.vms.kafka.InvoiceEvent;
import com.vms.repository.InvoiceRepository;
import com.vms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private KafkaTemplate<String, InvoiceEvent> kafkaTemplate;

    @Mock
    private UserDetails userDetails;

    private InvoiceService invoiceService;

    @BeforeEach
    void setUp() {
        invoiceService = new InvoiceService(invoiceRepository, userRepository, kafkaTemplate);
    }

    @Test
    void createInvoiceShouldSaveAndPublish() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("vendor@vms.com");
        user.setFullName("Vendor Vendor");
        user.setRole(User.Role.VENDOR);

        InvoiceRequest request = new InvoiceRequest();
        request.setInvoiceNumber("INV-001");
        request.setQuantity(2);
        request.setUnitPrice(50.0);
        request.setTotalAmount(100.0);

        when(userDetails.getUsername()).thenReturn("vendor@vms.com");
        when(userRepository.findByEmail("vendor@vms.com")).thenReturn(Optional.of(user));
        
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(invocation -> {
            Invoice inv = invocation.getArgument(0);
            inv.setId(100L); // Set generated ID
            return inv;
        });

        when(kafkaTemplate.send(anyString(), anyString(), any(InvoiceEvent.class))).thenReturn(null);

        // Act
        Invoice result = invoiceService.createInvoice(request, userDetails);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getId()); // Generated ID should be present
        assertEquals("INV-001", result.getInvoiceNumber());
        assertEquals(1L, result.getVendorUserId());

        verify(invoiceRepository, times(1)).save(any(Invoice.class));
        verify(kafkaTemplate, times(1)).send(eq("invoice-submitted"), eq("100"), any(InvoiceEvent.class));
    }
}
