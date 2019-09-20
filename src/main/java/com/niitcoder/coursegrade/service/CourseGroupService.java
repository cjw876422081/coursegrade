package com.niitcoder.coursegrade.service;

import com.niitcoder.coursegrade.domain.CourseGroup;

import com.niitcoder.coursegrade.domain.CourseInfo;
import com.niitcoder.coursegrade.service.dto.CourseGroupDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link CourseGroup}.
 */
public interface CourseGroupService {

    /**
     * Save a courseGroup.
     *
     * @param courseGroup the entity to save.
     * @return the persisted entity.
     */
    CourseGroup save(CourseGroup courseGroup) throws Exception;


    List<CourseGroup> findAll() throws Exception;

    /**
     * Get the "id" courseGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CourseGroup> findOne(Long id) throws Exception;

    List<CourseGroup> findByCourseId(Long id) throws Exception;
    /**
     * Delete the "id" courseGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id) throws Exception;
}
