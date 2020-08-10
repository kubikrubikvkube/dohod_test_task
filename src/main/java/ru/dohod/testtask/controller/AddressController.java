package ru.dohod.testtask.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.dohod.testtask.entity.Address;
import ru.dohod.testtask.entity.Client;
import ru.dohod.testtask.exception.ClientNotFoundException;
import ru.dohod.testtask.service.AddressService;
import ru.dohod.testtask.service.ClientService;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@Slf4j
@ResponseBody
public class AddressController {
    private final AddressService addressService;
    private final ClientService clientService;

    public AddressController(@Autowired AddressService addressService,
                             @Autowired ClientService clientService) {
        this.addressService = addressService;
        this.clientService = clientService;
    }


    @RequestMapping(value = "/address", method = GET)
    public List<Address> getAllAddresses() {
        return addressService.getAllAddresses();
    }

    @RequestMapping(value = "/address/clientId/{clientId}", method = POST)
    public Address createNewAddress(@PathVariable Long clientId, @RequestBody Address address) throws ClientNotFoundException {
        return addressService.save(clientId, address);
    }

    @RequestMapping(value = "/address/clientId/{clientId}", method = GET)
    public List<Address> getAddressesByClientId(@PathVariable Long clientId) throws ClientNotFoundException {
        Optional<Client> foundClientOptional = clientService.findById(clientId);
        Client client = foundClientOptional.orElseThrow(() -> new ClientNotFoundException("Client with id '%d' is not found", clientId));
        return client.getAddresses();
    }

    @RequestMapping(value = "/address/id/{id}", method = DELETE)
    public void deleteClient(@PathVariable Long id) {
        addressService.delete(id);
    }
}
