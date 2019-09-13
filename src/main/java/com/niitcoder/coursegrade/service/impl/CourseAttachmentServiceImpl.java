package com.niitcoder.coursegrade.service.impl;

import com.niitcoder.coursegrade.service.CourseAttachmentService;
import com.niitcoder.coursegrade.domain.CourseAttachment;
import com.niitcoder.coursegrade.repository.CourseAttachmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CourseAttachment}.
 */
@Service
@Transactional
public class CourseAttachmentServiceImpl implements CourseAttachmentService {

    private final Logger log = LoggerFactory.getLogger(CourseAttachmentServiceImpl.class);

    private final CourseAttachmentRepository courseAttachmentRepository;

    public CourseAttachmentServiceImpl(CourseAttachmentRepository courseAttachmentRepository) {
        this.courseAttachmentRepository = courseAttachmentRepository;
    }

    /**
     * Save a courseAttachment.
     *
     * @param courseAttachment the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CourseAttachment save(CourseAttachment courseAttachment) {
        log.debug("Request to save CourseAttachment : {}", courseAttachment);
        return courseAttachmentRepository.save(courseAttachment);
    }

    /**
     * Get all the courseAttachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseAttachment> findAll(Pageable pageable) {
        log.debug("Request to get all CourseAttachments");
        return courseAttachmentRepository.findAll(pageable);
    }


    /**
     * Get one courseAttachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CourseAttachment> findOne(Long id) {
        log.debug("Request to get CourseAttachment : {}", id);
        return courseAttachmentRepository.findById(id);
    }

    /**
     * Delete the courseAttachment by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseAttachment : {}", id);
        courseAttachmentRepository.deleteById(id);
    }
}
