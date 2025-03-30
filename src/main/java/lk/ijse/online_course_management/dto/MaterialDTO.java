package lk.ijse.online_course_management.dto;

import lk.ijse.online_course_management.entity.Material;

import java.util.Date;
import java.util.UUID;


public class MaterialDTO {
    private UUID materialId;
    private String title;
    private String fileUrl;
    private Date uploadAt;
    private UUID courseId;
    private String courseTitle;

    public MaterialDTO() {
    }

    public MaterialDTO(UUID materialId, String title, String fileUrl, Date uploadAt, UUID courseId, String courseTitle) {
        this.materialId = materialId;
        this.title = title;
        this.fileUrl = fileUrl;
        this.uploadAt = uploadAt;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
    }


    public UUID getMaterialId() {
        return materialId;
    }

    public void setMaterialId(UUID materialId) {
        this.materialId = materialId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Date getUploadAt() {
        return uploadAt;
    }

    public void setUploadAt(Date uploadAt) {
        this.uploadAt = uploadAt;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    @Override
    public String toString() {
        return "MaterialDTO{" +
                "materialId=" + materialId +
                ", title='" + title + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", uploadAt=" + uploadAt +
                ", courseId=" + courseId +
                ", courseTitle='" + courseTitle + '\'' +
                '}';
    }
}
