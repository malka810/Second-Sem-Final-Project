package lk.ijse.online_course_management.entity;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "material")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID materialId;
    private String title;
    private String fileUrl;
    private Date uploadAt;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    @Column(name = "course_title", nullable = false)
    private String courseTitle;

  public Material() {}
    public Material(UUID materialId, String title, String fileUrl, Date uploadAt, Course course, String courseTitle) {
        this.materialId = materialId;
        this.title = title;
        this.fileUrl = fileUrl;
        this.uploadAt = uploadAt;
        this.course = course;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    @Override
    public String toString() {
        return "Material{" +
                "materialId=" + materialId +
                ", title='" + title + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", uploadAt=" + uploadAt +
                ", course=" + course +
                ", courseTitle='" + courseTitle + '\'' +
                '}';
    }
}
