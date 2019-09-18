package com.niitcoder.coursegrade.service.impl;

import com.niitcoder.coursegrade.service.CourseInfoService;
import com.niitcoder.coursegrade.domain.CourseInfo;
import com.niitcoder.coursegrade.repository.CourseInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CourseInfo}.
 */
@Service
@Transactional
public class CourseInfoServiceImpl implements CourseInfoService {

    private final Logger log = LoggerFactory.getLogger(CourseInfoServiceImpl.class);

    private final CourseInfoRepository courseInfoRepository;

    public CourseInfoServiceImpl(CourseInfoRepository courseInfoRepository) {
        this.courseInfoRepository = courseInfoRepository;
    }

    /**
     * Save a courseInfo.
     *
     * @param courseInfo the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CourseInfo save(CourseInfo courseInfo) {
        log.debug("Request to save CourseInfo : {}", courseInfo);
        return courseInfoRepository.save(courseInfo);
    }

    /**
     * Get all the courseInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseInfo> findAll(Pageable pageable) {
        log.debug("Request to get all CourseInfos");
        return courseInfoRepository.findAll(pageable);
    }


    /**
     * Get one courseInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CourseInfo> getOrderInfo(Long id) {
        log.debug("Request to get CourseInfo : {}", id);
        return courseInfoRepository.findById(id);
    }

    /**
     * Delete the courseInfo by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseInfo : {}", id);
        courseInfoRepository.deleteById(id);
    }

    @Override
    public Page<CourseInfo> findByLogin(String login, Pageable page) {
        return courseInfoRepository.findByCourseUser(login,page);
    }
}
