package edu.bbte.beavolunteerbackend.model.repository;

import edu.bbte.beavolunteerbackend.model.ProjectDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProjectDomainRepository extends JpaRepository<ProjectDomain, Long> {

    @Query(value="SELECT domain_id FROM project_domain pd WHERE pd.project_id = :pid", nativeQuery = true)
    List<Long> findDomainsByProject(@Param("pid") Long projectId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO project_domain (project_id, domain_id) VALUES(:pid, :did) ", nativeQuery = true)
    void insertProjectDomain(@Param("pid") Long projectId, @Param("did") Long domainId);
}
