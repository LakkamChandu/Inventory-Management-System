package com.ims.inventory_management_system.Service;

import java.util.List;
import com.ims.inventory_management_system.DTO.CreateSupplierRequest;
import com.ims.inventory_management_system.DTO.SupplierDTO;
import com.ims.inventory_management_system.DTO.UpdateSupplierRequest;

public interface SupplierService {

    List<SupplierDTO> getAllSuppliers();

    SupplierDTO getSupplierById(Long id);

    SupplierDTO createSupplier(CreateSupplierRequest request);

    SupplierDTO updateSupplier(Long id, UpdateSupplierRequest request);

    void deactivateSupplier(Long id);

    void activateSupplier(Long id);

}
