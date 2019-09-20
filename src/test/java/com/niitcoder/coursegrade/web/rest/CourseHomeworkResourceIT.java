package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.CoursegradeApp;
import com.niitcoder.coursegrade.domain.CourseHomework;
import com.niitcoder.coursegrade.repository.CourseHomeworkRepository;
import com.niitcoder.coursegrade.service.CourseHomeworkService;
import com.niitcoder.coursegrade.service.CoursePlanService;
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
 * Integration tests for the {@link CourseHomeworkResource} REST controller.
 */
@SpringBootTest(classes = CoursegradeApp.class)
public class CourseHomeworkResourceIT {

    private static final String DEFAULT_HOMEWORK_CODE = "AAAAAAAAAA";
    private static final String UPDATED_HOMEWORK_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_HOMEWORK_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_HOMEWORK_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_HOMEWORK_TARGET = "AAAAAAAAAA";
    private static final String UPDATED_HOMEWORK_TARGET = "BBBBBBBBBB";

    private static final Integer DEFAULT_HOMEWORK_GRADE = 1;
    private static final Integer UPDATED_HOMEWORK_GRADE = 2;
    private static final Integer SMALLER_HOMEWORK_GRADE = 1 - 1;

    private static final ZonedDateTime DEFAULT_HOMEWORK_DEADLINE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_HOMEWORK_DEADLINE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_HOMEWORK_DEADLINE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_DATA_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private CourseHomeworkRepository courseHomeworkRepository;

    @Autowired
    private CourseHomeworkService courseHomeworkService;

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

    private MockMvc restCourseHomeworkMockMvc;

