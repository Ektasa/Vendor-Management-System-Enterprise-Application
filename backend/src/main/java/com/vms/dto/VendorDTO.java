package com.vms.dto;

import com.vms.entity.Vendor.VendorStatus;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorDTO {
    private Long id;
    private String companyName;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String country;
    private VendorStatus status;
    private Long userId;
    private String userFullName;
    private String documentsUrl;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}