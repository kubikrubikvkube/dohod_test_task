package ru.dohod.testtask.address;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.dohod.testtask.entity.Address;

import java.net.URI;

@Component
public class AddressTestHelper {
    private final TestRestTemplate restTemplate;

    public AddressTestHelper(@Autowired TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SneakyThrows
    public ResponseEntity<Address> addAddress(URI endpoint, Long clientId, String addressName) {
        var addAddressEndpoint = new URI(endpoint + "/clientId/" + clientId);
        String createAddressJsonRequest = String.format("{\"name\": \"%s\"}", addressName);
        RequestEntity<String> requestEntity = RequestEntity.post(addAddressEndpoint).contentType(MediaType.APPLICATION_JSON).body(createAddressJsonRequest);
        return restTemplate.exchange(requestEntity, Address.class);
    }

    @SneakyThrows
    public void deleteAddress(URI endpoint, Long addressId) {
        var deleteAddressEndpoint = new URI(endpoint + "/id/" + addressId);
        restTemplate.delete(deleteAddressEndpoint);
    }

    @SneakyThrows
    public ResponseEntity<Address[]> getAddressesForClient(URI endpoint, Long clientId) {
        var getAddressEndpoint = new URI(endpoint + "/clientId/" + clientId);
        RequestEntity<Void> requestEntity = RequestEntity.get(getAddressEndpoint).accept(MediaType.APPLICATION_JSON).build();
        return restTemplate.exchange(requestEntity, Address[].class);
    }
}
