package lk.ijse.online_course_management.repo;

import lk.ijse.online_course_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role = 'INSTRUCTOR'")
    List<User> findByRole();
}
