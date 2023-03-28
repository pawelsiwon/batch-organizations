package com.github.siwonpawel.organizations.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String website;
    private String country;
    private String description;
    private Integer founded;
    private String industry;
    private Integer numberOfEmployees;
}
