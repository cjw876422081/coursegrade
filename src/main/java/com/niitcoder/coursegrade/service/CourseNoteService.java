package com.niitcoder.coursegrade.service;

import com.niitcoder.coursegrade.domain.CourseNote;

import com.niitcoder.coursegrade.service.dto.CourseNoteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link CourseNote}.
 */
public interface CourseNoteService {

    /**
     * Save a courseNote.
     *
     * @param courseNote the entity to save.
     * @return the persisted entity.
     */
    CourseNote save(CourseNote courseNote);

    /**
     * Get all the courseNotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CourseNote> findAll(Pageable pageable);


    /**
     * Get the "id" courseNote.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CourseNote> findOne(Long id);

    /**
     * Delete the "id" courseNote.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * 根据课程获取根级笔记
     * @param courseId 课程Id
     * @param pageable 分页参数
     * @return
     */
    Page<CourseNote> findRootNoteByCourse(Long courseId,Pageable pageable);

    /**
     * 根据作业获取根级笔记
     * @param homeworkId
     * @param pageable
     * @return
     */
    Page<CourseNote> findRootNoteByHomewrok(Long homeworkId,Pageable pageable);

    /**
     * 根据授课计划获取根级笔记
     * @param planId
     * @param pageable
     * @return
     */
    Page<CourseNote> findRootNoteByCoursePlan(Long planId,Pageable pageable);

    /**
     * 将获取到的笔记列表封装成树形结构返回，如果有回复内容，递归获取所有回复
     * @param notes
     * @return
     */
    Page<CourseNoteDTO> getNoteTreeList(Page<CourseNote> notes);

    /**
     * 根据笔记entity，查找所有回复内容，组成树结构返回
     * @param note
     * @return
     */
    CourseNoteDTO getNoteItem(CourseNote note);

    /**
     * 根据笔记id，查找所有回复内容，组成树结构返回（递归）
     * @param noteId
     * @return
     */
    CourseNoteDTO getNoteItemById(Long noteId);

    List<CourseNoteDTO> getSubNotes(Long pid);
}
