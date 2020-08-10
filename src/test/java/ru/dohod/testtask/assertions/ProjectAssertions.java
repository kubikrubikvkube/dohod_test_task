package ru.dohod.testtask.assertions;

import org.assertj.core.api.Assertions;
import ru.dohod.testtask.entity.Address;
import ru.dohod.testtask.entity.Client;

public class ProjectAssertions extends Assertions {
    public static ClientAssert assertThat(Client actual) {
        return new ClientAssert(actual);
    }

    public static AddressAssert assertThat(Address actual) {
        return new AddressAssert(actual);
    }
}
