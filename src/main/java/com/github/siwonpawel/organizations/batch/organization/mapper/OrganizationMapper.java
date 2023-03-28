package com.github.siwonpawel.organizations.batch.organization.mapper;

import com.github.siwonpawel.organizations.batch.organization.dto.OrganizationDTO;
import com.github.siwonpawel.organizations.domain.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrganizationMapper {

    Organization mapOrganization(OrganizationDTO organizationDTO);

}
