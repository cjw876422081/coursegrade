package com.niitcoder.coursegrade.service.impl;

import com.niitcoder.coursegrade.domain.*;
import com.niitcoder.coursegrade.repository.*;
import com.niitcoder.coursegrade.security.SecurityUtils;
import com.niitcoder.coursegrade.service.CourseInfoService;
import com.niitcoder.coursegrade.service.StudentHomeworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
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
    private final UserRepository userRepository;
    private final CourseInfoService courseInfoRepository;
    private final CourseHomeworkRepository courseHomeworkRepository;
    private final CourseGroupRepository courseGroupRepository;
    private final StudentCourseGroupRepository studentCourseGroupRepository;
    private final CourseAttachmentRepository courseAttachmentRepository;
    private final JdbcTemplate jdbcTemplate;

    public StudentHomeworkServiceImpl(StudentHomeworkRepository studentHomeworkRepository, UserRepository userRepository, CourseInfoService courseInfoRepository, CourseHomeworkRepository courseHomeworkRepository, CourseGroupRepository courseGroupRepository, StudentCourseGroupRepository studentCourseGroupRepository, CourseAttachmentRepository courseAttachmentRepository, JdbcTemplate jdbcTemplate) {
        this.studentHomeworkRepository = studentHomeworkRepository;
        this.userRepository = userRepository;
        this.courseInfoRepository = courseInfoRepository;
        this.courseHomeworkRepository = courseHomeworkRepository;
        this.courseGroupRepository = courseGroupRepository;
        this.studentCourseGroupRepository = studentCourseGroupRepository;
        this.courseAttachmentRepository = courseAttachmentRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Save a studentHomework.
     *
     * @param studentHomework the entity to save.
     * @return the persisted entity.
     */
    @Override
    public StudentHomework save(StudentHomework studentHomework) throws Exception {
        log.debug("Request to save StudentHomework : {}", studentHomework);
        String loginName = SecurityUtils.getCurrentUserLogin().get();
        if (studentHomework.getHomework()==null){
            throw new Exception("请选择一个作业进行提交");
        }
        Long homeworkId = studentHomework.getHomework().getId();
        // 根据作业id查询出作业
        Optional<CourseHomework> courseHomework = courseHomeworkRepository.findById(homeworkId);
        // 作业id对应一个课程
        Long courseId = courseHomework.get().getPlan().getCourse().getId();
        // 根据课程查询出班级
        List<CourseGroup> courseGroups = courseGroupRepository.findByCourseId(courseId);
        Long groupId = courseGroups.get(0).getId();
        // 根据登录名和班级id查询表
        Optional<StudentCourseGroup> studentCourseGroup = studentCourseGroupRepository.findByIdAndStudent(groupId,loginName);
        if (studentCourseGroup.isPresent()){
            studentHomework.setStudent(loginName);
            studentHomework.setSubmitTime(ZonedDateTime.now());
            studentHomeworkRepository.save(studentHomework);
            return studentHomework;
        }
        throw new Exception("提交的作业不属于你的课程");
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
    public void delete(Long id) throws Exception {
        log.debug("Request to delete StudentHomework : {}", id);
        String loginName = SecurityUtils.getCurrentUserLogin().get();
        // 判断要删除的已提交作业是否为自己的
        Optional<StudentHomework> studentHomework = studentHomeworkRepository.findByIdAndStudent(id,loginName);
        if (studentHomework.isPresent()) {
            // 查询已提交作业是否携带附件
            Long homeworkId = studentHomework.get().getHomework().getId();
            Optional<List<CourseAttachment>> courseAttachment = courseAttachmentRepository.findCourseAttachmentsByFileUserAndHomework_Id(loginName,homeworkId);
            if (courseAttachment.isPresent()) {
                // 删除附件
                courseAttachmentRepository.deleteCourseAttachmentByFileUserAndHomework_Id(loginName,homeworkId);
            }
            studentHomeworkRepository.deleteById(id);
        }else {
        throw new Exception("不能删除别人的已提交作业");
        }
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

    /**
     * list转page
     * @param list
     * @param pageable
     * @param <T>
     * @return
     */
    public <T> Page<T> listConvertToPage(List<T> list, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : ( start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }
    @Override
    public Page<StudentHomework> findHomeworkByStudentId(Long studentId,Pageable pageable) throws Exception{
        //检查学生是否存在
        Optional<User> student=userRepository.findById(studentId);
        if(!student.isPresent()) {
            throw new Exception("学生不存在");
        }
        //检查学生所在班级
        String studentLogin=student.get().getLogin();
        Optional<StudentCourseGroup> studentCourseGroup=studentCourseGroupRepository.findByStudent(studentLogin);
        if(!studentCourseGroup.isPresent()) {
            throw new Exception("未找到学生所在班级");
        }
        //通过studentCourseGroup找到班级对应的课程
        CourseInfo courseInfo=studentCourseGroup.get().getGroup().getCourse();
        String loginName= SecurityUtils.getCurrentUserLogin().get();
        if(!courseInfo.getCourseUser().equals(loginName)){
            throw new Exception("无权限查询该学生作业");
        }
        //通过student和courseId找到studentHomework
        Long courseId=courseInfo.getId();
        List<StudentHomework> studentHomeworkList=studentHomeworkRepository.findByStudentAndHomeworkPlanCourseId(studentLogin,courseId,pageable);
        return listConvertToPage(studentHomeworkList,pageable);
    }

    @Override
    public Page<StudentHomework> getStudentHomeworkByCourseHomeworkId(Long id,Pageable pageable) throws Exception{
        log.debug("Request to get StudentHomework : {}", id);
        Optional<CourseHomework> courseHomework=courseHomeworkRepository.findById(id);
        if(!courseHomework.isPresent()){
            throw new Exception("该作业不存在");
        }
        String loginName= SecurityUtils.getCurrentUserLogin().get();
        String courseUser=courseHomework.get().getPlan().getCourse().getCourseUser();
        if (!courseUser.equals(loginName)) {
            throw new Exception("无权查询此作业");
        }
        List<StudentHomework> studentHomeworks=studentHomeworkRepository.findByHomeworkId(id);
        return listConvertToPage(studentHomeworks, pageable);

    }
    @Override
    public Optional<StudentHomework> updateStudentHomeworkGrade(Long id, Integer grade) throws Exception {
        log.debug("Request to update StudentHomeworkGrade : {},{}", id,grade);
        Optional<StudentHomework> studentHomework=studentHomeworkRepository.findById(id);
        if(!studentHomework.isPresent()){
            throw new Exception("该提交作业不存在");
        }
        String loginName = SecurityUtils.getCurrentUserLogin().get();
        String teacher=studentHomework.get().getHomework().getPlan().getCourse().getCourseUser();
        if(loginName.equals(teacher)){
            String sql="UPDATE student_homework SET grade="+grade+" WHERE id="+id;
            this.jdbcTemplate.update(sql);
            return studentHomeworkRepository.findById(id);
        }else{
            throw new Exception("无权限对此作业评分");
        }
    }
}
