package com.github.siwonpawel.organizations.batch.organization.dto;

import lombok.Data;

@Data
public class OrganizationDTO {

    private String name;
    private String website;
    private String country;
    private String description;
    private Integer founded;
    private String industry;
    private Integer numberOfEmployees;
}