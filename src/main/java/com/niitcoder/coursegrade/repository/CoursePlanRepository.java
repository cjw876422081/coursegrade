package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.CoursePlan;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the CoursePlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoursePlanRepository extends JpaRepository<CoursePlan, Long> {
    List<CoursePlan> findByParentPlanId(Long id);

    List<CoursePlan> findByCourseId(Long courseId);
}
