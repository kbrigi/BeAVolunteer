package edu.bbte.beavolunteerbackend.model.repository;

import edu.bbte.beavolunteerbackend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Blob;
import java.util.Date;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(value = "SELECT COUNT(p.project_name) FROM Project p WHERE p.project_name = :name", nativeQuery = true)
    Integer checkName(@Param("name")String project_name);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO project (creation_date, expiration_date, project_description, project_img, project_name, owner_id)" +
            " VALUES (:start, :end, :desc, :img, :name, :owner)", nativeQuery = true)
    void insertProject(@Param("start")Date start, @Param("end") Date end, @Param("desc") String desc,
                       @Param("img") Blob img, @Param("name") String name, @Param("owner") Long owner);

    @Query(value = "SELECT * FROM Project p WHERE p.project_name = :name", nativeQuery = true)
    Project getByName(@Param("name")String project_name);
}
