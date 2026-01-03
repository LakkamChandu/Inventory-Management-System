package com.ims.inventory_management_system.Service;

import java.util.List;

import com.ims.inventory_management_system.DTO.AddProductRequest;
import com.ims.inventory_management_system.DTO.ProductDTO;
import com.ims.inventory_management_system.DTO.UpdateProductRequest;

public interface ProductService {

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(Long id);

    ProductDTO addProduct(AddProductRequest request);

    ProductDTO updateProduct(Long id, UpdateProductRequest request);

    void deleteProduct(Long id);

    ProductDTO increaseStock(Long id, int quantity);

    ProductDTO decreaseStock(Long id, int quantity);

    List<ProductDTO> getReorderList();
}
