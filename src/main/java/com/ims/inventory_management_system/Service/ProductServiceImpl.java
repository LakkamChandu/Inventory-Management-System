package com.ims.inventory_management_system.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ims.inventory_management_system.Constants.MessageConstants;
import com.ims.inventory_management_system.DTO.AddProductRequest;
import com.ims.inventory_management_system.DTO.ProductDTO;
import com.ims.inventory_management_system.DTO.UpdateProductRequest;
import com.ims.inventory_management_system.Entity.Product;
import com.ims.inventory_management_system.Exception.BadRequestException;
import com.ims.inventory_management_system.Exception.ResourceNotFoundException;
import com.ims.inventory_management_system.Exception.UserAlreadyExistsException;
import com.ims.inventory_management_system.Repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class ProductServiceImpl implements ProductService{

   private final ProductRepository productRepo;

   private ProductDTO convertToDto(Product product){
    ProductDTO dto = new ProductDTO();
    dto.setId(product.getId());
    dto.setSku(product.getSku());
    dto.setName(product.getName());
    dto.setDescription(product.getDescription());
    dto.setPrice(product.getPrice());
    dto.setQuantityInStock(product.getQuantityInStock());
    dto.setReorderLevel(product.getReorderLevel());
    return dto;
   }
    
   @Override 
   public List<ProductDTO> getAllProducts(){
        return productRepo.findAll()
        .stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Long prodId){
        Product prod = productRepo.findById(prodId).orElseThrow(()-> new ResourceNotFoundException(String.format(MessageConstants.PRODUCT_NOT_FOUND,prodId)));
        return convertToDto(prod);
    }

    @Override
    public ProductDTO addProduct(AddProductRequest request){
        productRepo.findBySku(request.getSku()).ifPresent(existingProduct -> {
            throw new UserAlreadyExistsException("Product with SKU " + request.getSku()+ " already exists.");
        });

        if(request.getQuantityInStock() == null){
            request.setQuantityInStock(0);
        }
        Product product = new Product();
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantityInStock(request.getQuantityInStock());
        Product SavedProduct = productRepo.save(product);
        return convertToDto(SavedProduct);
    }

    @Override
    public ProductDTO updateProduct(Long prodId, UpdateProductRequest request){
        Product existingProd = productRepo.findById(prodId).orElseThrow(() -> new ResourceNotFoundException(String.format(MessageConstants.PRODUCT_NOT_FOUND,prodId)));
        existingProd.setName(request.getName());
        existingProd.setPrice(request.getPrice());
        existingProd.setDescription(request.getDescription());

        Product updatedProduct = productRepo.save(existingProd);
        return convertToDto(updatedProduct);
    }

    @Override
    public void deleteProduct(Long prodId){
        Product prod = productRepo.findById(prodId).orElseThrow(() -> new ResourceNotFoundException(String.format(MessageConstants.PRODUCT_NOT_FOUND,prodId)));
        productRepo.delete(prod);
    }

    @Override
    public ProductDTO increaseStock(Long id, int quantity) {
        if(quantity <= 0){
            throw new BadRequestException("Quantity to increase must be positive.");
        }
        Product prod = productRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(MessageConstants.PRODUCT_NOT_FOUND,id)));
        prod.setQuantityInStock(prod.getQuantityInStock() + quantity);
        Product updatedProd = productRepo.save(prod);
        return convertToDto(updatedProd);
    }

    @Override
    public ProductDTO decreaseStock(Long id, int quantity) {
        if(quantity <= 0){
            throw new BadRequestException("Quantity to increase must be positive.");
        }
        Product prod = productRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(MessageConstants.PRODUCT_NOT_FOUND,id)));

        if (prod.getQuantityInStock() < quantity) {
            throw new BadRequestException("Cannot decrease stock by " + quantity + ". Only " + prod.getQuantityInStock() + " items available for product SKU: " + prod.getSku());
        }
        prod.setQuantityInStock(prod.getQuantityInStock() - quantity);
        Product updatedProd = productRepo.save(prod);
        return convertToDto(updatedProd);
    }

    @Override
    public List<ProductDTO> getReorderList(){
        return productRepo.findAll().stream()
        .filter(p -> p.getQuantityInStock() <= p.getReorderLevel())
        .map(this::convertToDto)
        .collect(Collectors.toList());
    }
    
}
