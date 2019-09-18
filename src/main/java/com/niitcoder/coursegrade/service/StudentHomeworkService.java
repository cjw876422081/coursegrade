package com.niitcoder.coursegrade.service;

import com.niitcoder.coursegrade.domain.CourseHomework;
import com.niitcoder.coursegrade.domain.StudentHomework;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link StudentHomework}.
 */
public interface StudentHomeworkService {

    /**
     * Save a studentHomework.
     *
     * @param studentHomework the entity to save.
     * @return the persisted entity.
     */
    StudentHomework save(StudentHomework studentHomework);

    /**
     * Get all the studentHomeworks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StudentHomework> findAll(Pageable pageable);


    /**
     * Get the "id" studentHomework.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StudentHomework> findOne(Long id);

    /**
     * Delete the "id" studentHomework.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    Integer getOrderCourseGrade(Integer homework , String  student) ;
}
