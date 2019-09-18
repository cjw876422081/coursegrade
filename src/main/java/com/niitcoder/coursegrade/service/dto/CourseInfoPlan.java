package com.niitcoder.coursegrade.service.dto;

import io.swagger.annotations.ApiModelProperty;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程信息树形结构
 */
public class CourseInfoPlan {
    private Long id;

    @ApiModelProperty(value = "课程代码")
    private String courseCode;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    private Integer courseCount;

    private Integer courseWeekCount;

    private String courseMemo;

    private ZonedDateTime dataTime;

    private String courseUser;

    @ApiModelProperty(value = "下级授课内容")
    private List<CoursePlanDTO> children = new ArrayList<>();

    @ApiModelProperty(value = "是否叶子节点")
    private boolean leaf = false;

    public CourseInfoPlan(Long id, String courseCode, String courseName, Integer courseCount, Integer courseWeekCount,
                          String courseMemo, ZonedDateTime dataTime, String courseUser, List<CoursePlanDTO> children, boolean leaf) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.courseCount = courseCount;
        this.courseWeekCount = courseWeekCount;
        this.courseMemo = courseMemo;
        this.dataTime = dataTime;
        this.courseUser = courseUser;
        this.children = children;
        this.leaf = leaf;
    }

    public CourseInfoPlan() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(Integer courseCount) {
        this.courseCount = courseCount;
    }

    public Integer getCourseWeekCount() {
        return courseWeekCount;
    }

    public void setCourseWeekCount(Integer courseWeekCount) {
        this.courseWeekCount = courseWeekCount;
    }

    public String getCourseMemo() {
        return courseMemo;
    }

    public void setCourseMemo(String courseMemo) {
        this.courseMemo = courseMemo;
    }

    public ZonedDateTime getDataTime() {
        return dataTime;
    }

    public void setDataTime(ZonedDateTime dataTime) {
        this.dataTime = dataTime;
    }

    public String getCourseUser() {
        return courseUser;
    }

    public void setCourseUser(String courseUser) {
        this.courseUser = courseUser;
    }

    public List<CoursePlanDTO> getChildren() {
        return children;
    }

    public void setChildren(List<CoursePlanDTO> children) {
        this.children = children;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    @Override
    public String toString() {
        return "CourseInfoPlan{" +
            "id=" + id +
            ", courseCode='" + courseCode + '\'' +
            ", courseName='" + courseName + '\'' +
            ", courseCount=" + courseCount +
            ", courseWeekCount=" + courseWeekCount +
            ", courseMemo='" + courseMemo + '\'' +
            ", dataTime=" + dataTime +
            ", courseUser='" + courseUser + '\'' +
            ", children=" + children +
            ", leaf=" + leaf +
            '}';
    }
}
