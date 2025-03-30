package lk.ijse.online_course_management.repo;

import lk.ijse.online_course_management.entity.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepo extends JpaRepository<Authentication, Long> {
}
