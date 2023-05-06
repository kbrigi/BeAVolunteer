package edu.bbte.beavolunteerbackend.model.repository;

import edu.bbte.beavolunteerbackend.model.Project;
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

    @Query(value = "SELECT project_id FROM project_domain pd WHERE pd.domain_id = :did", nativeQuery = true)
    List<Long> getProjectsByDomain(@Param("did") Long domainId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO project_domain (project_id, domain_id) VALUES(:pid, :did) ", nativeQuery = true)
    void insertProjectDomain(@Param("pid") Long projectId, @Param("did") Long domainId);
}
