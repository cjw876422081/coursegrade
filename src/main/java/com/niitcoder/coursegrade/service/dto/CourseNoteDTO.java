package com.niitcoder.coursegrade.service.dto;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 笔记树形结构
 */
public class CourseNoteDTO {
    private Long id;

    private Long pid;

    private String noteType;

    private String noteMemo;

    private ZonedDateTime noteTime;

    private String publishUser;

    /**
     * 子笔记，即回复的内容
     */
    private List<CourseNoteDTO> children=new ArrayList<CourseNoteDTO>();

    /**
     * 是否叶子节点
     */
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
