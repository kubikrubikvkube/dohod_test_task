package ru.dohod.testtask.assertions;

import org.assertj.core.api.AbstractAssert;
import ru.dohod.testtask.entity.Address;

public class AddressAssert extends AbstractAssert<AddressAssert, Address> {
    public AddressAssert(Address address, Class<?> selfType) {
        super(address, selfType);
    }

    public AddressAssert(Address actual) {
        super(actual, AddressAssert.class);
    }

    public AddressAssert hasId(Integer expectedId) {
        isNotNull();
        Long actualId = actual.getId();
        Long expectedIdAsLong = Long.valueOf(expectedId);
        if (!actualId.equals(expectedIdAsLong)) {
            failWithMessage("Wrong 'id' found,expected: %d, found %d", expectedId, actualId);
        }
        return this;
    }

    public AddressAssert hasName(String expectedName) {
        isNotNull();
        var actualName = actual.getName();
        if (!actualName.equals(expectedName)) {
            failWithMessage("Wrong 'name' found,expected: %s, found %s", expectedName, actualName);
        }
        return this;
    }

    public AddressAssert hasValidId() {
        isNotNull();
        Long actualId = actual.getId();
        if (actualId == null || actualId <= 0) {
            failWithMessage("Wrong 'id' found,expected: != null && > 0, found %d", actualId);
        }
        return this;
    }
}
