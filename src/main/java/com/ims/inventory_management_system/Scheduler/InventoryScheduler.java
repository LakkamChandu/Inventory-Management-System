package com.ims.inventory_management_system.Scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ims.inventory_management_system.DTO.ProductDTO;
import com.ims.inventory_management_system.Service.ProductService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryScheduler {

    private final ProductService productService;

    //Runs everyday at 2:00 AM
    @Scheduled(cron = "0 0 2 * * * ")
    public void checkLowStockAndAlert(){

        List<ProductDTO> lowStockItems =  productService.getReorderList();

        if (!lowStockItems.isEmpty()) {
            // Log the alert for the Admin/Manager to see in the server logs
            System.err.println("----- CRITICAL LOW STOCK ALERT: " + LocalDateTime.now() + " -----");
            System.err.println("The following " + lowStockItems.size() + " products need reordering:");

            lowStockItems.forEach(p -> {
                System.err.printf("  ~ SKU: %s | Name: %s | Stock: %d / Reorder: %d\n",
                        p.getSku(), p.getName(), p.getQuantityInStock(), p.getReorderLevel());
            });
            System.err.println("-----END ALERT. Please check /api/products/reports/reorder-list-----");
        } else {
            System.out.println("Inventory check run successfully. All core stock levels are sufficient.");
        }

    }
}
