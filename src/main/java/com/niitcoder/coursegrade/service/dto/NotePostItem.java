package com.niitcoder.coursegrade.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.niitcoder.coursegrade.domain.CourseHomework;
import com.niitcoder.coursegrade.domain.CourseInfo;
import com.niitcoder.coursegrade.domain.CourseNote;
import com.niitcoder.coursegrade.domain.CoursePlan;

import java.util.ArrayList;
import java.util.List;

public class NotePostItem {
    private Long id;

    private String noteType;

    private String noteMemo;


    private CourseInfo course;

    private CoursePlan plan;

    private CourseHomework homework;

    private CourseNote parentNote;

    List<FileInfo> files=new ArrayList<>();

    public List<FileInfo> getFiles() {
        return files;
    }

    public void setFiles(List<FileInfo> files) {
        this.files = files;
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
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

    public CourseInfo getCourse() {
        return course;
    }

    public void setCourse(CourseInfo course) {
        this.course = course;
    }

    public CoursePlan getPlan() {
        return plan;
    }

    public void setPlan(CoursePlan plan) {
        this.plan = plan;
    }

    public CourseHomework getHomework() {
        return homework;
    }

    public void setHomework(CourseHomework homework) {
        this.homework = homework;
    }

    public CourseNote getParentNote() {
        return parentNote;
    }

    public void setParentNote(CourseNote parentNote) {
        this.parentNote = parentNote;
    }
}
