package com.niitcoder.coursegrade.service;

import com.niitcoder.coursegrade.domain.CourseInfo;
import com.niitcoder.coursegrade.domain.StudentCourseGroup;

import com.niitcoder.coursegrade.service.dto.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link StudentCourseGroup}.
 */
public interface StudentCourseGroupService {

    /**
     * Save a studentCourseGroup.
     *
     * @param studentCourseGroup the entity to save.
     * @return the persisted entity.
     */
    StudentCourseGroup save(StudentCourseGroup studentCourseGroup);

    /**
     * Get all the studentCourseGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StudentCourseGroup> findAll(Pageable pageable);


    /**
     * Get the "id" studentCourseGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StudentCourseGroup> findOne(Long id);

    /**
     * Get the "group" studentCourseGroup.
     *
     * @param group the group of the entity.
     * @return the entity.
     */
    Page<Student> findStudentByGroup(String group, Pageable pageable);

    Page<StudentCourseGroup> findStudentByGroup(Long id, Pageable pageable) throws Exception;

    /**
     * Delete the "id" studentCourseGroup.
     *
     * @param id the id of the entity.
     */
    void delete( String student, Long id) throws Exception;

    List<Map<String, Object>> getCourseGroup(String student , Long course_id ) throws Exception;

    List<Map<String, Object>> getMyCourse(String student ) throws Exception;

}
