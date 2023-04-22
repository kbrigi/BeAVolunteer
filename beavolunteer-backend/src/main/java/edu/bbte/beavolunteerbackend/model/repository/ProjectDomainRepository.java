package edu.bbte.beavolunteerbackend.model.repository;

import edu.bbte.beavolunteerbackend.model.ProjectDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProjectDomainRepository extends JpaRepository<ProjectDomain, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO project_domain (project_id, domain_id) VALUES(:pid, :did) ", nativeQuery = true)
    void insertProjectDomain(@Param("pid") Long projectId, @Param("did") Long domainId);
}
