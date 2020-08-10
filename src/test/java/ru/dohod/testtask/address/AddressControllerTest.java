package ru.dohod.testtask.address;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.dohod.testtask.entity.Address;
import ru.dohod.testtask.entity.Client;
import ru.dohod.testtask.service.AddressService;
import ru.dohod.testtask.service.ClientService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class AddressControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressTestHelper helper;

    @Autowired
    private ClientService clientService;

    private URI endpoint;
    private Client exampleClient;

    @BeforeEach
    void setUp() throws URISyntaxException {
        log.debug("Register endpoint");
        endpoint = new URI("http://localhost:" + port + "/address");

        log.debug("Create example client");
        exampleClient = clientService.save(new Client("exampleClient"));
    }

    @AfterEach
    void tearDown() {
        addressService.deleteAll();
    }

    @Test
    @DisplayName("Test addresses can be added to client")
    void testAddressCanBeAddedToClient() {
        Long id = exampleClient.getId();
        var addressName = "testAddressName";
        ResponseEntity<Address> responseEntity = helper.addAddress(endpoint, id, addressName);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Address address = responseEntity.getBody();
        assertThat(address).isNotNull();
        assertThat(address.getId()).isGreaterThan(0);
        assertThat(address.getName()).isEqualTo(addressName);
    }

    @Test
    @DisplayName("Test addresses can be deleted by id")
    void testAddressCanBeDeletedById() {
        log.info("Step 1. Add address to client");
        Long id = exampleClient.getId();
        var addressName = "testAddressName";
        ResponseEntity<Address> responseEntity = helper.addAddress(endpoint, id, addressName);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Address address = responseEntity.getBody();
        assertThat(address).isNotNull();
        assertThat(address.getId()).isGreaterThan(0);
        assertThat(address.getName()).isEqualTo(addressName);

        log.info("Step 2. Delete address from client");
        helper.deleteAddress(endpoint, address.getId());

        log.info("Step 3. Check that there are no addresses for this client");
        ResponseEntity<Address[]> addressesForClient = helper.getAddressesForClient(endpoint, id);
        assertThat(addressesForClient.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(addressesForClient.getBody()).isEmpty();
    }

    @Test
    @DisplayName("Address list can be fetched by client id")
    void testAddressesCanBeFetched() {
        log.info("Step 1. Add address to client");
        Long id = exampleClient.getId();
        var addressName1 = "testAddressName1";
        var addressName2 = "testAddressName2";
        ResponseEntity<Address> address1ResponseEntity = helper.addAddress(endpoint, id, addressName1);
        ResponseEntity<Address> address2ResponseEntity = helper.addAddress(endpoint, id, addressName2);

        ResponseEntity<Address[]> addressesForClient = helper.getAddressesForClient(endpoint, id);
        assertThat(addressesForClient.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Address> addresses = Arrays.asList(addressesForClient.getBody());
        assertThat(addresses).containsOnly(address1ResponseEntity.getBody(), address2ResponseEntity.getBody());
    }
}
