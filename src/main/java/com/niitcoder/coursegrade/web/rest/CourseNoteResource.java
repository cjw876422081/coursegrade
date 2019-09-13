package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.domain.CourseNote;
import com.niitcoder.coursegrade.service.CourseNoteService;
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
 * REST controller for managing {@link com.niitcoder.coursegrade.domain.CourseNote}.
 */
@RestController
@RequestMapping("/api")
public class CourseNoteResource {

    private final Logger log = LoggerFactory.getLogger(CourseNoteResource.class);

    private static final String ENTITY_NAME = "courseNote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseNoteService courseNoteService;

    public CourseNoteResource(CourseNoteService courseNoteService) {
        this.courseNoteService = courseNoteService;
    }

    /**
     * {@code POST  /course-notes} : Create a new courseNote.
     *
     * @param courseNote the courseNote to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseNote, or with status {@code 400 (Bad Request)} if the courseNote has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-notes")
    public ResponseEntity<CourseNote> createCourseNote(@RequestBody CourseNote courseNote) throws URISyntaxException {
        log.debug("REST request to save CourseNote : {}", courseNote);
        if (courseNote.getId() != null) {
            throw new BadRequestAlertException("A new courseNote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseNote result = courseNoteService.save(courseNote);
        return ResponseEntity.created(new URI("/api/course-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course-notes} : Updates an existing courseNote.
     *
     * @param courseNote the courseNote to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseNote,
     * or with status {@code 400 (Bad Request)} if the courseNote is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseNote couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/course-notes")
    public ResponseEntity<CourseNote> updateCourseNote(@RequestBody CourseNote courseNote) throws URISyntaxException {
        log.debug("REST request to update CourseNote : {}", courseNote);
        if (courseNote.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourseNote result = courseNoteService.save(courseNote);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseNote.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /course-notes} : get all the courseNotes.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseNotes in body.
     */
    @GetMapping("/course-notes")
    public ResponseEntity<List<CourseNote>> getAllCourseNotes(Pageable pageable) {
        log.debug("REST request to get a page of CourseNotes");
        Page<CourseNote> page = courseNoteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /course-notes/:id} : get the "id" courseNote.
     *
     * @param id the id of the courseNote to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseNote, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/course-notes/{id}")
    public ResponseEntity<CourseNote> getCourseNote(@PathVariable Long id) {
        log.debug("REST request to get CourseNote : {}", id);
        Optional<CourseNote> courseNote = courseNoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseNote);
    }

    /**
     * {@code DELETE  /course-notes/:id} : delete the "id" courseNote.
     *
     * @param id the id of the courseNote to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-notes/{id}")
    public ResponseEntity<Void> deleteCourseNote(@PathVariable Long id) {
        log.debug("REST request to delete CourseNote : {}", id);
        courseNoteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
