package com.niitcoder.coursegrade.service;

import com.niitcoder.coursegrade.domain.CourseNote;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
}
