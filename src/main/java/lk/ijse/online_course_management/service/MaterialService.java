package lk.ijse.online_course_management.service;


import lk.ijse.online_course_management.dto.MaterialDTO;

import java.util.List;
import java.util.UUID;

public interface MaterialService {

    int uploadMaterial(MaterialDTO materialDTO);

    List<MaterialDTO> getAllMaterials();

    List<MaterialDTO> getAllMaterialsByCourse(UUID courseId);

    MaterialDTO getMaterialById(UUID materialId);

    int updateMaterial(MaterialDTO materialDTO);

    int deleteMaterial(UUID materialId);

    boolean existsByTitleAndCourse(String title, UUID courseId);
}
