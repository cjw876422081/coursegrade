package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.domain.CourseHomework;
import com.niitcoder.coursegrade.service.CourseHomeworkService;
import com.niitcoder.coursegrade.service.CoursePlanService;
import com.niitcoder.coursegrade.service.dto.CourseInfoPlan;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.niitcoder.coursegrade.domain.CourseHomework}.
 */
@RestController
@RequestMapping("/api")
public class CourseHomeworkResource {

    private final Logger log = LoggerFactory.getLogger(CourseHomeworkResource.class);

    private static final String ENTITY_NAME = "courseHomework";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseHomeworkService courseHomeworkService;

    private final CoursePlanService coursePlanService;

    public CourseHomeworkResource(CourseHomeworkService courseHomeworkService, CoursePlanService coursePlanService) {
        this.courseHomeworkService = courseHomeworkService;
        this.coursePlanService = coursePlanService;
    }

    /**
     * {@code POST  /course-homeworks} : Create a new courseHomework.
     *
     * @param courseHomework the courseHomework to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseHomework, or with status {@code 400 (Bad Request)} if the courseHomework has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-homeworks")
    public ResponseEntity<CourseHomework> createCourseHomework(@RequestBody CourseHomework courseHomework) throws URISyntaxException {
        log.debug("REST request to save CourseHomework : {}", courseHomework);
        if (courseHomework.getId() != null) {
            throw new BadRequestAlertException("A new courseHomework cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseHomework result = courseHomeworkService.save(courseHomework);
        return ResponseEntity.created(new URI("/api/course-homeworks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course-homeworks} : Updates an existing courseHomework.
     *
     * @param courseHomework the courseHomework to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseHomework,
     * or with status {@code 400 (Bad Request)} if the courseHomework is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseHomework couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/course-homeworks")
    public ResponseEntity<CourseHomework> updateCourseHomework(@RequestBody CourseHomework courseHomework) throws URISyntaxException {
        log.debug("REST request to update CourseHomework : {}", courseHomework);
        if (courseHomework.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourseHomework result = courseHomeworkService.save(courseHomework);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseHomework.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /course-homeworks} : get all the courseHomeworks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseHomeworks in body.
     */
    @GetMapping("/course-homeworks")
    public ResponseEntity<List<CourseHomework>> getAllCourseHomeworks(Pageable pageable) {
        log.debug("REST request to get a page of CourseHomeworks");
        Page<CourseHomework> page = courseHomeworkService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /course-homeworks/:id} : get the "id" courseHomework.
     *
     * @param id the id of the courseHomework to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseHomework, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-homeworks/{id}")
    public ResponseEntity<CourseHomework> getCourseHomework(@PathVariable Long id) {
        log.debug("REST request to get CourseHomework : {}", id);
        Optional<CourseHomework> courseHomework = courseHomeworkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseHomework);
    }

    /**
     * {@code DELETE  /course-homeworks/:id} : delete the "id" courseHomework.
     *
     * @param id the id of the courseHomework to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-homeworks/{id}")
    public ResponseEntity<Void> deleteCourseHomework(@PathVariable Long id) {
        log.debug("REST request to delete CourseHomework : {}", id);
        courseHomeworkService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code get  /homeworks-plan/:id} : delete the "id" courseHomework.
     *
     * @param id the id of the courseHomework to get.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @ApiOperation(value = "根据指定授课内容获取对应作业的内容")
    @GetMapping("/homework-plan/{id}")
    public ResponseEntity<List<CourseHomework>> getCourseHomeworkByCouesePlan(@PathVariable Long id) {
        log.debug("REST request to get CourseHomework : {}", id);
        List<CourseHomework> courseHomework = courseHomeworkService.findByPlanId(id);
        return ResponseEntity.ok(courseHomework);
    }

    @ApiOperation(value = "根据指定课程获取对应作业的内容")
    @GetMapping("/homework-grade/{id}")
    public ResponseEntity<CourseInfoPlan> getCourseHomeworkByCoueseId(@PathVariable Long id) {
        log.debug("REST request to get CourseHomework : {}", id);
        CourseInfoPlan courseInfoPlan = coursePlanService.getCourseInfoPlan(id);
        return ResponseEntity.ok(courseInfoPlan);
    }
    @ApiOperation(value = "修改作业内容")
    @PutMapping("/course-homeworks/updateCourseHomework")
    public ResponseEntity<Void> updateCourseHomework(@RequestParam Long id,@RequestParam String homework_memo) throws URISyntaxException {
        log.debug("REST request to update CourseHomework : {},{}", id,homework_memo);
        if (id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        courseHomeworkService.updateTask(id,homework_memo);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
