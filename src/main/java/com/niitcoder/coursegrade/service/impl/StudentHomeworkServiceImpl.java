package com.niitcoder.coursegrade.service.impl;

import com.niitcoder.coursegrade.service.StudentHomeworkService;
import com.niitcoder.coursegrade.domain.StudentHomework;
import com.niitcoder.coursegrade.repository.StudentHomeworkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Implementation for managing {@link StudentHomework}.
 */
@Service
@Transactional
public class StudentHomeworkServiceImpl implements StudentHomeworkService {

    private final Logger log = LoggerFactory.getLogger(StudentHomeworkServiceImpl.class);

    private final StudentHomeworkRepository studentHomeworkRepository;
    private final JdbcTemplate jdbcTemplate;

    public StudentHomeworkServiceImpl(StudentHomeworkRepository studentHomeworkRepository, JdbcTemplate jdbcTemplate) {
        this.studentHomeworkRepository = studentHomeworkRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Save a studentHomework.
     *
     * @param studentHomework the entity to save.
     * @return the persisted entity.
     */
    @Override
    public StudentHomework save(StudentHomework studentHomework) {
        log.debug("Request to save StudentHomework : {}", studentHomework);
        return studentHomeworkRepository.save(studentHomework);
    }

    /**
     * Get all the studentHomeworks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StudentHomework> findAll(Pageable pageable) {
        log.debug("Request to get all StudentHomeworks");
        return studentHomeworkRepository.findAll(pageable);
    }


    /**
     * Get one studentHomework by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StudentHomework> findOne(Long id) {
        log.debug("Request to get StudentHomework : {}", id);
        return studentHomeworkRepository.findById(id);
    }

    /**
     * Delete the studentHomework by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StudentHomework : {}", id);
        studentHomeworkRepository.deleteById(id);
    }

    @Override
    public Integer getOrderCourseGrade(Integer homework, String student) {
        log.debug("Request to delete StudentHomework : {}", homework , student);
        String sql = "select grade from student_homework where homework_id=" +homework +" and student = "+ "\""+student +"\"";
        List<Map<String, Object>> sqlResult =jdbcTemplate.queryForList(sql);
        if (!sqlResult.isEmpty()){
            return (Integer) sqlResult.get(0).get("grade");
        }
        return 0 ;
    }


}
