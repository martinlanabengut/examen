package ar.com.plug.examen.app.rest;

import ar.com.plug.examen.domain.model.Seller;
import ar.com.plug.examen.domain.service.SellerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sellers")
public class SellerController {
    private static final Logger logger = LoggerFactory.getLogger(SellerController.class);

    @Autowired
    private SellerService sellerService;

    /**
     * Create a new seller.
     *
     * @param seller the seller to create
     * @return the created seller
     */
    @ApiOperation(value = "Create a new seller", response = Seller.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created seller"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) {
        logger.info("Received request to create seller: {}", seller);
        try {
            Seller createdSeller = sellerService.createSeller(seller);
            logger.info("Seller created successfully: {}", createdSeller);
            return ResponseEntity.ok(createdSeller);
        } catch (Exception e) {
            logger.error("Error creating seller: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get all sellers.
     *
     * @return the list of all sellers
     */
    @ApiOperation(value = "View a list of available sellers", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers() {
        logger.info("Received request to get all sellers");
        try {
            List<Seller> sellers = sellerService.getAllSellers();
            logger.info("Retrieved {} sellers", sellers.size());
            return ResponseEntity.ok(sellers);
        } catch (Exception e) {
            logger.error("Error retrieving sellers: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get seller by ID.
     *
     * @param id the ID of the seller to retrieve
     * @return the seller with the specified ID
     */
    @ApiOperation(value = "Get a seller by Id", response = Seller.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved seller"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) {
        logger.info("Received request to get seller by id: {}", id);
        try {
            return sellerService.getSellerById(id)
                    .map(seller -> {
                        logger.info("Seller retrieved successfully: {}", seller);
                        return ResponseEntity.ok(seller);
                    })
                    .orElseGet(() -> {
                        logger.warn("Seller with id {} not found", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            logger.error("Error retrieving seller by id: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Update an existing seller.
     *
     * @param id the ID of the seller to update
     * @param sellerDetails the new details of the seller
     * @return the updated seller
     */
    @ApiOperation(value = "Update an existing seller", response = Seller.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated seller"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Seller> updateSeller(@PathVariable Long id, @RequestBody Seller sellerDetails) {
        logger.info("Received request to update seller with id: {}", id);
        try {
            Seller updatedSeller = sellerService.updateSeller(id, sellerDetails);
            logger.info("Seller updated successfully: {}", updatedSeller);
            return ResponseEntity.ok(updatedSeller);
        } catch (Exception e) {
            logger.error("Error updating seller: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Delete a seller.
     *
     * @param id the ID of the seller to delete
     * @return a response entity with a success message
     */
    @ApiOperation(value = "Delete a seller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted seller"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSeller(@PathVariable Long id) {
        logger.info("Received request to delete seller with id: {}", id);
        try {
            sellerService.deleteSeller(id);
            logger.info("Seller deleted successfully");
            return ResponseEntity.ok("Seller deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting seller: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error deleting seller: " + e.getMessage());
        }
    }
}
