package lk.ijse.online_course_management.repo;

import lk.ijse.online_course_management.entity.Course;
import lk.ijse.online_course_management.entity.Enrollment;
import lk.ijse.online_course_management.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EnrollmentRepo extends JpaRepository<Enrollment, UUID> {
    boolean existsByStudentAndCourse(User student, Course course);

    List<Enrollment> findByStudent(User student);

    @Query("SELECT COUNT(e) > 0 FROM Enrollment e WHERE e.student.userId = :userId AND e.course.courseId = :courseId")
    boolean existsByStudentAndCourseId(UUID student, UUID courseId);

    @EntityGraph(attributePaths = {"course", "course.instructor", "student"})
    List<Enrollment> findByCourse(Course course);

    @Query("SELECT COUNT(e) > 0 FROM Enrollment e WHERE e.student.email = :email AND e.course.courseId = :courseId")
    boolean existsByStudentEmailAndCourseId(@Param("email") String email, @Param("courseId") UUID courseId);
}
