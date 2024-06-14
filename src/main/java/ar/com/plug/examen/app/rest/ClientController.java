package ar.com.plug.examen.app.rest;

import ar.com.plug.examen.domain.model.Client;
import ar.com.plug.examen.domain.service.ClientService;
import io.swagger.annotations.Api;
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
@RequestMapping("/clients")
public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private ClientService clientService;

    /**
     * Create a new client.
     *
     * @param client the client to create
     * @return the created client
     */
    @ApiOperation(value = "Create a new client", response = Client.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created client"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        logger.info("Received request to create client: {}", client);
        try {
            Client createdClient = clientService.createClient(client);
            logger.info("Client created successfully: {}", createdClient);
            return ResponseEntity.ok(createdClient);
        } catch (Exception e) {
            logger.error("Error creating client: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get all clients.
     *
     * @return the list of all clients
     */
    @ApiOperation(value = "View a list of available clients", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        logger.info("Received request to get all clients");
        try {
            List<Client> clients = clientService.getAllClients();
            logger.info("Retrieved {} clients", clients.size());
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            logger.error("Error retrieving clients: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get client by ID.
     *
     * @param id the ID of the client to retrieve
     * @return the client with the specified ID
     */
    @ApiOperation(value = "Get a client by Id", response = Client.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved client"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        logger.info("Received request to get client by id: {}", id);
        try {
            return clientService.getClientById(id)
                    .map(client -> {
                        logger.info("Client retrieved successfully: {}", client);
                        return ResponseEntity.ok(client);
                    })
                    .orElseGet(() -> {
                        logger.warn("Client with id {} not found", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            logger.error("Error retrieving client by id: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Update an existing client.
     *
     * @param id the ID of the client to update
     * @param clientDetails the new details of the client
     * @return the updated client
     */
    @ApiOperation(value = "Update an existing client", response = Client.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated client"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client clientDetails) {
        logger.info("Received request to update client with id: {}", id);
        try {
            Client updatedClient = clientService.updateClient(id, clientDetails);
            logger.info("Client updated successfully: {}", updatedClient);
            return ResponseEntity.ok(updatedClient);
        } catch (Exception e) {
            logger.error("Error updating client: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Delete a client.
     *
     * @param id the ID of the client to delete
     * @return a response entity with a success message
     */
    @ApiOperation(value = "Delete a client")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted client"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        logger.info("Received request to delete client with id: {}", id);
        try {
            clientService.deleteClient(id);
            logger.info("Client deleted successfully");
            return ResponseEntity.ok("Client deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting client: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error deleting client: " + e.getMessage());
        }
    }
}
