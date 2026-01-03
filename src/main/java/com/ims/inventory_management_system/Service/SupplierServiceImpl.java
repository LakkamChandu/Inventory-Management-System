package com.ims.inventory_management_system.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ims.inventory_management_system.Constants.MessageConstants;
import com.ims.inventory_management_system.DTO.CreateSupplierRequest;
import com.ims.inventory_management_system.DTO.SupplierDTO;
import com.ims.inventory_management_system.DTO.UpdateSupplierRequest;
import com.ims.inventory_management_system.Entity.Supplier;
import com.ims.inventory_management_system.Enum.SupplierStatus;
import com.ims.inventory_management_system.Exception.ResourceNotFoundException;
import com.ims.inventory_management_system.Exception.UserAlreadyExistsException;
import com.ims.inventory_management_system.Repository.SupplierRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierServiceImpl implements SupplierService{

    private final SupplierRepository supplierRepo;

    private SupplierDTO convertToDto(Supplier supplier){
        if(supplier == null)  return null;
        SupplierDTO dto = new SupplierDTO();
        dto.setId(supplier.getId());
        dto.setName(supplier.getName());
        dto.setContactPerson(supplier.getContactPerson());
        dto.setEmail(supplier.getEmail());
        dto.setPhone(supplier.getPhone());
        dto.setAddress(supplier.getAddress());
        dto.setPaymentTerms(supplier.getPaymentTerms());
        dto.setWebsite(supplier.getWebsite());
        dto.setGstin(supplier.getGstin());
        dto.setStatus(supplier.getStatus().name());
        return dto;
    }

    @Override
    public List<SupplierDTO> getAllSuppliers() {

        return supplierRepo.findAll().stream().map(this::convertToDto).collect(Collectors.toList());

    }

    @Override
    public SupplierDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(MessageConstants.USER_NOT_FOUND_BY_ID, id)));
        return convertToDto(supplier);
    }

    @Override
    public SupplierDTO createSupplier(CreateSupplierRequest request) {
        if(supplierRepo.findByEmail(request.getEmail()).isPresent()){
            throw new UserAlreadyExistsException(MessageConstants.EMAIL_ALREADY_IN_USE);
        }
        if(supplierRepo.findByName(request.getName()).isPresent()){
            throw new UserAlreadyExistsException(MessageConstants.USERNAME_ALREADY_TAKEN);
        }
        if(supplierRepo.findByGstin(request.getGstin()).isPresent()){
            throw new UserAlreadyExistsException("Supplier GSTIN '" + request.getGstin() + "' already exists.");
        }
        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setAddress(request.getAddress());
        supplier.setPaymentTerms(request.getPaymentTerms());
        supplier.setWebsite(request.getWebsite());
        supplier.setGstin(request.getGstin());
        supplier.setStatus(SupplierStatus.ACTIVE); // Default new suppliers to ACTIVE

        Supplier savedSupplier = supplierRepo.save(supplier);
        return convertToDto(savedSupplier);

    }

    @Override
    public SupplierDTO updateSupplier(Long id, UpdateSupplierRequest request) {
        Supplier existingSupplier = supplierRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(MessageConstants.USER_NOT_FOUND_BY_ID, id)));
        Optional<Supplier> supplierWithNewEmail = supplierRepo.findByEmail(request.getEmail());

        // Check if the new email is already used by another supplier
        if (supplierWithNewEmail.isPresent() && !supplierWithNewEmail.get().getId().equals(id)) {
            throw new UserAlreadyExistsException(MessageConstants.EMAIL_ALREADY_IN_USE);
        }
        existingSupplier.setContactPerson(request.getContactPerson());
        existingSupplier.setEmail(request.getEmail());
        existingSupplier.setPhone(request.getPhone());
        existingSupplier.setAddress(request.getAddress());
        existingSupplier.setPaymentTerms(request.getPaymentTerms());
        existingSupplier.setWebsite(request.getWebsite());

        Supplier updatedSupplier = supplierRepo.save(existingSupplier);
        return convertToDto(updatedSupplier);
    }

    @Override
    public void deactivateSupplier(Long id) {

        Supplier supplier = supplierRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(MessageConstants.USER_NOT_FOUND_BY_ID,id)));

        supplier.setStatus(SupplierStatus.DEACTIVE);
        supplierRepo.save(supplier);
    }

    @Override
    public void activateSupplier(Long id) {

        Supplier supplier = supplierRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(MessageConstants.USER_NOT_FOUND_BY_ID, id)));

        supplier.setStatus(SupplierStatus.ACTIVE);
        supplierRepo.save(supplier);
    }

    

}
