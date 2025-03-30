package lk.ijse.online_course_management.repo;

import lk.ijse.online_course_management.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MaterialRepo  extends JpaRepository<Material, UUID> {
    boolean existsByTitleAndCourse_CourseId(String title, UUID courseId);

    List<Material> findByCourse_CourseId(UUID courseId);
}
