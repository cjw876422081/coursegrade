package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.CoursegradeApp;
import com.niitcoder.coursegrade.domain.CourseNote;
import com.niitcoder.coursegrade.repository.CourseNoteRepository;
import com.niitcoder.coursegrade.service.CourseNoteService;
import com.niitcoder.coursegrade.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.niitcoder.coursegrade.web.rest.TestUtil.sameInstant;
import static com.niitcoder.coursegrade.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CourseNoteResource} REST controller.
 */
@SpringBootTest(classes = CoursegradeApp.class)
public class CourseNoteResourceIT {

    private static final String DEFAULT_NOTE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_NOTE_MEMO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_NOTE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_NOTE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_NOTE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_PUBLISH_USER = "AAAAAAAAAA";
    private static final String UPDATED_PUBLISH_USER = "BBBBBBBBBB";

    @Autowired
    private CourseNoteRepository courseNoteRepository;

    @Autowired
    private CourseNoteService courseNoteService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCourseNoteMockMvc;

    private CourseNote courseNote;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourseNoteResource courseNoteResource = new CourseNoteResource(courseNoteService);
        this.restCourseNoteMockMvc = MockMvcBuilders.standaloneSetup(courseNoteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseNote createEntity(EntityManager em) {
        CourseNote courseNote = new CourseNote()
            .noteType(DEFAULT_NOTE_TYPE)
            .noteMemo(DEFAULT_NOTE_MEMO)
            .noteTime(DEFAULT_NOTE_TIME)
            .publishUser(DEFAULT_PUBLISH_USER);
        return courseNote;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseNote createUpdatedEntity(EntityManager em) {
        CourseNote courseNote = new CourseNote()
            .noteType(UPDATED_NOTE_TYPE)
            .noteMemo(UPDATED_NOTE_MEMO)
            .noteTime(UPDATED_NOTE_TIME)
            .publishUser(UPDATED_PUBLISH_USER);
        return courseNote;
    }

    @BeforeEach
    public void initTest() {
        courseNote = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourseNote() throws Exception {
        int databaseSizeBeforeCreate = courseNoteRepository.findAll().size();

        // Create the CourseNote
        restCourseNoteMockMvc.perform(post("/api/course-notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseNote)))
            .andExpect(status().isCreated());

        // Validate the CourseNote in the database
        List<CourseNote> courseNoteList = courseNoteRepository.findAll();
        assertThat(courseNoteList).hasSize(databaseSizeBeforeCreate + 1);
        CourseNote testCourseNote = courseNoteList.get(courseNoteList.size() - 1);
        assertThat(testCourseNote.getNoteType()).isEqualTo(DEFAULT_NOTE_TYPE);
        assertThat(testCourseNote.getNoteMemo()).isEqualTo(DEFAULT_NOTE_MEMO);
        assertThat(testCourseNote.getNoteTime()).isEqualTo(DEFAULT_NOTE_TIME);
        assertThat(testCourseNote.getPublishUser()).isEqualTo(DEFAULT_PUBLISH_USER);
    }

    @Test
    @Transactional
    public void createCourseNoteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseNoteRepository.findAll().size();

        // Create the CourseNote with an existing ID
        courseNote.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseNoteMockMvc.perform(post("/api/course-notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseNote)))
            .andExpect(status().isBadRequest());

        // Validate the CourseNote in the database
        List<CourseNote> courseNoteList = courseNoteRepository.findAll();
        assertThat(courseNoteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCourseNotes() throws Exception {
        // Initialize the database
        courseNoteRepository.saveAndFlush(courseNote);

        // Get all the courseNoteList
        restCourseNoteMockMvc.perform(get("/api/course-notes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseNote.getId().intValue())))
            .andExpect(jsonPath("$.[*].noteType").value(hasItem(DEFAULT_NOTE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].noteMemo").value(hasItem(DEFAULT_NOTE_MEMO.toString())))
            .andExpect(jsonPath("$.[*].noteTime").value(hasItem(sameInstant(DEFAULT_NOTE_TIME))))
            .andExpect(jsonPath("$.[*].publishUser").value(hasItem(DEFAULT_PUBLISH_USER.toString())));
    }
    
    @Test
    @Transactional
    public void getCourseNote() throws Exception {
        // Initialize the database
        courseNoteRepository.saveAndFlush(courseNote);

        // Get the courseNote
        restCourseNoteMockMvc.perform(get("/api/course-notes/{id}", courseNote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(courseNote.getId().intValue()))
            .andExpect(jsonPath("$.noteType").value(DEFAULT_NOTE_TYPE.toString()))
            .andExpect(jsonPath("$.noteMemo").value(DEFAULT_NOTE_MEMO.toString()))
            .andExpect(jsonPath("$.noteTime").value(sameInstant(DEFAULT_NOTE_TIME)))
            .andExpect(jsonPath("$.publishUser").value(DEFAULT_PUBLISH_USER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCourseNote() throws Exception {
        // Get the courseNote
        restCourseNoteMockMvc.perform(get("/api/course-notes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourseNote() throws Exception {
        // Initialize the database
        courseNoteService.save(courseNote);

        int databaseSizeBeforeUpdate = courseNoteRepository.findAll().size();

        // Update the courseNote
        CourseNote updatedCourseNote = courseNoteRepository.findById(courseNote.getId()).get();
        // Disconnect from session so that the updates on updatedCourseNote are not directly saved in db
        em.detach(updatedCourseNote);
        updatedCourseNote
            .noteType(UPDATED_NOTE_TYPE)
            .noteMemo(UPDATED_NOTE_MEMO)
            .noteTime(UPDATED_NOTE_TIME)
            .publishUser(UPDATED_PUBLISH_USER);

        restCourseNoteMockMvc.perform(put("/api/course-notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourseNote)))
            .andExpect(status().isOk());

        // Validate the CourseNote in the database
        List<CourseNote> courseNoteList = courseNoteRepository.findAll();
        assertThat(courseNoteList).hasSize(databaseSizeBeforeUpdate);
        CourseNote testCourseNote = courseNoteList.get(courseNoteList.size() - 1);
        assertThat(testCourseNote.getNoteType()).isEqualTo(UPDATED_NOTE_TYPE);
        assertThat(testCourseNote.getNoteMemo()).isEqualTo(UPDATED_NOTE_MEMO);
        assertThat(testCourseNote.getNoteTime()).isEqualTo(UPDATED_NOTE_TIME);
        assertThat(testCourseNote.getPublishUser()).isEqualTo(UPDATED_PUBLISH_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingCourseNote() throws Exception {
        int databaseSizeBeforeUpdate = courseNoteRepository.findAll().size();

        // Create the CourseNote

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseNoteMockMvc.perform(put("/api/course-notes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseNote)))
            .andExpect(status().isBadRequest());

        // Validate the CourseNote in the database
        List<CourseNote> courseNoteList = courseNoteRepository.findAll();
        assertThat(courseNoteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourseNote() throws Exception {
        // Initialize the database
        courseNoteService.save(courseNote);

        int databaseSizeBeforeDelete = courseNoteRepository.findAll().size();

        // Delete the courseNote
        restCourseNoteMockMvc.perform(delete("/api/course-notes/{id}", courseNote.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseNote> courseNoteList = courseNoteRepository.findAll();
        assertThat(courseNoteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseNote.class);
        CourseNote courseNote1 = new CourseNote();
        courseNote1.setId(1L);
        CourseNote courseNote2 = new CourseNote();
        courseNote2.setId(courseNote1.getId());
        assertThat(courseNote1).isEqualTo(courseNote2);
        courseNote2.setId(2L);
        assertThat(courseNote1).isNotEqualTo(courseNote2);
        courseNote1.setId(null);
        assertThat(courseNote1).isNotEqualTo(courseNote2);
    }
}
