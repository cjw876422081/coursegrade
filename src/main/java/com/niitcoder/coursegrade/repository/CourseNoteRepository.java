package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.CourseNote;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CourseNote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseNoteRepository extends JpaRepository<CourseNote, Long> {

}
