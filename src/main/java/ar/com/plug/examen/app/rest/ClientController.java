package ar.com.plug.examen.app.rest;

import ar.com.plug.examen.domain.model.Client;
import ar.com.plug.examen.domain.service.ClientService;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        logger.info("Received request to delete client with id: {}", id);
        try {
            clientService.deleteClient(id);
            logger.info("Client deleted successfully");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting client: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
