package lk.ijse.online_course_management.service.impl;

import lk.ijse.online_course_management.dto.MaterialDTO;
import lk.ijse.online_course_management.entity.Course;
import lk.ijse.online_course_management.entity.Material;
import lk.ijse.online_course_management.repo.CourseRepo;
import lk.ijse.online_course_management.repo.MaterialRepo;
import lk.ijse.online_course_management.service.MaterialService;
import lk.ijse.online_course_management.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialRepo materialRepository;

    @Autowired
    private CourseRepo courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public int uploadMaterial(MaterialDTO materialDTO) {
        try {
            Course course = courseRepository.findById(materialDTO.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if (materialRepository.existsByTitleAndCourse_CourseId(materialDTO.getTitle(), materialDTO.getCourseId())) {
                return VarList.RSP_DUPLICATED;
            }

            Material material = modelMapper.map(materialDTO, Material.class);
            material.setUploadAt(new Date());
            material.setCourse(course);

            materialRepository.save(material);
            return VarList.Success;

        } catch (Exception e) {
            e.printStackTrace();
            return VarList.RSP_ERROR;
        }
    }

    @Override
    public int deleteMaterial(UUID materialId) {
        try {
            if (!materialRepository.existsById(materialId)) {
                return VarList.Not_Found;
            }
            materialRepository.deleteById(materialId);
            return VarList.Success;
        } catch (Exception e) {
            e.printStackTrace();
            return VarList.RSP_ERROR;
        }
    }

    @Override
    public int updateMaterial(MaterialDTO materialDTO) {
        try {
            Material existingMaterial = materialRepository.findById(materialDTO.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Material not found"));

            Course course = courseRepository.findById(materialDTO.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            existingMaterial.setTitle(materialDTO.getTitle());
            existingMaterial.setFileUrl(materialDTO.getFileUrl());
            existingMaterial.setCourse(course);

            materialRepository.save(existingMaterial);
            return VarList.Success;

        } catch (Exception e) {
            e.printStackTrace();
            return VarList.RSP_ERROR;
        }
    }

    @Override
    public MaterialDTO getMaterialById(UUID materialId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material not found"));

        MaterialDTO materialDTO = modelMapper.map(material, MaterialDTO.class);
        materialDTO.setCourseId(material.getCourse().getCourseId());
        materialDTO.setCourseTitle(material.getCourse().getTitle());
        return materialDTO;
    }

    @Override
    public List<MaterialDTO> getAllMaterials() {
        return materialRepository.findAll().stream()
                .map(material -> {
                    MaterialDTO dto = modelMapper.map(material, MaterialDTO.class);
                    dto.setCourseId(material.getCourse().getCourseId());
                    dto.setCourseTitle(material.getCourse().getTitle());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialDTO> getAllMaterialsByCourse(UUID courseId) {
        return materialRepository.findByCourse_CourseId(courseId).stream()
                .map(material -> {
                    MaterialDTO dto = modelMapper.map(material, MaterialDTO.class);
                    dto.setCourseId(material.getCourse().getCourseId());
                    dto.setCourseTitle(material.getCourse().getTitle());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByTitleAndCourse(String title, UUID courseId) {
        return materialRepository.existsByTitleAndCourse_CourseId(title, courseId);
    }
}