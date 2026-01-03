package com.ims.inventory_management_system.Repository;

import com.ims.inventory_management_system.Entity.GoodsReceiptNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoodsReceiptNoteRepository extends JpaRepository<GoodsReceiptNote, Long> {

    Optional<GoodsReceiptNote> findByPurchaseOrderId(Long purchaseOrderId);
}