package com.niitcoder.coursegrade.service.dto;

import io.swagger.annotations.ApiModelProperty;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 授课内容树形结构
 */
public class CoursePlanDTO {
    @ApiModelProperty(value = "授课内容Id")
    private Long id;

    @ApiModelProperty(value = "授课内容父级id")
    private Long pId;

    @ApiModelProperty(value = "授课内容")
    private String planMemo;

    @ApiModelProperty(value = "授课目标")
    private String planTarget;

    @ApiModelProperty(value = "课时数")
    private Integer planCount;

    @ApiModelProperty(value = "发布时间")
    private ZonedDateTime planTime;

    @ApiModelProperty(value = "所属课程")
    private Long course;

    @ApiModelProperty(value = "下级授课内容")
    private List<CoursePlanDTO> children = new ArrayList<CoursePlanDTO>();

    @ApiModelProperty(value = "是否叶子节点")
    private boolean leaf = false;

    public CoursePlanDTO() {
    }

    @Override
    public String toString() {
        return "CoursePlanDTO{" +
            "id=" + id +
            ", pId=" + pId +
            ", planMemo='" + planMemo + '\'' +
            ", planTarget='" + planTarget + '\'' +
            ", planCount='" + planCount + '\'' +
            ", planTime=" + planTime +
            ", course='" + course + '\'' +
            ", children=" + children +
            ", leaf=" + leaf +
            '}';
    }

    public CoursePlanDTO(Long id, Long pId, String planMemo, String planTarget, Integer planCount,
                         ZonedDateTime planTime, Long course, List<CoursePlanDTO> children, boolean leaf) {
        this.id = id;
        this.pId = pId;
        this.planMemo = planMemo;
        this.planTarget = planTarget;
        this.planCount = planCount;
        this.planTime = planTime;
        this.course = course;
        this.children = children;
        this.leaf = leaf;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public String getPlanMemo() {
        return planMemo;
    }

    public void setPlanMemo(String planMemo) {
        this.planMemo = planMemo;
    }

    public String getPlanTarget() {
        return planTarget;
    }

    public void setPlanTarget(String planTarget) {
        this.planTarget = planTarget;
    }

    public Integer getPlanCount() {
        return planCount;
    }

    public void setPlanCount(Integer planCount) {
        this.planCount = planCount;
    }

    public ZonedDateTime getPlanTime() {
        return planTime;
    }

    public void setPlanTime(ZonedDateTime planTime) {
        this.planTime = planTime;
    }

    public Long getCourse() {
        return course;
    }

    public void setCourse(Long course) {
        this.course = course;
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
}
