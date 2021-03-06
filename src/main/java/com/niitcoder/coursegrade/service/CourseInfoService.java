package com.niitcoder.coursegrade.service;

import com.niitcoder.coursegrade.domain.CourseInfo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link CourseInfo}.
 */
public interface CourseInfoService {

    /**
     * Save a courseInfo.
     *
     * @param courseInfo the entity to save.
     * @return the persisted entity.
     */
    CourseInfo save(CourseInfo courseInfo);

    /**
     * Get all the courseInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CourseInfo> findAll(Pageable pageable);

    /**
     * Delete the "id" courseInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id) throws Exception;

    Page<CourseInfo> findByLogin(String login,Pageable page);

    CourseInfo findById(Long id) throws Exception;

    boolean checkLoginName() throws Exception;

    CourseInfo isExistCourseInfo(Long courseInfoId) throws Exception;

    boolean isCreateByLogin(Long courseInfoId) throws Exception;

}
