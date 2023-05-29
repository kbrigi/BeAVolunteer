package edu.bbte.beavolunteerbackend.controller.mapper;

import edu.bbte.beavolunteerbackend.controller.dto.incoming.DomainDTO;
import edu.bbte.beavolunteerbackend.model.Domain;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DomainMapper {
    public static Domain domainDTOToDomain(DomainDTO domainDTO) {
        Domain domain = new Domain();
        domain.setDomainName(domainDTO.getDomain_name());
        return domain;
    }

    public static DomainDTO domainToDTO(Domain domain) {
        DomainDTO domainDTO = new DomainDTO();
        domainDTO.setDomain_id(domain.getDomainId());
        domainDTO.setDomain_name(domain.getDomainName());
        domainDTO.setDomain_img("http://localhost:8080/domain/image/" + domain.getDomainName());
        return domainDTO;
    }

    public static List<DomainDTO> domainsToDTO(Collection<Domain> domains) {
        return domains.stream().map(DomainMapper::domainToDTO).collect(Collectors.toList());
    }

    public static List<Domain> domainDTOSToDomain(Collection<DomainDTO> domains) {
        return domains.stream().map(DomainMapper::domainDTOToDomain).collect(Collectors.toList());
    }

    public static Set<Domain> domainDTOSToDomains(Collection<DomainDTO> domains) {
        return domains.stream().map(DomainMapper::domainDTOToDomain).collect(Collectors.toSet());
    }
}
