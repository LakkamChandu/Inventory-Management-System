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

import com.ims.inventory_management_system.DTO.ApiResponse;
import com.ims.inventory_management_system.DTO.CreatePurchaseOrderRequest;
import com.ims.inventory_management_system.DTO.PurchaseOrderDTO;
import com.ims.inventory_management_system.DTO.UpdatePurchaseOrderRequest;
import com.ims.inventory_management_system.Service.PurchaseOrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @PostMapping("/create-order")
    @PreAuthorize("hasRole('ADMIN', 'USER')")
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@Valid @RequestBody CreatePurchaseOrderRequest request){
        PurchaseOrderDTO purchaseOrder = purchaseOrderService.createPurchaseOrder(request);
        return new ResponseEntity<>(purchaseOrder, HttpStatus.CREATED);
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<PurchaseOrderDTO>> getAllPurchaseOrders(){
        List<PurchaseOrderDTO> allPurchaseOrders =  purchaseOrderService.getAllPurchaseOrders();
        return ResponseEntity.ok(allPurchaseOrders);

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<PurchaseOrderDTO> getPurchaseOrderById(@PathVariable Long id){
        PurchaseOrderDTO order = purchaseOrderService.getPurchaseOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/update-order/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<PurchaseOrderDTO> updatePurchaseOrder(@PathVariable Long id, @Valid @RequestBody UpdatePurchaseOrderRequest request) {
        PurchaseOrderDTO updatedOrder = purchaseOrderService.updatePurchaseOrder(id, request);
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/cancel-order/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> cancelPurchaseOrder(@PathVariable Long id){
        ApiResponse response = purchaseOrderService.cancelPurchaseOrder(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/receive-goods/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse> receiveGoods(@PathVariable Long id) {
        ApiResponse response = purchaseOrderService.receiveGoods(id);
        return ResponseEntity.ok(response);
    }
}
