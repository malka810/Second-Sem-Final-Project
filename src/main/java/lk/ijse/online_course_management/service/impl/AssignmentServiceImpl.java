package lk.ijse.online_course_management.service.impl;

import lk.ijse.online_course_management.dto.AssignmentDTO;
import lk.ijse.online_course_management.entity.Assignment;
import lk.ijse.online_course_management.entity.Course;
import lk.ijse.online_course_management.repo.AssignmentRepo;
import lk.ijse.online_course_management.repo.CourseRepo;
import lk.ijse.online_course_management.service.AssignmentService;
import lk.ijse.online_course_management.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    @Autowired
    private AssignmentRepo assignmentRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public int saveAssignment(AssignmentDTO assignmentDTO) {
        try {
            Course course = courseRepo.findById(assignmentDTO.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            // Check for duplicate assignment title in the same course
            if (assignmentRepo.existsByTitleAndCourse_CourseId(assignmentDTO.getTitle(), assignmentDTO.getCourseId())) {
                return VarList.RSP_DUPLICATED;
            }

            // Map DTO to entity
            Assignment assignment = modelMapper.map(assignmentDTO, Assignment.class);
            assignment.setCreatedAt(LocalDateTime.now());
            assignment.setCourse(course);

            // Save the assignment
            assignmentRepo.save(assignment);
            return VarList.Created;

        } catch (Exception e) {
            e.printStackTrace();
            return VarList.RSP_ERROR;
        }
    }

    @Override
    public List<AssignmentDTO> getAllAssignments() {
        return assignmentRepo.findAll().stream()
                .map(this::convertToDtoWithCourseInfo)
                .collect(Collectors.toList());
    }

    private AssignmentDTO convertToDtoWithCourseInfo(Assignment assignment) {
        AssignmentDTO dto = modelMapper.map(assignment, AssignmentDTO.class);
        dto.setCourseId(assignment.getCourse().getCourseId());
        dto.setTitle(assignment.getCourse().getTitle());
        return dto;
    }

    @Override
    public List<AssignmentDTO> getAssignmentsByCourse(UUID courseId) {
        try {
            List<Assignment> assignments = assignmentRepo.findByCourse_CourseId(courseId);

            return assignments.stream()
                    .map(assignment -> {
                        AssignmentDTO dto = modelMapper.map(assignment, AssignmentDTO.class);
                        dto.setCourseId(assignment.getCourse().getCourseId());
                        dto.setTitle(assignment.getCourse().getTitle());
                        return dto;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching assignments by course: " + e.getMessage());
        }
    }

    @Override
    public AssignmentDTO getAssignmentById(UUID assignmentId) {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        return convertToDtoWithCourseInfo(assignment);
    }

    @Override
    public int updateAssignment(AssignmentDTO assignmentDTO) {
        try {
            // Check if assignment exists
            Assignment existingAssignment = assignmentRepo.findById(assignmentDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Assignment not found"));

            // Check if course exists
            Course course = courseRepo.findById(assignmentDTO.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            // Update assignment fields
            existingAssignment.setTitle(assignmentDTO.getTitle());
            existingAssignment.setDescription(assignmentDTO.getDescription());
            existingAssignment.setFilePath(assignmentDTO.getFilePath());
            existingAssignment.setDueDate(assignmentDTO.getDueDate());
            existingAssignment.setCourse(course);

            // Save the updated assignment
            assignmentRepo.save(existingAssignment);
            return VarList.OK;

        } catch (Exception e) {
            e.printStackTrace();
            return VarList.RSP_ERROR;
        }
    }

    @Override
    public int deleteAssignment(UUID assignmentId) {
        try {
            if (!assignmentRepo.existsById(assignmentId)) {
                return VarList.Not_Found;
            }
            assignmentRepo.deleteById(assignmentId);
            return VarList.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return VarList.RSP_ERROR;
        }
    }
}
