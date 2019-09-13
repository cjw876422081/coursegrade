package com.niitcoder.coursegrade.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A StudentHomework.
 */
@Entity
@Table(name = "student_homework")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StudentHomework implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "submit_memo")
    private String submitMemo;

    @Column(name = "submit_time")
    private ZonedDateTime submitTime;

    @Column(name = "read_time")
    private ZonedDateTime readTime;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "student")
    private String student;

    @Column(name = "teacher")
    private String teacher;

    @ManyToOne
    @JsonIgnoreProperties("studentHomeworks")
    private CourseHomework homework;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubmitMemo() {
        return submitMemo;
    }

    public StudentHomework submitMemo(String submitMemo) {
        this.submitMemo = submitMemo;
        return this;
    }

    public void setSubmitMemo(String submitMemo) {
        this.submitMemo = submitMemo;
    }

    public ZonedDateTime getSubmitTime() {
        return submitTime;
    }

    public StudentHomework submitTime(ZonedDateTime submitTime) {
        this.submitTime = submitTime;
        return this;
    }

    public void setSubmitTime(ZonedDateTime submitTime) {
        this.submitTime = submitTime;
    }

    public ZonedDateTime getReadTime() {
        return readTime;
    }

    public StudentHomework readTime(ZonedDateTime readTime) {
        this.readTime = readTime;
        return this;
    }

    public void setReadTime(ZonedDateTime readTime) {
        this.readTime = readTime;
    }

    public Integer getGrade() {
        return grade;
    }

    public StudentHomework grade(Integer grade) {
        this.grade = grade;
        return this;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getStudent() {
        return student;
    }

    public StudentHomework student(String student) {
        this.student = student;
        return this;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getTeacher() {
        return teacher;
    }

    public StudentHomework teacher(String teacher) {
        this.teacher = teacher;
        return this;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public CourseHomework getHomework() {
        return homework;
    }

    public StudentHomework homework(CourseHomework courseHomework) {
        this.homework = courseHomework;
        return this;
    }

    public void setHomework(CourseHomework courseHomework) {
        this.homework = courseHomework;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentHomework)) {
            return false;
        }
        return id != null && id.equals(((StudentHomework) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "StudentHomework{" +
            "id=" + getId() +
            ", submitMemo='" + getSubmitMemo() + "'" +
            ", submitTime='" + getSubmitTime() + "'" +
            ", readTime='" + getReadTime() + "'" +
            ", grade=" + getGrade() +
            ", student='" + getStudent() + "'" +
            ", teacher='" + getTeacher() + "'" +
            "}";
    }
}
