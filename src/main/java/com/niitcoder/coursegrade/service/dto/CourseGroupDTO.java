package com.niitcoder.coursegrade.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseGroupDTO {
    private Long id;

    private String groupCode;

    private String groupName;

    private Integer groupCount;

    private ZonedDateTime dataTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(Integer groupCount) {
        this.groupCount = groupCount;
    }

    public ZonedDateTime getDataTime() {
        return dataTime;
    }

    public void setDataTime(ZonedDateTime dataTime) {
        this.dataTime = dataTime;
    }

    public CourseGroupDTO() {
    }

    @Override
    public String toString() {
        return "CourseGroupDTO{" +
            "id=" + id +
            ", groupCode='" + groupCode + '\'' +
            ", groupName='" + groupName + '\'' +
            ", groupCount=" + groupCount +
            ", dataTime=" + dataTime +
            '}';
    }

}
