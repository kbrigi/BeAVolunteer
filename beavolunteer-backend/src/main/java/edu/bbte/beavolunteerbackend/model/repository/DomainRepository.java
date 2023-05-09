package edu.bbte.beavolunteerbackend.model.repository;

import edu.bbte.beavolunteerbackend.model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long> {
    @Query(value = "SELECT COUNT(d.domain_id) FROM Domain d WHERE d.domain_name = :name", nativeQuery = true)
    int checkName(@Param("name") String domain_name);

    @Query(value = "SELECT d.* FROM Domain d WHERE d.domain_name = :name", nativeQuery = true)
    Domain findByName(@Param("name") String domain_name);

    @Query(value = "SELECT d.* FROM Domain d ORDER BY d.domain_name ASC ", nativeQuery = true)
    Collection<Domain> findAllAsc();
}
