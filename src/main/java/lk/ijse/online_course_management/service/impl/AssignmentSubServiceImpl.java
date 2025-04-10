package lk.ijse.online_course_management.service.impl;

import lk.ijse.online_course_management.dto.AssignmentSubDTO;
import lk.ijse.online_course_management.entity.Assignment;
import lk.ijse.online_course_management.entity.AssignmentSub;
import lk.ijse.online_course_management.entity.User;
import lk.ijse.online_course_management.repo.AssignmentRepo;
import lk.ijse.online_course_management.repo.AssignmentSubRepo;
import lk.ijse.online_course_management.repo.UserRepo;
import lk.ijse.online_course_management.service.AssignmentSubService;
import lk.ijse.online_course_management.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AssignmentSubServiceImpl implements AssignmentSubService {

    @Autowired
    private AssignmentSubRepo assignmentSubRepo;

    @Autowired
    private AssignmentRepo assignmentRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public int saveSubmission(AssignmentSubDTO submissionDTO) {
        try {
            // Check if assignment exists
            Assignment assignment = assignmentRepo.findById(submissionDTO.getAssignmentId())
                    .orElseThrow(() -> new RuntimeException("Assignment not found"));

            // Check if student exists
            User student = userRepo.findById((submissionDTO.getStudentId()))
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            // Check for duplicate submission
            if (assignmentSubRepo.existsByAssignmentAndStudent(assignment, student)) {
                return VarList.RSP_DUPLICATED; // "06"
            }

            // Create new submission
            AssignmentSub submission = modelMapper.map(submissionDTO, AssignmentSub.class);
            submission.setAssignment(assignment);
            submission.setStudent(student);
            submission.setSubmittedAt(LocalDateTime.now());
            submission.setGraded(false);

            assignmentSubRepo.save(submission);
            return VarList.OK; // "00"

        } catch (Exception e) {
            e.printStackTrace();
            return VarList.RSP_ERROR; // "05"
        }
    }

    @Override
    public AssignmentSubDTO getSubmissionById(UUID submissionId) {
        AssignmentSub submission = assignmentSubRepo.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        AssignmentSubDTO dto = modelMapper.map(submission, AssignmentSubDTO.class);
        dto.setAssignmentId(submission.getAssignment().getId());
        dto.setAssignmentTitle(submission.getAssignment().getTitle());
        dto.setStudentId(submission.getStudent().getUserId());
        dto.setStudentName(submission.getStudent().getFullName());

        return dto;
    }

    @Override
    public List<AssignmentSubDTO> getSubmissionsByAssignment(UUID assignmentId) {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        return assignmentSubRepo.findByAssignment(assignment).stream()
                .map(submission -> {
                    AssignmentSubDTO dto = modelMapper.map(submission, AssignmentSubDTO.class);
                    dto.setAssignmentId(assignment.getId());
                    dto.setAssignmentTitle(assignment.getTitle());
                    dto.setStudentId(submission.getStudent().getUserId());
                    dto.setStudentName(submission.getStudent().getFullName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<AssignmentSubDTO> getSubmissionsByStudent(UUID studentId) {
        User student = userRepo.findById((studentId))
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return assignmentSubRepo.findByStudent(student).stream()
                .map(submission -> {
                    AssignmentSubDTO dto = modelMapper.map(submission, AssignmentSubDTO.class);
                    dto.setAssignmentId(submission.getAssignment().getId());
                    dto.setAssignmentTitle(submission.getAssignment().getTitle());
                    dto.setStudentId(student.getUserId());
                    dto.setStudentName(student.getFullName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public int gradeSubmission(AssignmentSubDTO submissionDTO) {
        try {
            AssignmentSub submission = assignmentSubRepo.findById(submissionDTO.getSubmissionId())
                    .orElseThrow(() -> new RuntimeException("Submission not found"));

            submission.setMarks(submissionDTO.getMarks());
            submission.setFeedback(submissionDTO.getFeedback());
            submission.setGraded(true);

            assignmentSubRepo.save(submission);
            return VarList.OK; // "00"
        } catch (Exception e) {
            e.printStackTrace();
            return VarList.RSP_ERROR; // "05"
        }
    }
}