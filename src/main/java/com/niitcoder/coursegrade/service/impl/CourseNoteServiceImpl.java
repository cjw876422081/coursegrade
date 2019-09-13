package com.niitcoder.coursegrade.service.impl;

import com.niitcoder.coursegrade.service.CourseNoteService;
import com.niitcoder.coursegrade.domain.CourseNote;
import com.niitcoder.coursegrade.repository.CourseNoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CourseNote}.
 */
@Service
@Transactional
public class CourseNoteServiceImpl implements CourseNoteService {

    private final Logger log = LoggerFactory.getLogger(CourseNoteServiceImpl.class);

    private final CourseNoteRepository courseNoteRepository;

    public CourseNoteServiceImpl(CourseNoteRepository courseNoteRepository) {
        this.courseNoteRepository = courseNoteRepository;
    }

    /**
     * Save a courseNote.
     *
     * @param courseNote the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CourseNote save(CourseNote courseNote) {
        log.debug("Request to save CourseNote : {}", courseNote);
        return courseNoteRepository.save(courseNote);
    }

    /**
     * Get all the courseNotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseNote> findAll(Pageable pageable) {
        log.debug("Request to get all CourseNotes");
        return courseNoteRepository.findAll(pageable);
    }


    /**
     * Get one courseNote by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CourseNote> findOne(Long id) {
        log.debug("Request to get CourseNote : {}", id);
        return courseNoteRepository.findById(id);
    }

    /**
     * Delete the courseNote by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseNote : {}", id);
        courseNoteRepository.deleteById(id);
    }
}
