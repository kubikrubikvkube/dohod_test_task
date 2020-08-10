package ru.dohod.testtask.client;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.dohod.testtask.entity.Client;

import java.net.URI;
import java.util.Random;

@Component
public class ClientTestHelper {
    private final TestRestTemplate restTemplate;

    public ClientTestHelper(@Autowired TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    ResponseEntity<Client> createClient(URI endpoint, String name) {
        String createClientJsonRequest = String.format("{\"name\": \"%s\"}", name);
        RequestEntity<String> requestEntity = RequestEntity.post(endpoint).contentType(MediaType.APPLICATION_JSON).body(createClientJsonRequest);
        return restTemplate.exchange(requestEntity, Client.class);
    }

    ResponseEntity<Client[]> getAllClients(URI endpoint) {
        var requestListRequest = RequestEntity.get(endpoint).accept(MediaType.APPLICATION_JSON).build();
        return restTemplate.exchange(requestListRequest, Client[].class);
    }

    @SneakyThrows
    void deleteClient(URI endpoint, Long clientId) {
        var deleteEndpoint = new URI(endpoint + "/id/" + clientId);
        restTemplate.delete(deleteEndpoint);
    }

    @SneakyThrows
    ResponseEntity<Client[]> getClientByNameSubstring(URI endpoint, String nameSubstring) {
        URI requestUri = new URI(endpoint + "/name/" + nameSubstring);
        var request = RequestEntity.get(requestUri).accept(MediaType.APPLICATION_JSON).build();
        return restTemplate.exchange(request, Client[].class);
    }

    String randomName(int targetStringLength) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    String randomName() {
        return randomName(10);
    }
}
