package com.niitcoder.coursegrade.service;

import com.niitcoder.coursegrade.domain.CourseHomework;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link CourseHomework}.
 */
public interface CourseHomeworkService {

    /**
     * Save a courseHomework.
     *
     * @param courseHomework the entity to save.
     * @return the persisted entity.
     */
    CourseHomework save(CourseHomework courseHomework);

    /**
     * Get all the courseHomeworks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CourseHomework> findAll(Pageable pageable);


    /**
     * Get the "id" courseHomework.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CourseHomework> findOne(Long id);

    /**
     * Delete the "id" courseHomework.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
