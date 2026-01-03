package com.ims.inventory_management_system.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "goods_receipt_notes")
@Data
public class GoodsReceiptNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // This reference number makes the GRN an official, easily traceable document.
    @Column(nullable = false, unique = true, length = 50)
    private String receiptReference;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id", unique = true, nullable = false)
    private PurchaseOrder purchaseOrder;

    @Column(nullable = false)
    private LocalDateTime receiptTimestamp;

    //Name of employee who takes order
    @Column(length = 100)
    private String receivedBy;
}