package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.service.dto.Student;
import com.niitcoder.coursegrade.domain.StudentCourseGroup;
import com.niitcoder.coursegrade.service.StudentCourseGroupService;
import com.niitcoder.coursegrade.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
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

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.niitcoder.coursegrade.domain.StudentCourseGroup}.
 */
@RestController
@RequestMapping("/api")
public class StudentCourseGroupResource {

    private final Logger log = LoggerFactory.getLogger(StudentCourseGroupResource.class);

    private static final String ENTITY_NAME = "studentCourseGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentCourseGroupService studentCourseGroupService;

    public StudentCourseGroupResource(StudentCourseGroupService studentCourseGroupService) {
        this.studentCourseGroupService = studentCourseGroupService;
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
    public ResponseEntity<List<StudentCourseGroup>> getAllStudentCourseGroups(Pageable pageable) {
        log.debug("REST request to get a page of StudentCourseGroups");
        Page<StudentCourseGroup> page = studentCourseGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
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

    /**
     * {@code GET  /student-course-groups/:group} : get the "group" studentCourseGroup.
     *
     * @param group the student of the studentCourseGroup to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studentCourseGroup, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/student-course-groups/group")
    public ResponseEntity<List<Student>> getStudentByCourseGroup(@RequestParam String group,Pageable pageable) {
        log.debug("REST request to get StudentCourseGroup : {}", group);
        Page<Student> page = studentCourseGroupService.findStudentByGroup(group,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code DELETE  /student-course-groups/:id} : delete the "id" studentCourseGroup.
     *
     * @param id the id of the studentCourseGroup to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/student-course-groups/{id}")
    public ResponseEntity<Void> deleteStudentCourseGroup(@PathVariable Long id) {
        log.debug("REST request to delete StudentCourseGroup : {}", id);
        studentCourseGroupService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
    @GetMapping("/student-course-order-group/{student}/{course_id}")
    public  ResponseEntity getCourseGroup(@PathVariable String student ,@PathVariable Long course_id ){
        log.debug("REST request to get  getCourseGroup : {}", student , course_id);
        return ResponseEntity.ok(
            studentCourseGroupService.getCourseGroup(student , course_id)
        );
    }
    @GetMapping("/student-course-group/student")
    public ResponseEntity getMyCourse(@RequestParam String student){
        return ResponseEntity.ok(studentCourseGroupService.getMyCourse(student));
    }
}
