package lk.ijse.online_course_management.repo;


import lk.ijse.online_course_management.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface CourseRepo extends JpaRepository<Course, UUID> {


    boolean existsByTitle(String title);

    @Query("SELECT c FROM Course c WHERE c.instructor.userId = :instructorId")
    List<Course> findByInstructorUserId(UUID instructorId);

    @Query("SELECT c FROM Course c WHERE c.active = true")
    List<Course> findByActiveTrue();
}
