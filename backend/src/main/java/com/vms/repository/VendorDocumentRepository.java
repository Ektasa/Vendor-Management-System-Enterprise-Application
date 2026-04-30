package com.vms.repository;

import com.vms.entity.VendorDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VendorDocumentRepository extends JpaRepository<VendorDocument, Long> {
    List<VendorDocument> findByVendorId(Long vendorId);
    List<VendorDocument> findByUploadedById(Long userId);
}