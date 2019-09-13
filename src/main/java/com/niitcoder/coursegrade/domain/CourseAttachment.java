package com.niitcoder.coursegrade.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A CourseAttachment.
 */
@Entity
@Table(name = "course_attachment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CourseAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attachment_type")
    private String attachmentType;

    @Column(name = "attachment_use")
    private String attachmentUse;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "origin_name")
    private String originName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "upload_time")
    private ZonedDateTime uploadTime;

    @Column(name = "file_user")
    private String fileUser;

    @ManyToOne
    @JsonIgnoreProperties("courseAttachments")
    private StudentHomework homework;

    @ManyToOne
    @JsonIgnoreProperties("courseAttachments")
    private CourseNote note;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public CourseAttachment attachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
        return this;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getAttachmentUse() {
        return attachmentUse;
    }

    public CourseAttachment attachmentUse(String attachmentUse) {
        this.attachmentUse = attachmentUse;
        return this;
    }

    public void setAttachmentUse(String attachmentUse) {
        this.attachmentUse = attachmentUse;
    }

    public String getFileName() {
        return fileName;
    }

    public CourseAttachment fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginName() {
        return originName;
    }

    public CourseAttachment originName(String originName) {
        this.originName = originName;
        return this;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getFilePath() {
        return filePath;
    }

    public CourseAttachment filePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public CourseAttachment fileSize(Long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public ZonedDateTime getUploadTime() {
        return uploadTime;
    }

    public CourseAttachment uploadTime(ZonedDateTime uploadTime) {
        this.uploadTime = uploadTime;
        return this;
    }

    public void setUploadTime(ZonedDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getFileUser() {
        return fileUser;
    }

    public CourseAttachment fileUser(String fileUser) {
        this.fileUser = fileUser;
        return this;
    }

    public void setFileUser(String fileUser) {
        this.fileUser = fileUser;
    }

    public StudentHomework getHomework() {
        return homework;
    }

    public CourseAttachment homework(StudentHomework studentHomework) {
        this.homework = studentHomework;
        return this;
    }

    public void setHomework(StudentHomework studentHomework) {
        this.homework = studentHomework;
    }

    public CourseNote getNote() {
        return note;
    }

    public CourseAttachment note(CourseNote courseNote) {
        this.note = courseNote;
        return this;
    }

    public void setNote(CourseNote courseNote) {
        this.note = courseNote;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseAttachment)) {
            return false;
        }
        return id != null && id.equals(((CourseAttachment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CourseAttachment{" +
            "id=" + getId() +
            ", attachmentType='" + getAttachmentType() + "'" +
            ", attachmentUse='" + getAttachmentUse() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", originName='" + getOriginName() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", fileSize=" + getFileSize() +
            ", uploadTime='" + getUploadTime() + "'" +
            ", fileUser='" + getFileUser() + "'" +
            "}";
    }
}
