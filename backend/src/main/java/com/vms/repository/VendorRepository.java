package com.vms.repository;

import com.vms.entity.Vendor;
import com.vms.entity.Vendor.VendorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findByUserId(Long userId);
    
    @Query("SELECT v FROM Vendor v WHERE v.user.id = :userId")
    Optional<Vendor> findByUser_Id(@Param("userId") Long userId);
    
    List<Vendor> findByStatus(VendorStatus status);
    
    @Query("SELECT v FROM Vendor v WHERE v.status = :status")
    List<Vendor> findByVendorStatus(@Param("status") VendorStatus status);
    
    @Query("SELECT v FROM Vendor v WHERE LOWER(v.companyName) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Vendor> searchByCompanyName(@Param("search") String search);
}