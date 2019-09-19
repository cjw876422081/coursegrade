package com.niitcoder.coursegrade.service.dto;

import javax.persistence.Column;
import java.time.ZonedDateTime;

public class StudentHomewrokDTO {

    private Long id;
    private String submitMemo;
    private ZonedDateTime submitTime;
    private ZonedDateTime readTime;
    private Integer grade;
    private String student;
    private String teacher;

    public StudentHomewrokDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubmitMemo() {
        return submitMemo;
    }

    public void setSubmitMemo(String submitMemo) {
        this.submitMemo = submitMemo;
    }

    public ZonedDateTime getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(ZonedDateTime submitTime) {
        this.submitTime = submitTime;
    }

    public ZonedDateTime getReadTime() {
        return readTime;
    }

    public void setReadTime(ZonedDateTime readTime) {
        this.readTime = readTime;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "StudentHomewrokDTO{" +
            "id=" + id +
            ", submitMemo='" + submitMemo + '\'' +
            ", submitTime=" + submitTime +
            ", readTime=" + readTime +
            ", grade=" + grade +
            ", student='" + student + '\'' +
            ", teacher='" + teacher + '\'' +
            '}';
    }
}
