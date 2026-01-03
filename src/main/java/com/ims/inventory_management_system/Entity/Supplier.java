package com.ims.inventory_management_system.Entity;

import com.ims.inventory_management_system.Enum.SupplierStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "suppliers")
@Entity
public class Supplier {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 100)
    private String contactPerson;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 500)
    private String address;

    @Column(length = 100)
    private String paymentTerms;

    @Column(length = 255)
    private String website;

    @Column(nullable = false, unique = true, length = 15)
    private String gstin; // GST Identification Number

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SupplierStatus status;


}
