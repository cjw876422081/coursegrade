package com.niitcoder.coursegrade.service.impl;

import com.niitcoder.coursegrade.service.CoursePlanService;
import com.niitcoder.coursegrade.domain.CoursePlan;
import com.niitcoder.coursegrade.repository.CoursePlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CoursePlan}.
 */
@Service
@Transactional
public class CoursePlanServiceImpl implements CoursePlanService {

    private final Logger log = LoggerFactory.getLogger(CoursePlanServiceImpl.class);

    private final CoursePlanRepository coursePlanRepository;

    public CoursePlanServiceImpl(CoursePlanRepository coursePlanRepository) {
        this.coursePlanRepository = coursePlanRepository;
    }

    /**
     * Save a coursePlan.
     *
     * @param coursePlan the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CoursePlan save(CoursePlan coursePlan) {
        log.debug("Request to save CoursePlan : {}", coursePlan);
        return coursePlanRepository.save(coursePlan);
    }

    /**
     * Get all the coursePlans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CoursePlan> findAll(Pageable pageable) {
        log.debug("Request to get all CoursePlans");
        return coursePlanRepository.findAll(pageable);
    }


    /**
     * Get one coursePlan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CoursePlan> findOne(Long id) {
        log.debug("Request to get CoursePlan : {}", id);
        return coursePlanRepository.findById(id);
    }

    /**
     * Delete the coursePlan by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CoursePlan : {}", id);
        coursePlanRepository.deleteById(id);
    }
}
