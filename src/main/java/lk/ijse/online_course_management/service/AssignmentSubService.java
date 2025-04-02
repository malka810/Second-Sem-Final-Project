package lk.ijse.online_course_management.service;

import lk.ijse.online_course_management.dto.AssignmentSubDTO;

import java.util.UUID;

public interface AssignmentSubService {
    int saveSubmission(AssignmentSubDTO submissionDTO);

    AssignmentSubDTO getSubmissionById(UUID submissionId);

    Object getSubmissionsByAssignment(UUID assignmentId);

    Object getSubmissionsByStudent(UUID studentId);

    int gradeSubmission(AssignmentSubDTO submissionDTO);
}
