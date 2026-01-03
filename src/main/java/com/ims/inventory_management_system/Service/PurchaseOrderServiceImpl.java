package com.ims.inventory_management_system.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ims.inventory_management_system.Constants.MessageConstants;
import com.ims.inventory_management_system.DTO.ApiResponse;
import com.ims.inventory_management_system.DTO.CreatePurchaseOrderRequest;
import com.ims.inventory_management_system.DTO.PurchaseOrderDTO;
import com.ims.inventory_management_system.DTO.PurchaseOrderItemDTO;
import com.ims.inventory_management_system.DTO.PurchaseOrderItemRequest;
import com.ims.inventory_management_system.DTO.UpdatePurchaseOrderRequest;
import com.ims.inventory_management_system.Entity.GoodsReceiptNote;
import com.ims.inventory_management_system.Entity.Product;
import com.ims.inventory_management_system.Entity.PurchaseOrder;
import com.ims.inventory_management_system.Entity.PurchaseOrderItem;
import com.ims.inventory_management_system.Entity.Supplier;
import com.ims.inventory_management_system.Enum.OrderStatus;
import com.ims.inventory_management_system.Exception.BadRequestException;
import com.ims.inventory_management_system.Exception.ResourceNotFoundException;
import com.ims.inventory_management_system.Repository.GoodsReceiptNoteRepository;
import com.ims.inventory_management_system.Repository.ProductRepository;
import com.ims.inventory_management_system.Repository.PurchaseOrderItemRepository;
import com.ims.inventory_management_system.Repository.PurchaseOrderRepository;
import com.ims.inventory_management_system.Repository.SupplierRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService{

    private final PurchaseOrderRepository purchaseOrderRepo;
    private final PurchaseOrderItemRepository purchaseOrderItemRepo;
    private final ProductRepository productRepo;
    private final SupplierRepository supplierRepo;
    private final GoodsReceiptNoteRepository goodsReceiptNoteRepo;
    private final ProductService productService;

    private PurchaseOrderDTO convertToDto(PurchaseOrder purchaseOrder){
        PurchaseOrderDTO dto = new PurchaseOrderDTO();
        dto.setId(purchaseOrder.getId());
       dto.setSupplierName(purchaseOrder.getSupplier().getName());
       dto.setSupplierId(purchaseOrder.getSupplier().getId());
        dto.setOrderDate(purchaseOrder.getOrderDate());
        dto.setStatus(purchaseOrder.getOrderStatus().name());
        dto.setTotalAmount(purchaseOrder.getTotalAmount());
        List<PurchaseOrderItemDTO> itemDTOs = purchaseOrder.getOrderItems().stream().map( item -> {
            PurchaseOrderItemDTO itemDTO = new PurchaseOrderItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setProductName(item.getProduct().getName());
            itemDTO.setProductSku(item.getProduct().getSku());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPricePerUnit(item.getPricePerUnit());
            return itemDTO;
        }).collect(Collectors.toList());
        dto.setOrderItems(itemDTOs);
        return dto;
    }

    @Override
    public PurchaseOrderDTO createPurchaseOrder(CreatePurchaseOrderRequest request){
        Supplier supplier = supplierRepo.findById(request.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(MessageConstants.USER_NOT_FOUND_BY_ID,request.getSupplierId())));
        //Create a purchaseOrder(parent)
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setOrderDate(request.getOrderDate());
        purchaseOrder.setOrderStatus(OrderStatus.PENDING);

        // Process the Items
        List<PurchaseOrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for(PurchaseOrderItemRequest itemRequest : request.getItems()){
            Product product = productRepo.findById(itemRequest.getProdId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(MessageConstants.USER_NOT_FOUND_BY_ID,itemRequest.getProdId())));

            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setProduct(product);
            item.setPricePerUnit(itemRequest.getPricePerUnit());
            item.setQuantity(itemRequest.getQuantity());
            item.setPurchaseOrder(purchaseOrder);

            //Calculate Item cost = quantity * Price
            BigDecimal itemTotalAmount = itemRequest.getPricePerUnit().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(itemTotalAmount);
            orderItems.add(item);

        } 

        //Set Items and TotalAmount to parent
        purchaseOrder.setOrderItems(orderItems);
        purchaseOrder.setTotalAmount(totalAmount);

        PurchaseOrder savedOrder = purchaseOrderRepo.save(purchaseOrder);
        return convertToDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderDTO> getAllPurchaseOrders(){
        List<PurchaseOrderDTO> allOrders =  purchaseOrderRepo.findAll()
                    .stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        return allOrders;
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderDTO getPurchaseOrderById(Long id){
        PurchaseOrder purchaseOrder = purchaseOrderRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Purchase Order found with this id : " + id));
        return convertToDto(purchaseOrder);
    }

    //Checks Order Status for update/cancel order
    private PurchaseOrder getOrderIfPending(Long id) {
        PurchaseOrder order = purchaseOrderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with id: " + id));

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Order status is already " + order.getOrderStatus().name() + ". Only PENDING orders can be modified or canceled.");
        }
        return order;
    }

    @Override
    public PurchaseOrderDTO updatePurchaseOrder(Long id, UpdatePurchaseOrderRequest request){
        PurchaseOrder existingOrder = getOrderIfPending(id);

        purchaseOrderItemRepo.deleteAll(existingOrder.getOrderItems());
        existingOrder.getOrderItems().clear();
        
        List<PurchaseOrderItem> newItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for(PurchaseOrderItemRequest itemRequest : request.getItems()){
            Product product = productRepo.findById(itemRequest.getProdId()).orElseThrow(() -> new ResourceNotFoundException(String.format(MessageConstants.PRODUCT_NOT_FOUND,itemRequest.getProdId())));
            
            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setPricePerUnit(itemRequest.getPricePerUnit());
            item.setPurchaseOrder(existingOrder);

            BigDecimal itemAmount = itemRequest.getPricePerUnit().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(itemAmount);
            newItems.add(item);
        }
            existingOrder.setOrderDate(request.getOrderDate());
            existingOrder.setOrderItems(newItems);
            existingOrder.setTotalAmount(totalAmount);

            PurchaseOrder updatedOrder = purchaseOrderRepo.save(existingOrder);
            return convertToDto(updatedOrder);
    }

    @Override
    public ApiResponse cancelPurchaseOrder(Long id){
        PurchaseOrder order = getOrderIfPending(id);
        order.setOrderStatus(OrderStatus.CANCELLED);
        purchaseOrderRepo.save(order);
        String message = String.format(MessageConstants.PURCHASE_ORDER_CANCELLED, id);
        return new ApiResponse(message, true);
        
    }

    @Override
    public ApiResponse receiveGoods(Long id) {
        PurchaseOrder order = getOrderIfPending(id);

        //Increase Stock
        for (PurchaseOrderItem item : order.getOrderItems()) {
            productService.increaseStock(item.getProduct().getId(), item.getQuantity());
        }

        // 2. CREATE THE GRN AUDIT RECORD
        GoodsReceiptNote grn = new GoodsReceiptNote();
        grn.setPurchaseOrder(order);
        grn.setReceiptTimestamp(LocalDateTime.now());
        grn.setReceivedBy(order.getSupplier().getName()); 
        grn.setReceiptReference("GRN-" + order.getId() + "-" + System.currentTimeMillis());
        
        goodsReceiptNoteRepo.save(grn);

        order.setOrderStatus(OrderStatus.COMPLETED);
        purchaseOrderRepo.save(order);
        
        String message = String.format(MessageConstants.PURCHASE_ORDER_RECEIVED, 
                                        id, grn.getReceiptReference());
        return new ApiResponse(message, true);
    }

}
