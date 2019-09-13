package com.niitcoder.coursegrade.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A StudentCourseGroup.
 */
@Entity
@Table(name = "student_course_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StudentCourseGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student")
    private String student;

    @Column(name = "join_time")
    private ZonedDateTime joinTime;

    @ManyToOne
    @JsonIgnoreProperties("studentCourseGroups")
    private CourseGroup group;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudent() {
        return student;
    }

    public StudentCourseGroup student(String student) {
        this.student = student;
        return this;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public ZonedDateTime getJoinTime() {
        return joinTime;
    }

    public StudentCourseGroup joinTime(ZonedDateTime joinTime) {
        this.joinTime = joinTime;
        return this;
    }

    public void setJoinTime(ZonedDateTime joinTime) {
        this.joinTime = joinTime;
    }

    public CourseGroup getGroup() {
        return group;
    }

    public StudentCourseGroup group(CourseGroup courseGroup) {
        this.group = courseGroup;
        return this;
    }

    public void setGroup(CourseGroup courseGroup) {
        this.group = courseGroup;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentCourseGroup)) {
            return false;
        }
        return id != null && id.equals(((StudentCourseGroup) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "StudentCourseGroup{" +
            "id=" + getId() +
            ", student='" + getStudent() + "'" +
            ", joinTime='" + getJoinTime() + "'" +
            "}";
    }
}
