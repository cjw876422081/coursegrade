package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.domain.CourseAttachment;
import com.niitcoder.coursegrade.domain.StudentHomework;
import com.niitcoder.coursegrade.service.CourseAttachmentService;
import com.niitcoder.coursegrade.service.StudentHomeworkService;
import com.niitcoder.coursegrade.service.dto.StudentHomewrokDTO;
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
import com.niitcoder.coursegrade.security.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.niitcoder.coursegrade.domain.StudentHomework}.
 */
@RestController
@RequestMapping("/api")
public class StudentHomeworkResource {

    private final Logger log = LoggerFactory.getLogger(StudentHomeworkResource.class);

    private static final String ENTITY_NAME = "studentHomework";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentHomeworkService studentHomeworkService;

    private final CourseAttachmentService courseAttachmentService;

    public StudentHomeworkResource(StudentHomeworkService studentHomeworkService, CourseAttachmentService courseAttachmentService) {
        this.studentHomeworkService = studentHomeworkService;
        this.courseAttachmentService = courseAttachmentService;
    }

    /**
     * {@code POST  /student-homeworks} : Create a new studentHomework.
     *
     * @param studentHomework the studentHomework to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studentHomework, or with status {@code 400 (Bad Request)} if the studentHomework has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/student-homeworks")
    public ResponseEntity<StudentHomework> createStudentHomework(@RequestBody StudentHomework studentHomework) throws URISyntaxException {
        log.debug("REST request to save StudentHomework : {}", studentHomework);
        if (studentHomework.getId() != null) {
            throw new BadRequestAlertException("A new studentHomework cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudentHomework result = studentHomeworkService.save(studentHomework);
        return ResponseEntity.created(new URI("/api/student-homeworks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /student-homeworks} : Updates an existing studentHomework.
     *
     * @param studentHomework the studentHomework to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studentHomework,
     * or with status {@code 400 (Bad Request)} if the studentHomework is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studentHomework couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/student-homeworks")
    public ResponseEntity<StudentHomework> updateStudentHomework(@RequestBody StudentHomework studentHomework) throws URISyntaxException {
        log.debug("REST request to update StudentHomework : {}", studentHomework);
        if (studentHomework.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StudentHomework result = studentHomeworkService.save(studentHomework);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentHomework.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /student-homeworks} : get all the studentHomeworks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studentHomeworks in body.
     */
    @GetMapping("/student-homeworks")
    public ResponseEntity<List<StudentHomework>> getAllStudentHomeworks(Pageable pageable) {
        log.debug("REST request to get a page of StudentHomeworks");
        Page<StudentHomework> page = studentHomeworkService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /student-homeworks/:id} : get the "id" studentHomework.
     *
     * @param id the id of the studentHomework to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentHomework, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-homeworks/{id}")
    public ResponseEntity<StudentHomework> getStudentHomework(@PathVariable Long id) {
        log.debug("REST request to get StudentHomework : {}", id);
        Optional<StudentHomework> studentHomework = studentHomeworkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(studentHomework);
    }

    /**
     * {@code DELETE  /student-homeworks/:id} : delete the "id" studentHomework.
     *
     * @param id the id of the studentHomework to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/student-homeworks/{id}")
    public ResponseEntity<Void> deleteStudentHomework(@PathVariable Long id) {
        log.debug("REST request to delete StudentHomework : {}", id);
        Optional<StudentHomework> studentHomework = studentHomeworkService.findOne(id);
        Long homeworkId = studentHomework.get().getHomework().getId();
        String loginName = SecurityUtils.getCurrentUserLogin().get();
        // 查找提交作业时所携带的附件 如果有则删除
        Optional<List<CourseAttachment>> courseAttachments = courseAttachmentService.getCourseAttachmentsByFileUserAndHomeworkId(loginName, homeworkId);
        if (courseAttachments.isPresent()) {
            courseAttachmentService.deleteByFileUserAndHomeworkId(loginName, homeworkId);
        }
        studentHomeworkService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();

    }

    @GetMapping("/student-homeworks-grade/{homework}/{student}")
    public ResponseEntity<Integer> getStudentHomework(@PathVariable Integer homework, @PathVariable String student) {
        log.debug("REST request to get StudentHomework : {}", homework, student);
        Integer result = studentHomeworkService.getOrderCourseGrade(homework, student);
        return ResponseEntity.ok(
            result
        );
    }
    @ApiOperation(value = "通过学生名查询指定的一条学生提交的作业")
    @GetMapping("/student-homeworks/student")
    public ResponseEntity<Page<StudentHomework>> findCourseHomework(@RequestParam String student, Pageable pageable) {
        Page<StudentHomework> page = null;
        try {
            page = studentHomeworkService.findHomework(student, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestAlertException(e.getMessage(),ENTITY_NAME,"StudentHomework find error");
        }
        return ResponseEntity.ok(page);
    }


    @GetMapping("/student-homeworks/id")
    @ApiOperation(value="查询指定作业的学生提交记录")
    public ResponseEntity<Page<StudentHomework>> getStudentHomeworkByomeworkId(@RequestParam Long id,Pageable pageable) {
        log.debug("REST request Hto get StudentHomework : {}", id);
        try{
            Page<StudentHomework> studentHomeworks=studentHomeworkService.getStudentHomeworkByCourseHomework(id,pageable);
            return ResponseEntity.ok(studentHomeworks);
        }catch (Exception e){
            e.printStackTrace();
            throw  new BadRequestAlertException(e.getMessage(),ENTITY_NAME,"not found");
        }
    }

    @PutMapping("/student-homeworks/id/grade")
    @ApiOperation(value="给学生作业评分")
    public ResponseEntity<StudentHomework> updateStudentHomeworkGrade(@RequestParam Long id,@RequestParam Integer grade) {
        log.debug("REST request to update StudentHomeworkGrade : {},{}",id,grade);
        Optional<StudentHomework> studentHomework = null;
        try {
            studentHomework = studentHomeworkService.updateStudentHomeworkGrade(id,grade);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "CourseGroup update error");
        }
        return ResponseUtil.wrapOrNotFound(studentHomework);
    }
}
