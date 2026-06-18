package com.vms.service;

import com.vms.dto.VendorDTO;
import com.vms.dto.VendorRequest;
import com.vms.entity.User;
import com.vms.entity.Vendor;
import com.vms.entity.Vendor.VendorStatus;
import com.vms.repository.UserRepository;
import com.vms.repository.VendorRepository;

import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorService {
    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;

    public VendorService(VendorRepository vendorRepository, UserRepository userRepository) {
        this.vendorRepository = vendorRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @PreAuthorize("hasRole('VENDOR')")
    public VendorDTO createVendor(VendorRequest request, @NonNull Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (vendorRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("Vendor profile already exists for this user");
        }

        Vendor vendor = new Vendor();
        vendor.setCompanyName(request.getCompanyName());
        vendor.setContactPerson(request.getContactPerson());
        vendor.setEmail(request.getEmail());
        vendor.setPhone(request.getPhone());
        vendor.setAddress(request.getAddress());
        vendor.setCity(request.getCity());
        vendor.setCountry(request.getCountry());
        vendor.setDescription(request.getDescription());
        vendor.setStatus(VendorStatus.PENDING);
        vendor.setUser(user);

        vendorRepository.save(vendor);
        return mapToDTO(vendor);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public VendorDTO updateVendorStatus(@NonNull Long vendorId, VendorStatus status) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        vendor.setStatus(status);
        vendorRepository.save(vendor);
        return mapToDTO(vendor);
    }

    @Transactional
    @PreAuthorize("hasRole('VENDOR')")
    public VendorDTO getVendorByUserId(Long userId) {
        Vendor vendor = vendorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
        return mapToDTO(vendor);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<VendorDTO> getAllVendors() {
        return vendorRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<VendorDTO> getVendorsByStatus(VendorStatus status) {
        return vendorRepository.findByStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasRole('VENDOR')")
    public VendorDTO updateVendor(VendorRequest request, Long userId) {
        Vendor vendor = vendorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        vendor.setCompanyName(request.getCompanyName());
        vendor.setContactPerson(request.getContactPerson());
        vendor.setEmail(request.getEmail());
        vendor.setPhone(request.getPhone());
        vendor.setAddress(request.getAddress());
        vendor.setCity(request.getCity());
        vendor.setCountry(request.getCountry());
        vendor.setDescription(request.getDescription());

        vendorRepository.save(vendor);
        return mapToDTO(vendor);
    }

    private VendorDTO mapToDTO(Vendor vendor) {
        VendorDTO dto = new VendorDTO();
        dto.setId(vendor.getId());
        dto.setCompanyName(vendor.getCompanyName());
        dto.setContactPerson(vendor.getContactPerson());
        dto.setEmail(vendor.getEmail());
        dto.setPhone(vendor.getPhone());
        dto.setAddress(vendor.getAddress());
        dto.setCity(vendor.getCity());
        dto.setCountry(vendor.getCountry());
        dto.setStatus(vendor.getStatus());
        dto.setUserId(vendor.getUser().getId());
        dto.setUserFullName(vendor.getUser().getFullName());
        dto.setDocumentsUrl(vendor.getDocumentsUrl());
        dto.setDescription(vendor.getDescription());
        dto.setCreatedAt(vendor.getCreatedAt());
        dto.setUpdatedAt(vendor.getUpdatedAt());
        return dto;
    }
}