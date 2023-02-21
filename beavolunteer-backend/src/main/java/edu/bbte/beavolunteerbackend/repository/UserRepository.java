package edu.bbte.beavolunteerbackend.repository;

import edu.bbte.beavolunteerbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT COUNT(user.id) FROM User user WHERE user.user_name = :username", nativeQuery = true)
    int existsUsername(@Param("username") String username);

    @Query(value = "SELECT COUNT(user.id) FROM User user WHERE user.email = :email", nativeQuery = true)
    int existsEmail(@Param("email") String email);

    @Query(value = "SELECT user FROM User user WHERE user.userName = :username OR user.email = :username")
    User matchUser(@Param("username") String username);

}
