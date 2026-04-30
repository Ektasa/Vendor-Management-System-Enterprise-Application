package com.vms.controller;

import com.vms.dto.VendorDTO;
import com.vms.dto.VendorRequest;
import com.vms.entity.Vendor.VendorStatus;
import com.vms.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {
    private final VendorService vendorService;

    @PostMapping
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<VendorDTO> createVendor(
            @RequestBody VendorRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(vendorService.createVendor(request, userId));
    }

    @GetMapping("/my-profile")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<VendorDTO> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(vendorService.getVendorByUserId(userId));
    }

    @PutMapping("/my-profile")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<VendorDTO> updateMyProfile(
            @RequestBody VendorRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        return ResponseEntity.ok(vendorService.updateVendor(request, userId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<VendorDTO>> getAllVendors() {
        return ResponseEntity.ok(vendorService.getAllVendors());
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<VendorDTO>> getVendorsByStatus(@PathVariable VendorStatus status) {
        return ResponseEntity.ok(vendorService.getVendorsByStatus(status));
    }

    @PutMapping("/{vendorId}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<VendorDTO> updateVendorStatus(
            @PathVariable Long vendorId,
            @RequestParam VendorStatus status) {
        return ResponseEntity.ok(vendorService.updateVendorStatus(vendorId, status));
    }

    private Long getUserId(UserDetails userDetails) {
        return 1L; // In real app, extract from JWT claims
    }
}