package edu.bbte.beavolunteerbackend.model.repository;

import edu.bbte.beavolunteerbackend.model.VolunteerDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface VolunteerDomainRepository extends JpaRepository<VolunteerDomain, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO volunteer_domain (volunteer_id, domain_id) VALUES(:vid, :did) ", nativeQuery = true)
    void insertVolunteerDomain(@Param("vid") Long userId, @Param("did") Long domainId);
}
