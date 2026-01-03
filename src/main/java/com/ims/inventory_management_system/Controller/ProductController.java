package com.ims.inventory_management_system.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ims.inventory_management_system.DTO.AddProductRequest;
import com.ims.inventory_management_system.DTO.ProductDTO;
import com.ims.inventory_management_system.DTO.UpdateProductRequest;
import com.ims.inventory_management_system.Service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService prodService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/get-products")
    public ResponseEntity<List<ProductDTO>> getAllProducts(){
        List<ProductDTO> productDTOs = prodService.getAllProducts();
        return ResponseEntity.ok(productDTOs);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/get-products/{prodId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long prodId){
        ProductDTO productDto = prodService.getProductById(prodId);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody AddProductRequest request){
        ProductDTO productDto = prodService.addProduct(request);
        return new ResponseEntity<>(productDto,HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-product/{prodId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long prodId, @Valid @RequestBody UpdateProductRequest request){
        ProductDTO updatedProd = prodService.updateProduct(prodId, request);
        return ResponseEntity.ok(updatedProd);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-product/{prodId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long prodId){
        prodService.deleteProduct(prodId);
        return ResponseEntity.status(HttpStatus.OK).body("Product Deleted Successfully");
    }

    @PostMapping("/{id}/increase-stock")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ProductDTO> increaseProductStock(@PathVariable Long id,@RequestParam int quantity) {
        ProductDTO updatedProductDto = prodService.increaseStock(id, quantity);
        return ResponseEntity.ok(updatedProductDto);
    }

    @PostMapping("/{id}/decrease-stock")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ProductDTO> decreaseProductStock(@PathVariable Long id, @RequestParam int quantity) {
        ProductDTO updatedProductDto = prodService.decreaseStock(id, quantity);
        return ResponseEntity.ok(updatedProductDto);
    }

    @GetMapping("/reports/reorder-list")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ProductDTO>> getReorderList() {
        List<ProductDTO> reorderList = prodService.getReorderList();
        return ResponseEntity.ok(reorderList);
    }

}
