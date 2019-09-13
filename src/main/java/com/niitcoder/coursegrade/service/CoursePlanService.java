package com.niitcoder.coursegrade.service;

import com.niitcoder.coursegrade.domain.CoursePlan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link CoursePlan}.
 */
public interface CoursePlanService {

    /**
     * Save a coursePlan.
     *
     * @param coursePlan the entity to save.
     * @return the persisted entity.
     */
    CoursePlan save(CoursePlan coursePlan);

    /**
     * Get all the coursePlans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CoursePlan> findAll(Pageable pageable);


    /**
     * Get the "id" coursePlan.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CoursePlan> findOne(Long id);

    /**
     * Delete the "id" coursePlan.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
