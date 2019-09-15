package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.CourseNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the CourseNote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseNoteRepository extends JpaRepository<CourseNote, Long> {
    Page<CourseNote> findByCourseId(Long courseId, Pageable pageable);
    Page<CourseNote> findByHomeworkId(Long homeworkId,Pageable pageable);
    Page<CourseNote> findByPlanId(Long planId,Pageable pageable);

    List<CourseNote> findByParentNoteId(Long pid);
}
