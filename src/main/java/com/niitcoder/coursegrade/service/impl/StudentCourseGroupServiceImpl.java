package com.niitcoder.coursegrade.service.impl;

import com.niitcoder.coursegrade.domain.CourseGroup;
import com.niitcoder.coursegrade.domain.CourseInfo;
import com.niitcoder.coursegrade.repository.CourseGroupRepository;
import com.niitcoder.coursegrade.repository.CourseInfoRepository;
import com.niitcoder.coursegrade.security.SecurityUtils;
import com.niitcoder.coursegrade.service.CourseInfoService;
import com.niitcoder.coursegrade.service.StudentCourseGroupService;
import com.niitcoder.coursegrade.domain.StudentCourseGroup;
import com.niitcoder.coursegrade.repository.StudentCourseGroupRepository;
import com.niitcoder.coursegrade.service.StudentHomeworkService;
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
 * Service Implementation for managing {@link StudentCourseGroup}.
 */
@Service
@Transactional
public class StudentCourseGroupServiceImpl implements StudentCourseGroupService {
    private final Logger log = LoggerFactory.getLogger(StudentCourseGroupServiceImpl.class);

    private final StudentCourseGroupRepository studentCourseGroupRepository;
    private final CourseInfoRepository courseInfoRepository;
    private final CourseInfoService courseInfoService;
    private final CourseGroupRepository courseGroupRepository;
    private final StudentHomeworkService studentHomeworkService;
    private final JdbcTemplate jdbcTemplate ;

    public StudentCourseGroupServiceImpl(StudentCourseGroupRepository studentCourseGroupRepository, CourseInfoRepository courseInfoRepository, CourseInfoService courseInfoService, CourseGroupRepository courseGroupRepository, StudentHomeworkService studentHomeworkService, JdbcTemplate jdbcTemplate) {
        this.studentCourseGroupRepository = studentCourseGroupRepository;
        this.courseInfoRepository= courseInfoRepository;
        this.courseInfoService = courseInfoService;
        this.courseGroupRepository = courseGroupRepository;
        this.studentHomeworkService = studentHomeworkService;
        this.jdbcTemplate = jdbcTemplate;
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

    @Override
    public Page<StudentCourseGroup> findStudentByGroupId(Long id, Pageable pageable)throws Exception {
        log.debug("Request to findByCourseName  : {}", id);
        //检查班级是否存在
        Optional<CourseGroup> courseGroup=courseGroupRepository.findById(id);
        if(!courseGroup.isPresent()){
            throw new Exception("班级不存在.");
        }
        //检查登陆用户是否创建过课程
        if(!courseInfoService.checkLoginName()){
            throw new Exception("无权限搜索班级学生.");
        }
        //通过班级获得CourseInfo中的courseUser
        String courseUser=courseGroup.get().getCourse().getCourseUser();
        String loginName= SecurityUtils.getCurrentUserLogin().get();
        //判断courseUser与登陆名是否相同
        if(!courseUser.equals(loginName)){
            throw new Exception("无权限搜索该班级学生.");
        }
        List<StudentCourseGroup> studentCourseGroups=studentCourseGroupRepository.findByGroupId(id);
        return studentHomeworkService.listConvertToPage(studentCourseGroups,pageable);
    }


    @Override
    public void delete( String student , Long course_id) throws Exception {
        log.debug("Request to delete StudentCourseGroup : {}", course_id);
        List<CourseGroup> result = this.getCourseGroup(student, course_id);
        String sql = "delete from student_course_group where student =\"" + student + "\"  and group_id in (" +
            " select id from course_group where course_id = " +
            +course_id + ")";
        if (result.size() > 0) {
            jdbcTemplate.execute(sql);
        } else {
            throw new Exception("班级id 出错");
        }
    }

//    + " and id =(" +
//        "select group_id from student_course_group where student = \"" + student + "\"" +
//        ")) "
    @Override
    public List<CourseGroup> getCourseGroup(String student, Long course_id) throws Exception {
//        String sql = "select * from student_course_group where group_id = (" +
//            "select id from course_group where course_id = " + course_id +")" ;
//
//        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        List<CourseGroup> result = courseGroupRepository.findByCourseId(course_id) ;
        if (result.size() <= 0) {
            throw new Exception("没有查询到结果");
        } else {
            return result;
        }
    }


    @Override
    public List<Map<String, Object>> getMyCourse(String student) throws Exception {
        String sql = "SELECT * FROM course_info WHERE id=(" +
            "SELECT course_id FROM course_group WHERE id =(" +
            "SELECT group_id FROM student_course_group WHERE student = \""+ student+"\"))" ;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql) ;
        if( result.size() <= 0  ){
            throw new Exception("该用户没有加入任何课程");
        }
        return result;

    }

    public Page<CourseInfo> getStudentCourses(String login, Pageable page){
        return courseInfoRepository.findByCourseUser(login,page);
    }

}
