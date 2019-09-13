package com.niitcoder.coursegrade.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A CourseNote.
 */
@Entity
@Table(name = "course_note")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CourseNote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "note_type")
    private String noteType;

    @Column(name = "note_memo")
    private String noteMemo;

    @Column(name = "note_time")
    private ZonedDateTime noteTime;

    @Column(name = "publish_user")
    private String publishUser;

    @ManyToOne
    @JsonIgnoreProperties("courseNotes")
    private CourseInfo course;

    @ManyToOne
    @JsonIgnoreProperties("courseNotes")
    private CoursePlan plan;

    @ManyToOne
    @JsonIgnoreProperties("courseNotes")
    private CourseHomework homework;

    @ManyToOne
    @JsonIgnoreProperties("courseNotes")
    private CourseNote parentNote;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNoteType() {
        return noteType;
    }

    public CourseNote noteType(String noteType) {
        this.noteType = noteType;
        return this;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public String getNoteMemo() {
        return noteMemo;
    }

    public CourseNote noteMemo(String noteMemo) {
        this.noteMemo = noteMemo;
        return this;
    }

    public void setNoteMemo(String noteMemo) {
        this.noteMemo = noteMemo;
    }

    public ZonedDateTime getNoteTime() {
        return noteTime;
    }

    public CourseNote noteTime(ZonedDateTime noteTime) {
        this.noteTime = noteTime;
        return this;
    }

    public void setNoteTime(ZonedDateTime noteTime) {
        this.noteTime = noteTime;
    }

    public String getPublishUser() {
        return publishUser;
    }

    public CourseNote publishUser(String publishUser) {
        this.publishUser = publishUser;
        return this;
    }

    public void setPublishUser(String publishUser) {
        this.publishUser = publishUser;
    }

    public CourseInfo getCourse() {
        return course;
    }

    public CourseNote course(CourseInfo courseInfo) {
        this.course = courseInfo;
        return this;
    }

    public void setCourse(CourseInfo courseInfo) {
        this.course = courseInfo;
    }

    public CoursePlan getPlan() {
        return plan;
    }

    public CourseNote plan(CoursePlan coursePlan) {
        this.plan = coursePlan;
        return this;
    }

    public void setPlan(CoursePlan coursePlan) {
        this.plan = coursePlan;
    }

    public CourseHomework getHomework() {
        return homework;
    }

    public CourseNote homework(CourseHomework courseHomework) {
        this.homework = courseHomework;
        return this;
    }

    public void setHomework(CourseHomework courseHomework) {
        this.homework = courseHomework;
    }

    public CourseNote getParentNote() {
        return parentNote;
    }

    public CourseNote parentNote(CourseNote courseNote) {
        this.parentNote = courseNote;
        return this;
    }

    public void setParentNote(CourseNote courseNote) {
        this.parentNote = courseNote;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseNote)) {
            return false;
        }
        return id != null && id.equals(((CourseNote) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CourseNote{" +
            "id=" + getId() +
            ", noteType='" + getNoteType() + "'" +
            ", noteMemo='" + getNoteMemo() + "'" +
            ", noteTime='" + getNoteTime() + "'" +
            ", publishUser='" + getPublishUser() + "'" +
            "}";
    }
}
