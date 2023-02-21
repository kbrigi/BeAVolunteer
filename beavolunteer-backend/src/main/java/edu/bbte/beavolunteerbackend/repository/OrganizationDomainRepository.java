package edu.bbte.beavolunteerbackend.repository;

import edu.bbte.beavolunteerbackend.model.OrganizationDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationDomainRepository extends JpaRepository<OrganizationDomain, Long> {
}
