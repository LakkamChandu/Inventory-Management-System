package com.ims.inventory_management_system.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ims.inventory_management_system.Entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>{

    Optional<Supplier> findByName(String name); //for checking duplicate supplier names
    Optional<Supplier> findByEmail(String email);
    Optional<Supplier> findByGstin(String gstin);

}
