package edu.bbte.beavolunteerbackend.model.repository;

import edu.bbte.beavolunteerbackend.model.Project;
import edu.bbte.beavolunteerbackend.model.User;
import edu.bbte.beavolunteerbackend.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO volunteer (id, surname, firstname, phone_nr, description, age, volunteered, gender) " +
            "VALUES (:id, :name1, :name2, :phone," +
            " :desc, :age, :volunt, :gen)", nativeQuery = true)
    void insertVolunteer(@Param("id") Long id, @Param("name1") String surname, @Param("name2") String firstname, @Param("phone") String phoneNr, @Param("desc") String desc,
                         @Param("age") Integer age, @Param("volunt") Boolean volunteered, @Param("gen") String gender);
}
