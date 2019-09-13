package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.StudentHomework;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StudentHomework entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentHomeworkRepository extends JpaRepository<StudentHomework, Long> {

}
