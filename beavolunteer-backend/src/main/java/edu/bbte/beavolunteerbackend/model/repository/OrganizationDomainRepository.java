package edu.bbte.beavolunteerbackend.model.repository;

import edu.bbte.beavolunteerbackend.model.Domain;
import edu.bbte.beavolunteerbackend.model.OrganizationDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationDomainRepository extends JpaRepository<OrganizationDomain, Long> {
    @Query(value = "SELECT domain_id FROM organization_domain od WHERE od.organization_id = :oid", nativeQuery = true)
    List<Long> findDomainsByOrg(@Param("oid") Long orgId);
}
