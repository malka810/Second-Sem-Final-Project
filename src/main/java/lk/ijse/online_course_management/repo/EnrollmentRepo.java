package lk.ijse.online_course_management.repo;

import lk.ijse.online_course_management.entity.Course;
import lk.ijse.online_course_management.entity.Enrollment;
import lk.ijse.online_course_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EnrollmentRepo extends JpaRepository<Enrollment, UUID> {
    boolean existsByStudentAndCourse(User student, Course course);
}
