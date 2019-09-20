package com.niitcoder.coursegrade.service;

import com.niitcoder.coursegrade.domain.CourseAttachment;

import com.niitcoder.coursegrade.service.dto.FileInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link CourseAttachment}.
 */
public interface CourseAttachmentService {

    /**
     * Save a courseAttachment.
     *
     * @param courseAttachment the entity to save.
     * @return the persisted entity.
     */
    CourseAttachment save(CourseAttachment courseAttachment);

    CourseAttachment save(String type, Long entity, FileInfo fileInfo);

    /**
     * Get all the courseAttachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CourseAttachment> findAll(Pageable pageable);


    /**
     * Get the "id" courseAttachment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CourseAttachment> findOne(Long id);

    /**
     * Delete the "id" courseAttachment.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * 查找作业附件
     * @param fileUser
     * @param homeworkId
     * @return
     */
    Optional<List<CourseAttachment>> getCourseAttachmentsByFileUserAndHomeworkId(String fileUser,Long homeworkId);

    /**
     * 删除作业附件
     * @param fileUser
     * @param homeworkId
     */
    void deleteByFileUserAndHomeworkId(String fileUser,Long homeworkId);

}
