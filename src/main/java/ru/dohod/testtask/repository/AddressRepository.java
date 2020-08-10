package ru.dohod.testtask.repository;

import org.springframework.data.repository.CrudRepository;
import ru.dohod.testtask.entity.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
