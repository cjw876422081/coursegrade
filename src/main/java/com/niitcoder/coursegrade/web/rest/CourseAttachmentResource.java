package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.domain.CourseAttachment;
import com.niitcoder.coursegrade.service.CourseAttachmentService;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.niitcoder.coursegrade.domain.CourseAttachment}.
 */
@RestController
@RequestMapping("/api")
public class CourseAttachmentResource {

    private final Logger log = LoggerFactory.getLogger(CourseAttachmentResource.class);

    private static final String ENTITY_NAME = "courseAttachment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseAttachmentService courseAttachmentService;

    public CourseAttachmentResource(CourseAttachmentService courseAttachmentService) {
        this.courseAttachmentService = courseAttachmentService;
    }

    /**
     * {@code POST  /course-attachments} : Create a new courseAttachment.
     *
     * @param courseAttachment the courseAttachment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseAttachment, or with status {@code 400 (Bad Request)} if the courseAttachment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-attachments")
    public ResponseEntity<CourseAttachment> createCourseAttachment(@RequestBody CourseAttachment courseAttachment) throws URISyntaxException {
        log.debug("REST request to save CourseAttachment : {}", courseAttachment);
        if (courseAttachment.getId() != null) {
            throw new BadRequestAlertException("A new courseAttachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseAttachment result = courseAttachmentService.save(courseAttachment);
        return ResponseEntity.created(new URI("/api/course-attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course-attachments} : Updates an existing courseAttachment.
     *
     * @param courseAttachment the courseAttachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseAttachment,
     * or with status {@code 400 (Bad Request)} if the courseAttachment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseAttachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/course-attachments")
    public ResponseEntity<CourseAttachment> updateCourseAttachment(@RequestBody CourseAttachment courseAttachment) throws URISyntaxException {
        log.debug("REST request to update CourseAttachment : {}", courseAttachment);
        if (courseAttachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourseAttachment result = courseAttachmentService.save(courseAttachment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseAttachment.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /course-attachments} : get all the courseAttachments.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseAttachments in body.
     */
    @GetMapping("/course-attachments")
    public ResponseEntity<List<CourseAttachment>> getAllCourseAttachments(Pageable pageable) {
        log.debug("REST request to get a page of CourseAttachments");
        Page<CourseAttachment> page = courseAttachmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /course-attachments/:id} : get the "id" courseAttachment.
     *
     * @param id the id of the courseAttachment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseAttachment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-attachments/{id}")
    public ResponseEntity<CourseAttachment> getCourseAttachment(@PathVariable Long id) {
        log.debug("REST request to get CourseAttachment : {}", id);
        Optional<CourseAttachment> courseAttachment = courseAttachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseAttachment);
    }

    /**
     * {@code DELETE  /course-attachments/:id} : delete the "id" courseAttachment.
     *
     * @param id the id of the courseAttachment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-attachments/{id}")
    public ResponseEntity<Void> deleteCourseAttachment(@PathVariable Long id) {
        log.debug("REST request to delete CourseAttachment : {}", id);
        courseAttachmentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
