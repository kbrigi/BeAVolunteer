package edu.bbte.beavolunteerbackend.repository;

import edu.bbte.beavolunteerbackend.model.VolunteerDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerDomainRepository extends JpaRepository<VolunteerDomain, Long> {
}
