package com.niitcoder.coursegrade.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A CourseGroup.
 */
@Entity
@Table(name = "course_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CourseGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_code")
    private String groupCode;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "group_count")
    private Integer groupCount;

    @Column(name = "data_time")
    private ZonedDateTime dataTime;

    @ManyToOne
    @JsonIgnoreProperties("courseGroups")
    private CourseInfo course;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public CourseGroup groupCode(String groupCode) {
        this.groupCode = groupCode;
        return this;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public CourseGroup groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupCount() {
        return groupCount;
    }

    public CourseGroup groupCount(Integer groupCount) {
        this.groupCount = groupCount;
        return this;
    }

    public void setGroupCount(Integer groupCount) {
        this.groupCount = groupCount;
    }

    public ZonedDateTime getDataTime() {
        return dataTime;
    }

    public CourseGroup dataTime(ZonedDateTime dataTime) {
        this.dataTime = dataTime;
        return this;
    }

    public void setDataTime(ZonedDateTime dataTime) {
        this.dataTime = dataTime;
    }

    public CourseInfo getCourse() {
        return course;
    }

    public CourseGroup course(CourseInfo courseInfo) {
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
        if (!(o instanceof CourseGroup)) {
            return false;
        }
        return id != null && id.equals(((CourseGroup) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CourseGroup{" +
            "id=" + getId() +
            ", groupCode='" + getGroupCode() + "'" +
            ", groupName='" + getGroupName() + "'" +
            ", groupCount=" + getGroupCount() +
            ", dataTime='" + getDataTime() + "'" +
            "}";
    }
}
