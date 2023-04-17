package edu.bbte.beavolunteerbackend.model.repository;

import edu.bbte.beavolunteerbackend.model.ProjectDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDomainRepository extends JpaRepository<ProjectDomain, Long> {
}
