package ar.com.plug.examen.domain.service;

import ar.com.plug.examen.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Client createClient(Client client);
    List<Client> getAllClients();
    Optional<Client> getClientById(Long id);
    Client updateClient(Long id, Client clientDetails);
    void deleteClient(Long id);
}
