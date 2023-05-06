package edu.bbte.beavolunteerbackend.model.repository;

import edu.bbte.beavolunteerbackend.model.Role;
import edu.bbte.beavolunteerbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT COUNT(user.id) FROM User user WHERE user.user_name = :username", nativeQuery = true)
    int existsUsername(@Param("username") String username);

    @Query(value = "SELECT COUNT(user.id) FROM User user WHERE user.email = :email", nativeQuery = true)
    int existsEmail(@Param("email") String email);

    @Query(value = "SELECT user FROM User user WHERE user.email = :email")
    User findByEmail(@Param("email") String email);

    @Query(value = "SELECT user FROM User user WHERE user.userName = :username")
    User findByUsername(@Param("username") String username);

    @Query(value = "SELECT u.id FROM User u WHERE u.role = :role", nativeQuery = true)
    List<Long> findByRole(@Param("role") String role);
}
