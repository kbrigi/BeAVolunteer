package edu.bbte.beavolunteerbackend.model.repository;

import edu.bbte.beavolunteerbackend.model.Domain;
import edu.bbte.beavolunteerbackend.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Blob;
import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO organization (id, address, description, phone_nr, logo, website, domains) VALUES (:id, :address,"
            + " :desc, :phone, :logo, :web, :domains)", nativeQuery = true)
    void insertOrg(@Param("id") Long id, @Param("address") String address, @Param("desc") String desc,
                   @Param("phone") String phone, @Param("logo") Blob logo, @Param("web") String web, @Param("domains") List<Domain> domains);
}
