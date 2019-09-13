package com.niitcoder.coursegrade.service.impl;

import com.niitcoder.coursegrade.service.StudentCourseGroupService;
import com.niitcoder.coursegrade.domain.StudentCourseGroup;
import com.niitcoder.coursegrade.repository.StudentCourseGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link StudentCourseGroup}.
 */
@Service
@Transactional
public class StudentCourseGroupServiceImpl implements StudentCourseGroupService {

    private final Logger log = LoggerFactory.getLogger(StudentCourseGroupServiceImpl.class);

    private final StudentCourseGroupRepository studentCourseGroupRepository;

    public StudentCourseGroupServiceImpl(StudentCourseGroupRepository studentCourseGroupRepository) {
        this.studentCourseGroupRepository = studentCourseGroupRepository;
    }

    /**
     * Save a studentCourseGroup.
     *
     * @param studentCourseGroup the entity to save.
     * @return the persisted entity.
     */
    @Override
    public StudentCourseGroup save(StudentCourseGroup studentCourseGroup) {
        log.debug("Request to save StudentCourseGroup : {}", studentCourseGroup);
        return studentCourseGroupRepository.save(studentCourseGroup);
    }

    /**
     * Get all the studentCourseGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StudentCourseGroup> findAll(Pageable pageable) {
        log.debug("Request to get all StudentCourseGroups");
        return studentCourseGroupRepository.findAll(pageable);
    }


    /**
     * Get one studentCourseGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StudentCourseGroup> findOne(Long id) {
        log.debug("Request to get StudentCourseGroup : {}", id);
        return studentCourseGroupRepository.findById(id);
    }

    /**
     * Delete the studentCourseGroup by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StudentCourseGroup : {}", id);
        studentCourseGroupRepository.deleteById(id);
    }
}
