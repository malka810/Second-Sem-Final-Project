package lk.ijse.online_course_management.repo;

import lk.ijse.online_course_management.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssignmentRepo extends JpaRepository<Assignment, UUID> {
    boolean existsByTitleAndCourse_CourseId(String title, UUID courseId);

    boolean existsById(UUID assignmentId);

    void deleteById(UUID assignmentId);

    List<Assignment> findByCourse_CourseId(UUID courseId);

    ;
}