    private CourseHomework courseHomework;
    private CoursePlanService coursePlanService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourseHomeworkResource courseHomeworkResource = new CourseHomeworkResource(courseHomeworkService, coursePlanService);
        this.restCourseHomeworkMockMvc = MockMvcBuilders.standaloneSetup(courseHomeworkResource)
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
    public static CourseHomework createEntity(EntityManager em) {
        CourseHomework courseHomework = new CourseHomework()
            .homeworkCode(DEFAULT_HOMEWORK_CODE)
            .homeworkMemo(DEFAULT_HOMEWORK_MEMO)
            .homeworkTarget(DEFAULT_HOMEWORK_TARGET)
            .homeworkGrade(DEFAULT_HOMEWORK_GRADE)
            .homeworkDeadline(DEFAULT_HOMEWORK_DEADLINE)
            .dataTime(DEFAULT_DATA_TIME);
        return courseHomework;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseHomework createUpdatedEntity(EntityManager em) {
        CourseHomework courseHomework = new CourseHomework()
            .homeworkCode(UPDATED_HOMEWORK_CODE)
            .homeworkMemo(UPDATED_HOMEWORK_MEMO)
            .homeworkTarget(UPDATED_HOMEWORK_TARGET)
            .homeworkGrade(UPDATED_HOMEWORK_GRADE)
            .homeworkDeadline(UPDATED_HOMEWORK_DEADLINE)
            .dataTime(UPDATED_DATA_TIME);
        return courseHomework;
    }

    @BeforeEach
    public void initTest() {
        courseHomework = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourseHomework() throws Exception {
        int databaseSizeBeforeCreate = courseHomeworkRepository.findAll().size();

        // Create the CourseHomework
        restCourseHomeworkMockMvc.perform(post("/api/course-homeworks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseHomework)))
            .andExpect(status().isCreated());

        // Validate the CourseHomework in the database
        List<CourseHomework> courseHomeworkList = courseHomeworkRepository.findAll();
        assertThat(courseHomeworkList).hasSize(databaseSizeBeforeCreate + 1);
        CourseHomework testCourseHomework = courseHomeworkList.get(courseHomeworkList.size() - 1);
        assertThat(testCourseHomework.getHomeworkCode()).isEqualTo(DEFAULT_HOMEWORK_CODE);
        assertThat(testCourseHomework.getHomeworkMemo()).isEqualTo(DEFAULT_HOMEWORK_MEMO);
        assertThat(testCourseHomework.getHomeworkTarget()).isEqualTo(DEFAULT_HOMEWORK_TARGET);
        assertThat(testCourseHomework.getHomeworkGrade()).isEqualTo(DEFAULT_HOMEWORK_GRADE);
        assertThat(testCourseHomework.getHomeworkDeadline()).isEqualTo(DEFAULT_HOMEWORK_DEADLINE);
        assertThat(testCourseHomework.getDataTime()).isEqualTo(DEFAULT_DATA_TIME);
    }

    @Test
    @Transactional
    public void createCourseHomeworkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseHomeworkRepository.findAll().size();

        // Create the CourseHomework with an existing ID
        courseHomework.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseHomeworkMockMvc.perform(post("/api/course-homeworks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseHomework)))
            .andExpect(status().isBadRequest());

        // Validate the CourseHomework in the database
        List<CourseHomework> courseHomeworkList = courseHomeworkRepository.findAll();
        assertThat(courseHomeworkList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCourseHomeworks() throws Exception {
        // Initialize the database
        courseHomeworkRepository.saveAndFlush(courseHomework);

        // Get all the courseHomeworkList
        restCourseHomeworkMockMvc.perform(get("/api/course-homeworks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseHomework.getId().intValue())))
            .andExpect(jsonPath("$.[*].homeworkCode").value(hasItem(DEFAULT_HOMEWORK_CODE.toString())))
            .andExpect(jsonPath("$.[*].homeworkMemo").value(hasItem(DEFAULT_HOMEWORK_MEMO.toString())))
            .andExpect(jsonPath("$.[*].homeworkTarget").value(hasItem(DEFAULT_HOMEWORK_TARGET.toString())))
            .andExpect(jsonPath("$.[*].homeworkGrade").value(hasItem(DEFAULT_HOMEWORK_GRADE)))
            .andExpect(jsonPath("$.[*].homeworkDeadline").value(hasItem(sameInstant(DEFAULT_HOMEWORK_DEADLINE))))
            .andExpect(jsonPath("$.[*].dataTime").value(hasItem(sameInstant(DEFAULT_DATA_TIME))));
    }
    
    @Test
    @Transactional
    public void getCourseHomework() throws Exception {
        // Initialize the database
        courseHomeworkRepository.saveAndFlush(courseHomework);

        // Get the courseHomework
        restCourseHomeworkMockMvc.perform(get("/api/course-homeworks/{id}", courseHomework.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(courseHomework.getId().intValue()))
            .andExpect(jsonPath("$.homeworkCode").value(DEFAULT_HOMEWORK_CODE.toString()))
            .andExpect(jsonPath("$.homeworkMemo").value(DEFAULT_HOMEWORK_MEMO.toString()))
            .andExpect(jsonPath("$.homeworkTarget").value(DEFAULT_HOMEWORK_TARGET.toString()))
            .andExpect(jsonPath("$.homeworkGrade").value(DEFAULT_HOMEWORK_GRADE))
            .andExpect(jsonPath("$.homeworkDeadline").value(sameInstant(DEFAULT_HOMEWORK_DEADLINE)))
            .andExpect(jsonPath("$.dataTime").value(sameInstant(DEFAULT_DATA_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingCourseHomework() throws Exception {
        // Get the courseHomework
        restCourseHomeworkMockMvc.perform(get("/api/course-homeworks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourseHomework() throws Exception {
        // Initialize the database
        courseHomeworkService.save(courseHomework);

        int databaseSizeBeforeUpdate = courseHomeworkRepository.findAll().size();

        // Update the courseHomework
        CourseHomework updatedCourseHomework = courseHomeworkRepository.findById(courseHomework.getId()).get();
        // Disconnect from session so that the updates on updatedCourseHomework are not directly saved in db
        em.detach(updatedCourseHomework);
        updatedCourseHomework
            .homeworkCode(UPDATED_HOMEWORK_CODE)
            .homeworkMemo(UPDATED_HOMEWORK_MEMO)
            .homeworkTarget(UPDATED_HOMEWORK_TARGET)
            .homeworkGrade(UPDATED_HOMEWORK_GRADE)
            .homeworkDeadline(UPDATED_HOMEWORK_DEADLINE)
            .dataTime(UPDATED_DATA_TIME);

        restCourseHomeworkMockMvc.perform(put("/api/course-homeworks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourseHomework)))
            .andExpect(status().isOk());

        // Validate the CourseHomework in the database
        List<CourseHomework> courseHomeworkList = courseHomeworkRepository.findAll();
        assertThat(courseHomeworkList).hasSize(databaseSizeBeforeUpdate);
        CourseHomework testCourseHomework = courseHomeworkList.get(courseHomeworkList.size() - 1);
        assertThat(testCourseHomework.getHomeworkCode()).isEqualTo(UPDATED_HOMEWORK_CODE);
        assertThat(testCourseHomework.getHomeworkMemo()).isEqualTo(UPDATED_HOMEWORK_MEMO);
        assertThat(testCourseHomework.getHomeworkTarget()).isEqualTo(UPDATED_HOMEWORK_TARGET);
        assertThat(testCourseHomework.getHomeworkGrade()).isEqualTo(UPDATED_HOMEWORK_GRADE);
        assertThat(testCourseHomework.getHomeworkDeadline()).isEqualTo(UPDATED_HOMEWORK_DEADLINE);
        assertThat(testCourseHomework.getDataTime()).isEqualTo(UPDATED_DATA_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingCourseHomework() throws Exception {
        int databaseSizeBeforeUpdate = courseHomeworkRepository.findAll().size();

        // Create the CourseHomework

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseHomeworkMockMvc.perform(put("/api/course-homeworks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseHomework)))
            .andExpect(status().isBadRequest());

        // Validate the CourseHomework in the database
        List<CourseHomework> courseHomeworkList = courseHomeworkRepository.findAll();
        assertThat(courseHomeworkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourseHomework() throws Exception {
        // Initialize the database
        courseHomeworkService.save(courseHomework);

        int databaseSizeBeforeDelete = courseHomeworkRepository.findAll().size();

        // Delete the courseHomework
        restCourseHomeworkMockMvc.perform(delete("/api/course-homeworks/{id}", courseHomework.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseHomework> courseHomeworkList = courseHomeworkRepository.findAll();
        assertThat(courseHomeworkList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseHomework.class);
        CourseHomework courseHomework1 = new CourseHomework();
        courseHomework1.setId(1L);
        CourseHomework courseHomework2 = new CourseHomework();
        courseHomework2.setId(courseHomework1.getId());
        assertThat(courseHomework1).isEqualTo(courseHomework2);
        courseHomework2.setId(2L);
        assertThat(courseHomework1).isNotEqualTo(courseHomework2);
        courseHomework1.setId(null);
        assertThat(courseHomework1).isNotEqualTo(courseHomework2);
    }
}
