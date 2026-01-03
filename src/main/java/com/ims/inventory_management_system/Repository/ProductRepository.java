package com.ims.inventory_management_system.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ims.inventory_management_system.Entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>{

    Optional<Product> findBySku(String sku);

    List<Product> findByQuantityInStockLessThanEqual(Integer reorderLevel);
    
}
