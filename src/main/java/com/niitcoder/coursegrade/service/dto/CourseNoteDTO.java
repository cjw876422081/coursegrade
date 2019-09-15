package com.niitcoder.coursegrade.service.dto;

import io.swagger.annotations.ApiModelProperty;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 笔记树形结构
 */
public class CourseNoteDTO {
    @ApiModelProperty(value = "笔记Id")
    private Long id;
    @ApiModelProperty(value = "笔记父级id")
    private Long pid;

    @ApiModelProperty(value = "笔记类型：课程，授课内容，作业")
    private String noteType;

    @ApiModelProperty(value = "笔记内容")
    private String noteMemo;
    @ApiModelProperty(value = "发布时间")
    private ZonedDateTime noteTime;
    @ApiModelProperty(value = "发布者")
    private String publishUser;

    @ApiModelProperty(value = "下级笔记，即回复的笔记")
    private List<CourseNoteDTO> children=new ArrayList<CourseNoteDTO>();

    @ApiModelProperty(value = "是否叶子节点")
    private boolean leaf=false;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public String getNoteMemo() {
        return noteMemo;
    }

    public void setNoteMemo(String noteMemo) {
        this.noteMemo = noteMemo;
    }

    public ZonedDateTime getNoteTime() {
        return noteTime;
    }

    public void setNoteTime(ZonedDateTime noteTime) {
        this.noteTime = noteTime;
    }

    public String getPublishUser() {
        return publishUser;
    }

    public void setPublishUser(String publishUser) {
        this.publishUser = publishUser;
    }

    public List<CourseNoteDTO> getChildren() {
        return children;
    }

    public void setChildren(List<CourseNoteDTO> children) {
        this.children = children;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

}
