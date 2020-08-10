package ru.dohod.testtask.entity;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Client {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;


    @OneToMany(cascade = {CascadeType.REMOVE}, orphanRemoval = true, mappedBy = "client", fetch = FetchType.EAGER)
    private List<Address> addresses;

    public Client(@NonNull String name) {
        this.name = name;
    }

    public Client() {
    }
}
