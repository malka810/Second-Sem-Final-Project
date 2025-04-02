package lk.ijse.online_course_management.repo;

import lk.ijse.online_course_management.entity.Assignment;
import lk.ijse.online_course_management.entity.AssignmentSub;
import lk.ijse.online_course_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AssignmentSubRepo extends JpaRepository<AssignmentSub, UUID> {
   
    List<AssignmentSub> findByAssignment(Assignment assignment);
    List<AssignmentSub> findByStudent(User student);

    boolean existsByAssignmentAndStudent(Assignment assignment, User student);
}