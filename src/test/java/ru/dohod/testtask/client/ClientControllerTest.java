package ru.dohod.testtask.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.dohod.testtask.entity.Client;
import ru.dohod.testtask.service.ClientService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static ru.dohod.testtask.assertions.ProjectAssertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ClientControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientTestHelper helper;

    private URI clientsEndpoint;

    @BeforeEach
    void setUp() throws URISyntaxException {
        log.debug("Register endpoint");
        clientsEndpoint = new URI("http://localhost:" + port + "/clients");
    }

    @AfterEach
    void tearDown() {
        clientService.deleteAll();
    }

    @Test
    @DisplayName("Test that Client can be created and present in list using controller")
    void testCanBeCreatedAndFoundInList() {
        log.info("Step 1. Create new client using API");
        var randomName = helper.randomName();
        ResponseEntity<Client> responseEntity = helper.createClient(clientsEndpoint, randomName);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Client client = responseEntity.getBody();
        log.info("Created client {}", client);
        assertThat(client)
                .hasValidId()
                .hasName(randomName)
                .hasNoAddresses();

        log.info("Step 2. Check that this client is present in 'all clients' list");
        ResponseEntity<Client[]> clientsListResponse = helper.getAllClients(clientsEndpoint);
        assertThat(clientsListResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Client> clientsFromList = Arrays.asList(clientsListResponse.getBody());
        assertThat(clientsFromList).hasSize(1);

        log.info("Step 3. Find and check client with name {}", randomName);
        Optional<Client> first = clientsFromList.stream().filter(c -> c.getName().equals(randomName)).findFirst();
        assertThat(first).isPresent();
        assertThat(first.get())
                .hasValidId()
                .hasName(randomName)
                .hasNoAddresses();
    }

    @Test
    @DisplayName("Check that client can be found by substring of it's name")
    void testClientCanBeFoundByNameSubstring() {
        log.info("Step 1. Create client with random name");
        var randomName = helper.randomName();
        ResponseEntity<Client> responseEntity = helper.createClient(clientsEndpoint, randomName);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);


        log.info("Step 2. Find client by name substring");
        ResponseEntity<Client[]> foundClientEntity = helper.getClientByNameSubstring(clientsEndpoint, randomName.substring(0, 5));
        assertThat(foundClientEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Arrays.asList(foundClientEntity.getBody())).hasSize(1);
        Client client = Arrays.asList(foundClientEntity.getBody()).get(0);
        log.info("Found client {}", client);
    }

    @Test
    @DisplayName("Check that client can be added")
    void testClientCanBeAdded() {
        log.info("Step 1. Create client with random name and chekc it's existence");
        var randomName = helper.randomName();
        ResponseEntity<Client> responseEntity = helper.createClient(clientsEndpoint, randomName);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Client client = responseEntity.getBody();
        assertThat(client)
                .hasValidId()
                .hasName(randomName)
                .hasNoAddresses();
    }

    @Test
    @DisplayName("Check that client can be deleted")
    void testClientCanBeDeleted() {
        log.info("Step 1. Create client with random name");
        var randomName = helper.randomName();
        ResponseEntity<Client> clientResponseEntity = helper.createClient(clientsEndpoint, randomName);
        assertThat(clientResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Client client = clientResponseEntity.getBody();
        assertThat(client)
                .hasValidId()
                .hasName(randomName)
                .hasNoAddresses();

        log.info("Step 2. Delete client");
        helper.deleteClient(clientsEndpoint, client.getId());

        log.info("Step 3. Check that this client is deleted");
        ResponseEntity<Client[]> deletedClientResponseEntity = helper.getAllClients(clientsEndpoint);
        assertThat(deletedClientResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Optional<Client> deletedClientOpt = Stream.of(deletedClientResponseEntity.getBody()).filter(e -> e.getId().equals(client.getId())).findAny();
        assertThat(deletedClientOpt).isEmpty();

    }
}
