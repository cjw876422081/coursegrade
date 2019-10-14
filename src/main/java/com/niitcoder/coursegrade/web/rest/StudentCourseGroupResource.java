package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.service.CourseGroupService;
import com.niitcoder.coursegrade.service.CourseInfoService;
import com.niitcoder.coursegrade.domain.CourseGroup;
import com.niitcoder.coursegrade.domain.CourseInfo;
import com.niitcoder.coursegrade.security.SecurityUtils;
import com.niitcoder.coursegrade.service.dto.Student;
import com.niitcoder.coursegrade.domain.StudentCourseGroup;
import com.niitcoder.coursegrade.service.StudentCourseGroupService;
import com.niitcoder.coursegrade.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing {@link com.niitcoder.coursegrade.domain.StudentCourseGroup}.
 */
@RestController
@RequestMapping("/api")
public class StudentCourseGroupResource {

    private final Logger log = LoggerFactory.getLogger(StudentCourseGroupResource.class);

    private final CourseInfoService courseInfoService;

    private final CourseGroupService courseGroupService;

    private static final String ENTITY_NAME = "studentCourseGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentCourseGroupService studentCourseGroupService;

    public StudentCourseGroupResource(StudentCourseGroupService studentCourseGroupService,CourseInfoService courseInfoService,CourseGroupService courseGroupService) {
        this.studentCourseGroupService = studentCourseGroupService;
        this.courseInfoService = courseInfoService;
        this.courseGroupService=courseGroupService;
    }

    /**
     * {@code POST  /student-course-groups} : Create a new studentCourseGroup.
     *
     * @param studentCourseGroup the studentCourseGroup to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentCourseGroup, or with status {@code 400 (Bad Request)} if the studentCourseGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/student-course-groups")
    public ResponseEntity<StudentCourseGroup> createStudentCourseGroup(@RequestBody StudentCourseGroup studentCourseGroup) throws URISyntaxException {
        log.debug("REST request to save StudentCourseGroup : {}", studentCourseGroup);
        if (studentCourseGroup.getId() != null) {
            throw new BadRequestAlertException("A new studentCourseGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudentCourseGroup result = studentCourseGroupService.save(studentCourseGroup);
        return ResponseEntity.created(new URI("/api/student-course-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /student-course-groups} : Updates an existing studentCourseGroup.
     *
     * @param studentCourseGroup the studentCourseGroup to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentCourseGroup,
     * or with status {@code 400 (Bad Request)} if the studentCourseGroup is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentCourseGroup couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/student-course-groups")
    public ResponseEntity<StudentCourseGroup> updateStudentCourseGroup(@RequestBody StudentCourseGroup studentCourseGroup) throws URISyntaxException {
        log.debug("REST request to update StudentCourseGroup : {}", studentCourseGroup);
        if (studentCourseGroup.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StudentCourseGroup result = studentCourseGroupService.save(studentCourseGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentCourseGroup.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /student-course-groups} : get all the studentCourseGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentCourseGroups in body.
     */
    @GetMapping("/student-course-groups")
    public ResponseEntity getAllStudentCourseGroups(Pageable pageable) {
        log.debug("REST request to get a page of StudentCourseGroups");
        Page<StudentCourseGroup> page = studentCourseGroupService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * {@code GET  /student-course-groups/:id} : get the "id" studentCourseGroup.
     *
     * @param id the id of the studentCourseGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentCourseGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-course-groups/{id}")
    public ResponseEntity<StudentCourseGroup> getStudentCourseGroup(@PathVariable Long id) {
        log.debug("REST request to get StudentCourseGroup : {}", id);
        Optional<StudentCourseGroup> studentCourseGroup = studentCourseGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studentCourseGroup);
    }

    @GetMapping("/student-course-groups/")
    @ApiOperation(value = "查询指定班级已加入的学生名单,2组")
    public ResponseEntity<Page<StudentCourseGroup>> getStudentByCourseGroup(@RequestParam Long id, Pageable pageable) {
        log.debug("REST request to get StudentCourseGroup : {}", id);
        try {
            Page<StudentCourseGroup> studentCourseGroups = studentCourseGroupService.findStudentByGroupId(id, pageable);
            return ResponseEntity.ok(studentCourseGroups);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "not found");
        }
    }

    @ApiOperation("退出指定课程的班级")
    @DeleteMapping("/student-course-groups/{course_id}")
    public ResponseEntity deleteStudentCourseGroup(@PathVariable Long course_id) {
        String student = SecurityUtils.getCurrentUserLogin().get();
        log.debug("REST request to delete StudentCourseGroup : {}", course_id);
        try {
            studentCourseGroupService.delete(student, course_id);
        } catch (Exception e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "course_id error");
        }
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName,
            true, ENTITY_NAME,
            course_id.toString())).build();
    }

    @ApiOperation(value ="查询指定课程的班级")
    @GetMapping("/student-course-order-group/{course_id}")
    public  ResponseEntity getCourseGroup(@PathVariable Long course_id ) {
        String student = SecurityUtils.getCurrentUserLogin().get();
        log.debug("REST request to get  getCourseGroup : {}", course_id, student);
        List<CourseGroup> result = null;
        try {
            result = studentCourseGroupService.getCourseGroup(student, course_id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, " no found courseGroup");
        }
    }

    @ApiOperation("查看自己已经加入的课程")
    @GetMapping("/student-course-group/student")
    public ResponseEntity getMyCourse(Pageable pageable) {
        String student = SecurityUtils.getCurrentUserLogin().get();
        log.debug("REST request to get getMyCourse : {}", student);
        Page result = null;
        try {
            result = studentCourseGroupService.getMyCourse(student ,pageable);
            return  ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "not found joining courses");
        }
    }

    @ApiOperation("根据当前登录的学生，获取学生已加入的课程")
    @GetMapping("/student-course-infos/student")
    public ResponseEntity getStudentCourses(Pageable pageable) {
        log.debug("REST request to get a page of CourseInfos");
        String loginName= SecurityUtils.getCurrentUserLogin().get();
        Page<CourseInfo> page = courseInfoService.findByLogin(loginName, pageable);
        return ResponseEntity.ok(page);
    }

    @ApiOperation("根据班级id加入课程")
    @PostMapping("/student-course-group/groupId")
    public ResponseEntity joinCourse(String groupCode){
        log.debug("REST request to join a group");
        StudentCourseGroup studentCourseGroup=new StudentCourseGroup();
        CourseGroup courseGroup= courseGroupService.findByGroupcode(groupCode);
        studentCourseGroup.setGroup(courseGroup);
        studentCourseGroup.setJoinTime(ZonedDateTime.now());
        boolean flag=studentCourseGroupService.joinCourse(studentCourseGroup);
        return ResponseEntity.ok(flag);
    }
}
