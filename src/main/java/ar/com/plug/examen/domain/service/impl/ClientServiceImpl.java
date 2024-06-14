package ar.com.plug.examen.domain.service.impl;

import ar.com.plug.examen.domain.model.Client;
import ar.com.plug.examen.domain.repository.ClientRepository;
import ar.com.plug.examen.domain.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client createClient(Client client) {
        logger.debug("Creating client with details: {}", client);
        try {
            Client savedClient = clientRepository.save(client);
            logger.debug("Client created successfully: {}", savedClient);
            return savedClient;
        } catch (Exception e) {
            logger.error("Error creating client: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Client> getAllClients() {
        logger.debug("Retrieving all clients");
        try {
            List<Client> clients = clientRepository.findAll();
            logger.debug("Retrieved {} clients", clients.size());
            return clients;
        } catch (Exception e) {
            logger.error("Error retrieving clients: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Optional<Client> getClientById(Long id) {
        logger.debug("Retrieving client by id: {}", id);
        try {
            Optional<Client> client = clientRepository.findById(id);
            client.ifPresent(c -> logger.debug("Client retrieved successfully: {}", c));
            return client;
        } catch (Exception e) {
            logger.error("Error retrieving client by id: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Client updateClient(Long id, Client clientDetails) {
        logger.debug("Updating client with id: {}", id);
        try {
            Client client = clientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Client not found"));
            client.setName(clientDetails.getName());
            client.setEmail(clientDetails.getEmail());
            Client updatedClient = clientRepository.save(client);
            logger.debug("Client updated successfully: {}", updatedClient);
            return updatedClient;
        } catch (Exception e) {
            logger.error("Error updating client: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteClient(Long id) {
        logger.debug("Deleting client with id: {}", id);
        try {
            clientRepository.deleteById(id);
            logger.debug("Client deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting client: {}", e.getMessage());
            throw e;
        }
    }
}
