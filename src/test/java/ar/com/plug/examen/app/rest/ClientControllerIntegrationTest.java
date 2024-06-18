package ar.com.plug.examen.app.rest;

import ar.com.plug.examen.domain.model.Client;
import ar.com.plug.examen.domain.repository.ClientRepository;
import ar.com.plug.examen.domain.repository.ProductRepository;
import ar.com.plug.examen.domain.repository.SellerRepository;
import ar.com.plug.examen.domain.repository.TransactionRepository;
import ar.com.plug.examen.domain.service.ClientService;
import java.util.Arrays;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @MockBean
    private ClientService clientService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        transactionRepository.deleteAll();
        clientRepository.deleteAll();
        productRepository.deleteAll();
        sellerRepository.deleteAll();

    }

    @Test
    public void testCreateClient() throws Exception {
        String clientJson = "{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"}";

        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");

        Mockito.when(clientService.createClient(Mockito.any(Client.class))).thenReturn(client);

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    public void testCreateClientInvalidData() throws Exception {
        String clientJson = "{\"name\":\"\",\"email\":\"invalid-email\"}";

        Mockito.when(clientService.createClient(Mockito.any(Client.class)))
                .thenThrow(new IllegalArgumentException("Invalid client data"));

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllClients() throws Exception {
        Client client1 = new Client();
        client1.setName("John Doe");
        client1.setEmail("john.doe@example.com");

        Client client2 = new Client();
        client2.setName("Jane Doe");
        client2.setEmail("jane.doe@example.com");

        clientRepository.saveAll(Arrays.asList(client1, client2));

        Mockito.when(clientService.getAllClients()).thenReturn(Arrays.asList(client1, client2));

        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"))
                .andExpect(jsonPath("$[1].email").value("jane.doe@example.com"));
    }

    @Test
    public void testGetClientById() throws Exception {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");

        client = clientRepository.save(client);

        Mockito.when(clientService.getClientById(client.getId())).thenReturn(Optional.of(client));

        mockMvc.perform(get("/clients/{id}", client.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    public void testGetClientByIdNotFound() throws Exception {
        Long clientId = 999L;

        Mockito.when(clientService.getClientById(clientId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/clients/{id}", clientId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateClient() throws Exception {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");

        client = clientRepository.save(client);

        String updatedClientJson = "{\"name\":\"Updated John\",\"email\":\"updated.john@example.com\"}";

        Client updatedClient = new Client();
        updatedClient.setId(client.getId());
        updatedClient.setName("Updated John");
        updatedClient.setEmail("updated.john@example.com");

        Mockito.when(clientService.updateClient(Mockito.eq(client.getId()), Mockito.any(Client.class)))
                .thenReturn(updatedClient);

        mockMvc.perform(put("/clients/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedClientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated John"))
                .andExpect(jsonPath("$.email").value("updated.john@example.com"));
    }

    @Test
    public void testUpdateClientInvalidData() throws Exception {
        Long clientId = 1L;
        String updatedClientJson = "{\"name\":\"\",\"email\":\"invalid-email\"}";

        Mockito.when(clientService.updateClient(Mockito.eq(clientId), Mockito.any(Client.class)))
                .thenThrow(new IllegalArgumentException("Invalid client data"));

        mockMvc.perform(put("/clients/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedClientJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateClientNotFound() throws Exception {
        Long clientId = 999L;
        String updatedClientJson = "{\"name\":\"Updated John\",\"email\":\"updated.john@example.com\"}";

        Mockito.when(clientService.updateClient(Mockito.eq(clientId), Mockito.any(Client.class)))
                .thenThrow(new NoSuchElementException("Client not found"));

        mockMvc.perform(put("/clients/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedClientJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteClient() throws Exception {
        Client client = new Client();
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");

        client = clientRepository.save(client);

        Mockito.doNothing().when(clientService).deleteClient(client.getId());

        mockMvc.perform(delete("/clients/{id}", client.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Client deleted successfully"));
    }

    @Test
    public void testDeleteClientNotFound() throws Exception {
        Long clientId = 999L;

        Mockito.doThrow(new NoSuchElementException("Client not found")).when(clientService).deleteClient(clientId);

        mockMvc.perform(delete("/clients/{id}", clientId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Error deleting client: Client not found"));
    }
}
