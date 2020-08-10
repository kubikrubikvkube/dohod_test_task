package ru.dohod.testtask.repository;

import org.springframework.data.repository.CrudRepository;
import ru.dohod.testtask.entity.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
