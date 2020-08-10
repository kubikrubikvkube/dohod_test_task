package ru.dohod.testtask.assertions;

import lombok.NonNull;
import org.assertj.core.api.AbstractAssert;
import ru.dohod.testtask.entity.Address;
import ru.dohod.testtask.entity.Client;

import java.util.Collection;

public class ClientAssert extends AbstractAssert<ClientAssert, Client> {
    public ClientAssert(Client client, Class<?> selfType) {
        super(client, selfType);
    }

    public ClientAssert(Client client) {
        super(client, ClientAssert.class);
    }

    public ClientAssert hasId(Integer expectedId) {
        isNotNull();
        Long actualId = actual.getId();
        Long expectedIdAsLong = Long.valueOf(expectedId);
        if (!actualId.equals(expectedIdAsLong)) {
            failWithMessage("Wrong 'id' found,expected: %d, found %d", expectedId, actualId);
        }
        return this;
    }

    public ClientAssert hasValidId() {
        isNotNull();
        Long actualId = actual.getId();
        if (actualId == null || actualId <= 0) {
            failWithMessage("Wrong 'id' found,expected: != null && > 0, found %d", actualId);
        }
        return this;
    }

    public ClientAssert hasName(String expectedName) {
        isNotNull();
        var actualName = actual.getName();
        if (!actualName.equals(expectedName)) {
            failWithMessage("Wrong 'name' found,expected: %s, found %s", expectedName, actualName);
        }
        return this;
    }

    public ClientAssert hasAddresses(@NonNull Collection<Address> expectedAddresses) {
        isNotNull();
        var actualAddresses = actual.getAddresses();
        if (!(expectedAddresses.containsAll(actualAddresses) && actualAddresses.containsAll(expectedAddresses))) {
            failWithMessage("Wrong 'addresses' found,expected: %s, found %s", expectedAddresses, actualAddresses);
        }
        return this;
    }

    public ClientAssert hasNoAddresses() {
        isNotNull();
        var actualAddresses = actual.getAddresses();
        if (actualAddresses != null && !actualAddresses.isEmpty()) {
            failWithMessage("Wrong 'addresses' found,expected null or empty, found %s", actualAddresses);
        }
        return this;
    }
}
