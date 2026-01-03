package com.ims.inventory_management_system.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ims.inventory_management_system.Entity.PurchaseOrder;
import com.ims.inventory_management_system.Enum.OrderStatus;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Long>{

    List<PurchaseOrder> findBySupplierId(Long supplierId);
    List<PurchaseOrder> findByOrderStatus(OrderStatus status);
    
}
