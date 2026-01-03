package com.ims.inventory_management_system.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ims.inventory_management_system.DTO.CreateSupplierRequest;
import com.ims.inventory_management_system.DTO.SupplierDTO;
import com.ims.inventory_management_system.DTO.UpdateSupplierRequest;
import com.ims.inventory_management_system.Service.SupplierService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SupplierDTO>> getAllSuppliers(){
        List<SupplierDTO> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-supplier/{id}")
    public ResponseEntity<SupplierDTO> getSupplierById(@PathVariable Long id){
        SupplierDTO supplier = supplierService.getSupplierById(id);
        return ResponseEntity.ok(supplier);
    }

    @PostMapping("/create-supplier")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierDTO> createSupplier(@Valid @RequestBody CreateSupplierRequest request) {
        SupplierDTO createdSupplier = supplierService.createSupplier(request);
        return new ResponseEntity<>(createdSupplier, HttpStatus.CREATED);
    }

    @PutMapping("/update-supplier/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierDTO> updateSupplier(@PathVariable Long id, @Valid @RequestBody UpdateSupplierRequest request) {
        SupplierDTO updatedSupplier = supplierService.updateSupplier(id, request);
        return ResponseEntity.ok(updatedSupplier);
    }

    @PostMapping("/deactivate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deactivateSupplier(@PathVariable Long id) {
        supplierService.deactivateSupplier(id);
        return  ResponseEntity.ok("Supplier De-Activated Succesfully.");
    }

    @PostMapping("/activate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> activateSupplier(@PathVariable Long id) {
        supplierService.activateSupplier(id);
        return  ResponseEntity.ok("Supplier Activated Succesfully.");
    }


}
