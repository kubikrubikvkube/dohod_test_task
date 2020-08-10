package ru.dohod.testtask.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dohod.testtask.entity.Client;
import ru.dohod.testtask.repository.ClientRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(@Autowired ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    public List<Client> getAllClients() {
        Iterable<Client> allClientsIterable = clientRepository.findAll();
        List<Client> allClientsList = IterableUtils.toList(allClientsIterable);
        log.debug("Got {} clients from repository", allClientsList.size());
        return allClientsList;
    }

    public Client save(@NonNull Client client) {
        log.info("Got {} client to be saved", client);
        if (client.getAddresses() == null) {
            client.setAddresses(Collections.emptyList());
        }
        Client savedClient = clientRepository.save(client);
        log.debug("Saved client {}", savedClient);
        return savedClient;
    }

    public void delete(@NonNull Long clientId) {
        log.debug("Got deletion request for clientId {}", clientId);
        clientRepository.deleteById(clientId);
        log.debug("Deleted client by clientId {}", clientId);
    }

    public Optional<Client> findById(@NonNull Long clientId) {
        return clientRepository.findById(clientId);
    }

    public void deleteAll() {
        clientRepository.deleteAll();
    }
}
