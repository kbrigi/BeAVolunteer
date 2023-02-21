package edu.bbte.beavolunteerbackend.repository;

import edu.bbte.beavolunteerbackend.model.Volunteer;
import edu.bbte.beavolunteerbackend.util.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO volunteer (id, phone_nr, description, age, volunteered, gender) VALUES (:id, :phone," +
            " :desc, :age, :volunt, :gen)", nativeQuery = true)
    void insertVolunteer(@Param("id") Long id, @Param("phone") String phoneNr, @Param("desc") String desc,
                       @Param("age") Integer age, @Param("volunt") Boolean volunteered, @Param("gen")String gender);

}
