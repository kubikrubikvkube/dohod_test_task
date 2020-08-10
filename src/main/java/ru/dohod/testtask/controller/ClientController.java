package ru.dohod.testtask.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.dohod.testtask.entity.Client;
import ru.dohod.testtask.service.ClientService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@Slf4j
@ResponseBody
public class ClientController {
    private final ClientService clientService;

    public ClientController(@Autowired ClientService clientService) {
        this.clientService = clientService;
    }

    @RequestMapping(value = "/clients", method = GET)
    public List<Client> getClients(HttpServletRequest httpRequest) {
        List<Client> clientsListFromService = clientService.getAllClients();
        log.info("Provided {} clients from clientService", clientsListFromService.size());
        return clientsListFromService;
    }

    @RequestMapping(value = "/clients/name/{name}", method = GET)
    public List<Client> getClientByName(HttpServletRequest httpRequest, @PathVariable @NonNull String name) {
        List<Client> clientsListFromService = clientService.getAllClients();
        log.info("Provided {} client objects from clientService", clientsListFromService.size());
        List<Client> filteredClients = clientsListFromService
                .stream()
                .filter(e -> e.getName().contains(name))
                .collect(Collectors.toList());
        log.info("After filtering there are {} matching clients", filteredClients.size());
        return filteredClients;
    }

    @RequestMapping(value = "/clients", method = POST)
    public Client createNewClient(@RequestBody Client client) {
        return clientService.save(client);
    }

    @RequestMapping(value = "/clients/id/{clientId}", method = DELETE)
    public void deleteClient(@PathVariable Long clientId) {
        clientService.delete(clientId);
    }
}
