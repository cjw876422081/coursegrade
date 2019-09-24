package com.niitcoder.coursegrade.service.impl;

import com.niitcoder.coursegrade.domain.CourseAttachment;
import com.niitcoder.coursegrade.domain.StudentHomework;
import com.niitcoder.coursegrade.repository.CourseAttachmentRepository;
import com.niitcoder.coursegrade.repository.StudentHomeworkRepository;
import com.niitcoder.coursegrade.security.SecurityUtils;
import com.niitcoder.coursegrade.service.CourseHomeworkService;
import com.niitcoder.coursegrade.domain.CourseHomework;
import com.niitcoder.coursegrade.repository.CourseHomeworkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link CourseHomework}.
 */
@Service
@Transactional
public class CourseHomeworkServiceImpl implements CourseHomeworkService {

    private final Logger log = LoggerFactory.getLogger(CourseHomeworkServiceImpl.class);

    private final CourseHomeworkRepository courseHomeworkRepository;
    private final StudentHomeworkRepository studentHomeworkRepository;
    private final CourseAttachmentRepository courseAttachmentRepository;

    public CourseHomeworkServiceImpl(CourseHomeworkRepository courseHomeworkRepository, StudentHomeworkRepository studentHomeworkRepository, CourseAttachmentRepository courseAttachmentRepository) {
        this.courseHomeworkRepository = courseHomeworkRepository;
        this.studentHomeworkRepository = studentHomeworkRepository;
        this.courseAttachmentRepository = courseAttachmentRepository;
    }

    /**
     * Save a courseHomework.
     *
     * @param courseHomework the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CourseHomework save(CourseHomework courseHomework) {
        log.debug("Request to save CourseHomework : {}", courseHomework);
        return courseHomeworkRepository.save(courseHomework);
    }

    /**
     * Get all the courseHomeworks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CourseHomework> findAll(Pageable pageable) {
        log.debug("Request to get all CourseHomeworks");
        return courseHomeworkRepository.findAll(pageable);
    }


    /**
     * Get one courseHomework by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CourseHomework> findOne(Long id) {
        log.debug("Request to get CourseHomework : {}", id);
        return courseHomeworkRepository.findById(id);
    }

    /**
     * Delete the courseHomework by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) throws Exception {
        log.debug("Request to delete CourseHomework : {}", id);

        //当前id对应的作业是否是当前登录用户的
        Optional<CourseHomework> courseHomework = courseHomeworkRepository.findById(id);
        if (courseHomework.isPresent()) { //id是否可以查询到作业
            CourseHomework homework = courseHomework.get();
            String loginName = SecurityUtils.getCurrentUserLogin().get();
            if (!homework.getPlan().getCourse().getCourseUser().equals(loginName)) {
                throw new Exception("无权限删除此课程.");
            }
            //检查是否有学生提交作业
            List<StudentHomework> studentHomeworks = studentHomeworkRepository.findByHomeworkId(id);
            if (studentHomeworks != null && !studentHomeworks.isEmpty()) {
                throw new Exception("作业已经有提交记录");
            }
            //检查作业是否有附件
            List<CourseAttachment> courseAttachments = courseAttachmentRepository.findByHomeworkId(id);
            if (courseAttachments != null && !courseAttachments.isEmpty()) {
                throw new Exception("作业已有附件提交");
            }
            courseHomeworkRepository.deleteById(id);

        } else { //id对应的课程不存在
            throw new Exception("作业不存在");
        }

    }

    /**
     * 通过授课内容查找所有作业
     *
     * @param id
     * @return
     */
    @Override
    public List<CourseHomework> findByPlanId(Long id) {
        List<CourseHomework> courseHomework = courseHomeworkRepository.findByPlanId(id);
        return courseHomework;
    }

    @Override
    public List<CourseHomework> getAllTaskByCourse(Long course_id) {
        List<CourseHomework> courseHomework = new ArrayList<>();
        courseHomework = courseHomeworkRepository.findByCourseId(course_id);
        return courseHomework;
    }

    @Override
    public void updateTask(Long id, String homework_memo) {
        log.debug("REST request to update CourseHomework : {},{}", id,homework_memo);
        CourseHomework result=courseHomeworkRepository.findById(id).get();
        result.setHomeworkMemo(homework_memo);
    }


}
