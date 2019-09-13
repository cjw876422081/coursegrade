package com.niitcoder.coursegrade.repository;

import com.niitcoder.coursegrade.domain.CoursePlan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CoursePlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoursePlanRepository extends JpaRepository<CoursePlan, Long> {

}
