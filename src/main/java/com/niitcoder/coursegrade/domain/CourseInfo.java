package com.niitcoder.coursegrade.domain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * 课程信息
 */
@ApiModel(description = "课程信息")
@Entity
@Table(name = "course_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CourseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 课程代码
     */
    @ApiModelProperty(value = "课程代码")
    @Column(name = "course_code")
    private String courseCode;

    /**
     * 课程名称
     */
    @ApiModelProperty(value = "课程名称")
    @Column(name = "course_name")
    private String courseName;

    @Column(name = "course_count")
    private Integer courseCount;

    @Column(name = "course_week_count")
    private Integer courseWeekCount;

    @Column(name = "course_memo")
    private String courseMemo;

    @Column(name = "data_time")
    private ZonedDateTime dataTime;

    @Column(name = "course_user")
    private String courseUser;

    @Column(name = "course_cover")
    private String courseCover;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public CourseInfo courseCode(String courseCode) {
        this.courseCode = courseCode;
        return this;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public CourseInfo courseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCourseCount() {
        return courseCount;
    }

    public CourseInfo courseCount(Integer courseCount) {
        this.courseCount = courseCount;
        return this;
    }

    public void setCourseCount(Integer courseCount) {
        this.courseCount = courseCount;
    }

    public Integer getCourseWeekCount() {
        return courseWeekCount;
    }

    public CourseInfo courseWeekCount(Integer courseWeekCount) {
        this.courseWeekCount = courseWeekCount;
        return this;
    }

    public void setCourseWeekCount(Integer courseWeekCount) {
        this.courseWeekCount = courseWeekCount;
    }

    public String getCourseMemo() {
        return courseMemo;
    }

    public CourseInfo courseMemo(String courseMemo) {
        this.courseMemo = courseMemo;
        return this;
    }

    public void setCourseMemo(String courseMemo) {
        this.courseMemo = courseMemo;
    }

    public ZonedDateTime getDataTime() {
        return dataTime;
    }

    public CourseInfo dataTime(ZonedDateTime dataTime) {
        this.dataTime = dataTime;
        return this;
    }

    public void setDataTime(ZonedDateTime dataTime) {
        this.dataTime = dataTime;
    }

    public String getCourseUser() {
        return courseUser;
    }

    public CourseInfo courseUser(String courseUser) {
        this.courseUser = courseUser;
        return this;
    }

    public String getCourseCover() {
        return courseCover;
    }

    public void setCourseCover(String courseCover) {
        this.courseCover = courseCover;
    }

    public void setCourseUser(String courseUser) {
        this.courseUser = courseUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseInfo)) {
            return false;
        }
        return id != null && id.equals(((CourseInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CourseInfo{" +
            "id=" + getId() +
            ", courseCode='" + getCourseCode() + "'" +
            ", courseName='" + getCourseName() + "'" +
            ", courseCount=" + getCourseCount() +
            ", courseWeekCount=" + getCourseWeekCount() +
            ", courseMemo='" + getCourseMemo() + "'" +
            ", dataTime='" + getDataTime() + "'" +
            ", courseUser='" + getCourseUser() + "'" +
            "}";
    }
}
