package lk.ijse.online_course_management.repo;

import lk.ijse.online_course_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

    boolean existsByEmail(String email);

    User findByEmail(String email);

    Collection<Object> findByRole(String upperCase);
}
