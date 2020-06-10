package com.example.hibernate.domain;

import javax.persistence.*;

@Entity
@Table(name = "PERSONS")
public class Person {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    //Getter and Setter
}
