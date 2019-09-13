package com.niitcoder.coursegrade.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A CourseHomework.
 */
@Entity
@Table(name = "course_homework")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CourseHomework implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "homework_code")
    private String homeworkCode;

    @Column(name = "homework_memo")
    private String homeworkMemo;

    @Column(name = "homework_target")
    private String homeworkTarget;

    @Column(name = "homework_grade")
    private Integer homeworkGrade;

    @Column(name = "homework_deadline")
    private ZonedDateTime homeworkDeadline;

    @Column(name = "data_time")
    private ZonedDateTime dataTime;

    @ManyToOne
    @JsonIgnoreProperties("courseHomeworks")
    private CoursePlan plan;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHomeworkCode() {
        return homeworkCode;
    }

    public CourseHomework homeworkCode(String homeworkCode) {
        this.homeworkCode = homeworkCode;
        return this;
    }

    public void setHomeworkCode(String homeworkCode) {
        this.homeworkCode = homeworkCode;
    }

    public String getHomeworkMemo() {
        return homeworkMemo;
    }

    public CourseHomework homeworkMemo(String homeworkMemo) {
        this.homeworkMemo = homeworkMemo;
        return this;
    }

    public void setHomeworkMemo(String homeworkMemo) {
        this.homeworkMemo = homeworkMemo;
    }

    public String getHomeworkTarget() {
        return homeworkTarget;
    }

    public CourseHomework homeworkTarget(String homeworkTarget) {
        this.homeworkTarget = homeworkTarget;
        return this;
    }

    public void setHomeworkTarget(String homeworkTarget) {
        this.homeworkTarget = homeworkTarget;
    }

    public Integer getHomeworkGrade() {
        return homeworkGrade;
    }

    public CourseHomework homeworkGrade(Integer homeworkGrade) {
        this.homeworkGrade = homeworkGrade;
        return this;
    }

    public void setHomeworkGrade(Integer homeworkGrade) {
        this.homeworkGrade = homeworkGrade;
    }

    public ZonedDateTime getHomeworkDeadline() {
        return homeworkDeadline;
    }

    public CourseHomework homeworkDeadline(ZonedDateTime homeworkDeadline) {
        this.homeworkDeadline = homeworkDeadline;
        return this;
    }

    public void setHomeworkDeadline(ZonedDateTime homeworkDeadline) {
        this.homeworkDeadline = homeworkDeadline;
    }

    public ZonedDateTime getDataTime() {
        return dataTime;
    }

    public CourseHomework dataTime(ZonedDateTime dataTime) {
        this.dataTime = dataTime;
        return this;
    }

    public void setDataTime(ZonedDateTime dataTime) {
        this.dataTime = dataTime;
    }

    public CoursePlan getPlan() {
        return plan;
    }

    public CourseHomework plan(CoursePlan coursePlan) {
        this.plan = coursePlan;
        return this;
    }

    public void setPlan(CoursePlan coursePlan) {
        this.plan = coursePlan;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseHomework)) {
            return false;
        }
        return id != null && id.equals(((CourseHomework) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CourseHomework{" +
            "id=" + getId() +
            ", homeworkCode='" + getHomeworkCode() + "'" +
            ", homeworkMemo='" + getHomeworkMemo() + "'" +
            ", homeworkTarget='" + getHomeworkTarget() + "'" +
            ", homeworkGrade=" + getHomeworkGrade() +
            ", homeworkDeadline='" + getHomeworkDeadline() + "'" +
            ", dataTime='" + getDataTime() + "'" +
            "}";
    }
}
