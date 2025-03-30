package lk.ijse.online_course_management.repo;


import lk.ijse.online_course_management.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;
@Repository
public interface CourseRepo extends JpaRepository<Course, UUID> {


    boolean existsByTitle(String title);

    Collection<Object> findByInstructorUserId(UUID instructorId);
}
