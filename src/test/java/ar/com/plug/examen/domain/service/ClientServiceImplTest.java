package ar.com.plug.examen.domain.service;

import ar.com.plug.examen.domain.model.Client;
import ar.com.plug.examen.domain.repository.ClientRepository;
import ar.com.plug.examen.domain.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    @InjectMocks
    private ClientServiceImpl clientServiceImpl; // Implementation of the service

    private ClientService clientService; // Interface of the service

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        clientService = clientServiceImpl; // Assign the implementation to the interface reference
    }

    @Test
    @DisplayName("Should create a new client successfully")
    void testCreateClient() {
        // Given
        Client client = new Client();
        client.setName("Test Client");

        // When
        when(clientRepository.save(client)).thenReturn(client);
        Client createdClient = clientService.createClient(client);

        // Then
        assertThat(createdClient).isNotNull();
        assertThat(createdClient.getName()).isEqualTo("Test Client");
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    @DisplayName("Should throw exception when creating client fails")
    void testCreateClientThrowsException() {
        // Given
        Client client = new Client();
        client.setName("Test Client");

        // When
        when(clientRepository.save(client)).thenThrow(new RuntimeException("Error creating client"));
        Exception exception = assertThrows(RuntimeException.class, () -> clientService.createClient(client));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error creating client");
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    @DisplayName("Should return all clients")
    void testGetAllClients() {
        // Given
        Client client1 = new Client();
        Client client2 = new Client();
        List<Client> clients = Arrays.asList(client1, client2);

        // When
        when(clientRepository.findAll()).thenReturn(clients);
        List<Client> result = clientService.getAllClients();

        // Then
        assertThat(result).hasSize(2);
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should throw exception when retrieving all clients fails")
    void testGetAllClientsThrowsException() {
        // When
        when(clientRepository.findAll()).thenThrow(new RuntimeException("Error retrieving clients"));
        Exception exception = assertThrows(RuntimeException.class, () -> clientService.getAllClients());

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error retrieving clients");
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return client by ID")
    void testGetClientById() {
        // Given
        Client client = new Client();
        client.setId(1L);

        // When
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        Optional<Client> result = clientService.getClientById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when retrieving client by ID fails")
    void testGetClientByIdThrowsException() {
        // When
        when(clientRepository.findById(1L)).thenThrow(new RuntimeException("Error retrieving client by id"));
        Exception exception = assertThrows(RuntimeException.class, () -> clientService.getClientById(1L));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error retrieving client by id");
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should update client successfully")
    void testUpdateClient() {
        // Given
        Client client = new Client();
        client.setId(1L);
        client.setName("Updated Client");

        Client updatedDetails = new Client();
        updatedDetails.setName("Updated Client");

        // When
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);
        Client updatedClient = clientService.updateClient(1L, updatedDetails);

        // Then
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getName()).isEqualTo("Updated Client");
        verify(clientRepository, times(1)).findById(1L);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    @DisplayName("Should throw exception when updating client fails")
    void testUpdateClientThrowsException() {
        // Given
        Client updatedDetails = new Client();
        updatedDetails.setName("Updated Client");

        // When
        when(clientRepository.findById(1L)).thenThrow(new RuntimeException("Error updating client"));
        Exception exception = assertThrows(RuntimeException.class, () -> clientService.updateClient(1L, updatedDetails));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error updating client");
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should delete client successfully")
    void testDeleteClient() {
        // Given
        long clientId = 1L;

        // When
        clientService.deleteClient(clientId);

        // Then
        verify(clientRepository, times(1)).deleteById(clientId);
    }

    @Test
    @DisplayName("Should throw exception when deleting client fails")
    void testDeleteClientThrowsException() {
        // Given
        long clientId = 1L;

        // When
        doThrow(new RuntimeException("Error deleting client")).when(clientRepository).deleteById(clientId);
        Exception exception = assertThrows(RuntimeException.class, () -> clientService.deleteClient(clientId));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Error deleting client");
        verify(clientRepository, times(1)).deleteById(clientId);
    }
}
