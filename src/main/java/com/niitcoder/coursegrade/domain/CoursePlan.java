package com.niitcoder.coursegrade.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A CoursePlan.
 */
@Entity
@Table(name = "course_plan")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CoursePlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plan_code")
    private Integer planCode;

    @Column(name = "plan_memo")
    private String planMemo;

    @Column(name = "plan_target")
    private String planTarget;

    @Column(name = "plan_count")
    private Integer planCount;

    @Column(name = "plan_parent_code")
    private Integer planParentCode;

    @Column(name = "data_time")
    private ZonedDateTime dataTime;

    @ManyToOne
    @JsonIgnoreProperties("coursePlans")
    private CoursePlan parentPlan;

    @ManyToOne
    @JsonIgnoreProperties("coursePlans")
    private CourseInfo course;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPlanCode() {
        return planCode;
    }

    public CoursePlan planCode(Integer planCode) {
        this.planCode = planCode;
        return this;
    }

    public void setPlanCode(Integer planCode) {
        this.planCode = planCode;
    }

    public String getPlanMemo() {
        return planMemo;
    }

    public CoursePlan planMemo(String planMemo) {
        this.planMemo = planMemo;
        return this;
    }

    public void setPlanMemo(String planMemo) {
        this.planMemo = planMemo;
    }

    public String getPlanTarget() {
        return planTarget;
    }

    public CoursePlan planTarget(String planTarget) {
        this.planTarget = planTarget;
        return this;
    }

    public void setPlanTarget(String planTarget) {
        this.planTarget = planTarget;
    }

    public Integer getPlanCount() {
        return planCount;
    }

    public CoursePlan planCount(Integer planCount) {
        this.planCount = planCount;
        return this;
    }

    public void setPlanCount(Integer planCount) {
        this.planCount = planCount;
    }

    public Integer getPlanParentCode() {
        return planParentCode;
    }

    public CoursePlan planParentCode(Integer planParentCode) {
        this.planParentCode = planParentCode;
        return this;
    }

    public void setPlanParentCode(Integer planParentCode) {
        this.planParentCode = planParentCode;
    }

    public ZonedDateTime getDataTime() {
        return dataTime;
    }

    public CoursePlan dataTime(ZonedDateTime dataTime) {
        this.dataTime = dataTime;
        return this;
    }

    public void setDataTime(ZonedDateTime dataTime) {
        this.dataTime = dataTime;
    }

    public CoursePlan getParentPlan() {
        return parentPlan;
    }

    public CoursePlan parentPlan(CoursePlan coursePlan) {
        this.parentPlan = coursePlan;
        return this;
    }

    public void setParentPlan(CoursePlan coursePlan) {
        this.parentPlan = coursePlan;
    }

    public CourseInfo getCourse() {
        return course;
    }

    public CoursePlan course(CourseInfo courseInfo) {
        this.course = courseInfo;
        return this;
    }

    public void setCourse(CourseInfo courseInfo) {
        this.course = courseInfo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoursePlan)) {
            return false;
        }
        return id != null && id.equals(((CoursePlan) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CoursePlan{" +
            "id=" + getId() +
            ", planCode=" + getPlanCode() +
            ", planMemo='" + getPlanMemo() + "'" +
            ", planTarget='" + getPlanTarget() + "'" +
            ", planCount=" + getPlanCount() +
            ", planParentCode=" + getPlanParentCode() +
            ", dataTime='" + getDataTime() + "'" +
            "}";
    }
}
