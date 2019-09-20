package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.domain.CoursePlan;
import com.niitcoder.coursegrade.service.CoursePlanService;
import com.niitcoder.coursegrade.service.dto.CourseInfoPlan;
import com.niitcoder.coursegrade.service.dto.CoursePlanDTO;
import com.niitcoder.coursegrade.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.niitcoder.coursegrade.domain.CoursePlan}.
 */
@RestController
@RequestMapping("/api")
@Api("授课内容接口")
public class CoursePlanResource {

    private final Logger log = LoggerFactory.getLogger(CoursePlanResource.class);

    private static final String ENTITY_NAME = "coursePlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoursePlanService coursePlanService;

    public CoursePlanResource(CoursePlanService coursePlanService) {
        this.coursePlanService = coursePlanService;
    }

    /**
     * {@code POST  /course-plans} : Create a new coursePlan.
     *
     * @param coursePlan the coursePlan to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coursePlan, or with status {@code 400 (Bad Request)} if the coursePlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-plans")
    public ResponseEntity<CoursePlan> createCoursePlan(@RequestBody CoursePlan coursePlan) throws URISyntaxException {
        log.debug("REST request to save CoursePlan : {}", coursePlan);
        if (coursePlan.getId() != null) {
            throw new BadRequestAlertException("A new coursePlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CoursePlan result = coursePlanService.save(coursePlan);
        return ResponseEntity.created(new URI("/api/course-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @ApiOperation(value = "修改课程信息")
    @PutMapping("/course-plans")
    public ResponseEntity<CoursePlan> updateCoursePlan(@RequestBody CoursePlan coursePlan) throws URISyntaxException {
        log.debug("REST request to update CoursePlan : {}", coursePlan);
        if (coursePlan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CoursePlan coursePlan1 = new CoursePlan();
        coursePlan1 = coursePlanService.findOne(coursePlan.getId()).get();
        coursePlan.setDataTime(coursePlan1.getDataTime());
        CoursePlan result = coursePlanService.save(coursePlan);
        return ResponseEntity.ok(coursePlan);
    }

    /**
     * {@code GET  /course-plans} : get all the coursePlans.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coursePlans in body.
     */
    @GetMapping("/course-plans")
    public ResponseEntity<List<CoursePlan>> getAllCoursePlans(Pageable pageable) {
        log.debug("REST request to get a page of CoursePlans");
        Page<CoursePlan> page = coursePlanService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /course-plans/:id} : get the "id" coursePlan.
     *
     * @param id the id of the coursePlan to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coursePlan, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-plans/{id}")
    public ResponseEntity<CoursePlan> getCoursePlan(@PathVariable Long id) {
        log.debug("REST request to get CoursePlan : {}", id);
        Optional<CoursePlan> coursePlan = coursePlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coursePlan);
    }

    /**
     * {@code DELETE  /course-plans/:id} : delete the "id" coursePlan.
     *
     * @param id the id of the coursePlan to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-plans/{id}")
    public ResponseEntity<Void> deleteCoursePlan(@PathVariable Long id) {
        log.debug("REST request to delete CoursePlan : {}", id);
        coursePlanService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @ApiOperation(value = "根据父级内容查询下级授课内容")
    @ApiImplicitParam(name = "id", value = "授课内容父级id")
    @GetMapping("/course-plans/child/{id}")
    public ResponseEntity<List<CoursePlan>> getCoursePlanByParentsId(@PathVariable Long id) {
        log.debug("REST request to get getCoursePlanByParentsId : {}", id);
        List<CoursePlan> coursePlan = coursePlanService.getPlanByParentId(id);
        return ResponseEntity.ok(coursePlan);
    }

    @ApiOperation(value = "查询单个课程的全部授课内容，没有课程信息")
    @ApiImplicitParam(name = "courseId", value = "课程id")
    @GetMapping("/course-plans/tree2/{courseId}")
    public ResponseEntity<List<CoursePlanDTO>> getCoursePlanTree2(@PathVariable Long courseId) {
        log.debug("REST request to get getCoursePlanByParentsId : {}", courseId);
        List<CoursePlanDTO> coursePlan = coursePlanService.getCoursePlanDTOByCourseId(courseId);
        return ResponseEntity.ok(coursePlan);
    }

    @ApiOperation(value = "查询单个课程的全部授课内容，有课程信息")
    @ApiImplicitParam(name = "courseId", value = "课程id")
    @GetMapping("/course-plans/tree/{courseId}")
    public ResponseEntity<CourseInfoPlan> getCoursePlanTree(@PathVariable Long courseId) {
        log.debug("REST request to get getCoursePlanByParentsId : {}", courseId);
        CourseInfoPlan coursePlan = coursePlanService.getCourseInfoPlan(courseId);
        return ResponseEntity.ok(coursePlan);
    }
}
