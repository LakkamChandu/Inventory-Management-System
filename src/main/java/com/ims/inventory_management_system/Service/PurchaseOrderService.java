package com.ims.inventory_management_system.Service;

import java.util.List;

import com.ims.inventory_management_system.DTO.ApiResponse;
import com.ims.inventory_management_system.DTO.CreatePurchaseOrderRequest;
import com.ims.inventory_management_system.DTO.PurchaseOrderDTO;
import com.ims.inventory_management_system.DTO.UpdatePurchaseOrderRequest;

public interface PurchaseOrderService {

    PurchaseOrderDTO createPurchaseOrder(CreatePurchaseOrderRequest request);

    List<PurchaseOrderDTO> getAllPurchaseOrders();

    PurchaseOrderDTO getPurchaseOrderById(Long id);
    
    PurchaseOrderDTO updatePurchaseOrder(Long id, UpdatePurchaseOrderRequest request);

    ApiResponse cancelPurchaseOrder(Long id);

    ApiResponse receiveGoods(Long id);

}
