package com.niitcoder.coursegrade.service.impl;

import com.niitcoder.coursegrade.service.CourseHomeworkService;
import com.niitcoder.coursegrade.domain.CourseHomework;
import com.niitcoder.coursegrade.repository.CourseHomeworkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link CourseHomework}.
 */
@Service
@Transactional
public class CourseHomeworkServiceImpl implements CourseHomeworkService {

    private final Logger log = LoggerFactory.getLogger(CourseHomeworkServiceImpl.class);

    private final CourseHomeworkRepository courseHomeworkRepository;

    public CourseHomeworkServiceImpl(CourseHomeworkRepository courseHomeworkRepository) {
        this.courseHomeworkRepository = courseHomeworkRepository;
    }

    /**
     * Save a courseHomework.
     *
     * @param courseHomework the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CourseHomework save(CourseHomework courseHomework) {
        log.debug("Request to save CourseHomework : {}", courseHomework);
        return courseHomeworkRepository.save(courseHomework);
    }

    /**
     * Get all the courseHomeworks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseHomework> findAll(Pageable pageable) {
        log.debug("Request to get all CourseHomeworks");
        return courseHomeworkRepository.findAll(pageable);
    }


    /**
     * Get one courseHomework by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CourseHomework> findOne(Long id) {
        log.debug("Request to get CourseHomework : {}", id);
        return courseHomeworkRepository.findById(id);
    }

    /**
     * Delete the courseHomework by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseHomework : {}", id);
        courseHomeworkRepository.deleteById(id);
    }
    /**
     * Get one courseHomework by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public List<CourseHomework> findByPlanId(Long id) {
        List<CourseHomework> courseHomework = new ArrayList<>();
        courseHomework = courseHomeworkRepository.findByPlanId(id);
        return courseHomework;
    }
}
