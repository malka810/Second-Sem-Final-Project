package lk.ijse.online_course_management.service;

import lk.ijse.online_course_management.dto.AssignmentDTO;

import java.util.List;
import java.util.UUID;

public interface AssignmentService {
    int saveAssignment(AssignmentDTO assignmentDTO);

    List<AssignmentDTO> getAllAssignments();

    List<AssignmentDTO> getAssignmentsByCourse(UUID courseId);

    AssignmentDTO getAssignmentById(UUID assignmentId);

    int updateAssignment(AssignmentDTO assignmentDTO);

    int deleteAssignment(UUID assignmentId);
}
