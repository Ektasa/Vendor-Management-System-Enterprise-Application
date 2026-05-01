package com.vms.service;

import com.vms.dto.VendorDTO;
import com.vms.dto.VendorRequest;
import com.vms.entity.User;
import com.vms.entity.Vendor;
import com.vms.entity.Vendor.VendorStatus;
import com.vms.repository.UserRepository;
import com.vms.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorService {
    private final VendorRepository vendorRepository;
    private final UserRepository userRepository;

    @Transactional
    @PreAuthorize("hasRole('VENDOR')")
    public VendorDTO createVendor(VendorRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (vendorRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("Vendor profile already exists for this user");
        }

        Vendor vendor = Vendor.builder()
                .companyName(request.getCompanyName())
                .contactPerson(request.getContactPerson())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .description(request.getDescription())
                .status(VendorStatus.PENDING)
                .user(user)
                .build();

        vendorRepository.save(vendor);
        return mapToDTO(vendor);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public VendorDTO updateVendorStatus(Long vendorId, VendorStatus status) {
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
        return VendorDTO.builder()
                .id(vendor.getId())
                .companyName(vendor.getCompanyName())
                .contactPerson(vendor.getContactPerson())
                .email(vendor.getEmail())
                .phone(vendor.getPhone())
                .address(vendor.getAddress())
                .city(vendor.getCity())
                .country(vendor.getCountry())
                .status(vendor.getStatus())
                .userId(vendor.getUser().getId())
                .userFullName(vendor.getUser().getFullName())
                .documentsUrl(vendor.getDocumentsUrl())
                .description(vendor.getDescription())
                .createdAt(vendor.getCreatedAt())
                .updatedAt(vendor.getUpdatedAt())
                .build();
    }
}