package ru.dohod.testtask.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dohod.testtask.entity.Address;
import ru.dohod.testtask.entity.Client;
import ru.dohod.testtask.exception.ClientNotFoundException;
import ru.dohod.testtask.repository.AddressRepository;
import ru.dohod.testtask.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class AddressService {
    private final AddressRepository addressRepository;
    private final ClientRepository clientRepository;

    public AddressService(@Autowired AddressRepository addressRepository,
                          @Autowired ClientRepository clientRepository) {
        this.addressRepository = addressRepository;
        this.clientRepository = clientRepository;
    }


    public List<Address> getAllAddresses() {
        Iterable<Address> allAddresses = addressRepository.findAll();
        List<Address> allAddressesList = IterableUtils.toList(allAddresses);
        log.info("Got {} addresses from repository", allAddressesList.size());
        return allAddressesList;
    }

    public Address save(Long clientId, Address address) throws ClientNotFoundException {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        Client client = clientOpt.orElseThrow(() -> new ClientNotFoundException("Client with id '%d' not found", clientId));
        address.setClient(client);
        return addressRepository.save(address);
    }

    public void delete(Long id) {
        addressRepository.deleteById(id);
    }

    public void deleteAll() {
        addressRepository.deleteAll();
    }
}
