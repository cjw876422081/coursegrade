package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.CoursegradeApp;
import com.niitcoder.coursegrade.domain.CourseInfo;
import com.niitcoder.coursegrade.repository.CourseInfoRepository;
import com.niitcoder.coursegrade.service.CourseInfoService;
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
 * Integration tests for the {@link CourseInfoResource} REST controller.
 */
@SpringBootTest(classes = CoursegradeApp.class)
public class CourseInfoResourceIT {

    private static final String DEFAULT_COURSE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_COURSE_COUNT = 1;
    private static final Integer UPDATED_COURSE_COUNT = 2;
    private static final Integer SMALLER_COURSE_COUNT = 1 - 1;

    private static final Integer DEFAULT_COURSE_WEEK_COUNT = 1;
    private static final Integer UPDATED_COURSE_WEEK_COUNT = 2;
    private static final Integer SMALLER_COURSE_WEEK_COUNT = 1 - 1;

    private static final String DEFAULT_COURSE_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_MEMO = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATA_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_COURSE_USER = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_USER = "BBBBBBBBBB";

    @Autowired
    private CourseInfoRepository courseInfoRepository;

    @Autowired
    private CourseInfoService courseInfoService;

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

    private MockMvc restCourseInfoMockMvc;

    private CourseInfo courseInfo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourseInfoResource courseInfoResource = new CourseInfoResource(courseInfoService);
        this.restCourseInfoMockMvc = MockMvcBuilders.standaloneSetup(courseInfoResource)
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
    public static CourseInfo createEntity(EntityManager em) {
        CourseInfo courseInfo = new CourseInfo()
            .courseCode(DEFAULT_COURSE_CODE)
            .courseName(DEFAULT_COURSE_NAME)
            .courseCount(DEFAULT_COURSE_COUNT)
            .courseWeekCount(DEFAULT_COURSE_WEEK_COUNT)
            .courseMemo(DEFAULT_COURSE_MEMO)
            .dataTime(DEFAULT_DATA_TIME)
            .courseUser(DEFAULT_COURSE_USER);
        return courseInfo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseInfo createUpdatedEntity(EntityManager em) {
        CourseInfo courseInfo = new CourseInfo()
            .courseCode(UPDATED_COURSE_CODE)
            .courseName(UPDATED_COURSE_NAME)
            .courseCount(UPDATED_COURSE_COUNT)
            .courseWeekCount(UPDATED_COURSE_WEEK_COUNT)
            .courseMemo(UPDATED_COURSE_MEMO)
            .dataTime(UPDATED_DATA_TIME)
            .courseUser(UPDATED_COURSE_USER);
        return courseInfo;
    }

    @BeforeEach
    public void initTest() {
        courseInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourseInfo() throws Exception {
        int databaseSizeBeforeCreate = courseInfoRepository.findAll().size();

        // Create the CourseInfo
        restCourseInfoMockMvc.perform(post("/api/course-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseInfo)))
            .andExpect(status().isCreated());

        // Validate the CourseInfo in the database
        List<CourseInfo> courseInfoList = courseInfoRepository.findAll();
        assertThat(courseInfoList).hasSize(databaseSizeBeforeCreate + 1);
        CourseInfo testCourseInfo = courseInfoList.get(courseInfoList.size() - 1);
        assertThat(testCourseInfo.getCourseCode()).isEqualTo(DEFAULT_COURSE_CODE);
        assertThat(testCourseInfo.getCourseName()).isEqualTo(DEFAULT_COURSE_NAME);
        assertThat(testCourseInfo.getCourseCount()).isEqualTo(DEFAULT_COURSE_COUNT);
        assertThat(testCourseInfo.getCourseWeekCount()).isEqualTo(DEFAULT_COURSE_WEEK_COUNT);
        assertThat(testCourseInfo.getCourseMemo()).isEqualTo(DEFAULT_COURSE_MEMO);
        assertThat(testCourseInfo.getDataTime()).isEqualTo(DEFAULT_DATA_TIME);
        assertThat(testCourseInfo.getCourseUser()).isEqualTo(DEFAULT_COURSE_USER);
    }

    @Test
    @Transactional
    public void createCourseInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseInfoRepository.findAll().size();

        // Create the CourseInfo with an existing ID
        courseInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseInfoMockMvc.perform(post("/api/course-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseInfo)))
            .andExpect(status().isBadRequest());

        // Validate the CourseInfo in the database
        List<CourseInfo> courseInfoList = courseInfoRepository.findAll();
        assertThat(courseInfoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCourseInfos() throws Exception {
        // Initialize the database
        courseInfoRepository.saveAndFlush(courseInfo);

        // Get all the courseInfoList
        restCourseInfoMockMvc.perform(get("/api/course-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].courseCode").value(hasItem(DEFAULT_COURSE_CODE.toString())))
            .andExpect(jsonPath("$.[*].courseName").value(hasItem(DEFAULT_COURSE_NAME.toString())))
            .andExpect(jsonPath("$.[*].courseCount").value(hasItem(DEFAULT_COURSE_COUNT)))
            .andExpect(jsonPath("$.[*].courseWeekCount").value(hasItem(DEFAULT_COURSE_WEEK_COUNT)))
            .andExpect(jsonPath("$.[*].courseMemo").value(hasItem(DEFAULT_COURSE_MEMO.toString())))
            .andExpect(jsonPath("$.[*].dataTime").value(hasItem(sameInstant(DEFAULT_DATA_TIME))))
            .andExpect(jsonPath("$.[*].courseUser").value(hasItem(DEFAULT_COURSE_USER.toString())));
    }
    
    @Test
    @Transactional
    public void getCourseInfo() throws Exception {
        // Initialize the database
        courseInfoRepository.saveAndFlush(courseInfo);

        // Get the courseInfo
        restCourseInfoMockMvc.perform(get("/api/course-infos/{id}", courseInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(courseInfo.getId().intValue()))
            .andExpect(jsonPath("$.courseCode").value(DEFAULT_COURSE_CODE.toString()))
            .andExpect(jsonPath("$.courseName").value(DEFAULT_COURSE_NAME.toString()))
            .andExpect(jsonPath("$.courseCount").value(DEFAULT_COURSE_COUNT))
            .andExpect(jsonPath("$.courseWeekCount").value(DEFAULT_COURSE_WEEK_COUNT))
            .andExpect(jsonPath("$.courseMemo").value(DEFAULT_COURSE_MEMO.toString()))
            .andExpect(jsonPath("$.dataTime").value(sameInstant(DEFAULT_DATA_TIME)))
            .andExpect(jsonPath("$.courseUser").value(DEFAULT_COURSE_USER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCourseInfo() throws Exception {
        // Get the courseInfo
        restCourseInfoMockMvc.perform(get("/api/course-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourseInfo() throws Exception {
        // Initialize the database
        courseInfoService.save(courseInfo);

        int databaseSizeBeforeUpdate = courseInfoRepository.findAll().size();

        // Update the courseInfo
        CourseInfo updatedCourseInfo = courseInfoRepository.findById(courseInfo.getId()).get();
        // Disconnect from session so that the updates on updatedCourseInfo are not directly saved in db
        em.detach(updatedCourseInfo);
        updatedCourseInfo
            .courseCode(UPDATED_COURSE_CODE)
            .courseName(UPDATED_COURSE_NAME)
            .courseCount(UPDATED_COURSE_COUNT)
            .courseWeekCount(UPDATED_COURSE_WEEK_COUNT)
            .courseMemo(UPDATED_COURSE_MEMO)
            .dataTime(UPDATED_DATA_TIME)
            .courseUser(UPDATED_COURSE_USER);

        restCourseInfoMockMvc.perform(put("/api/course-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourseInfo)))
            .andExpect(status().isOk());

        // Validate the CourseInfo in the database
        List<CourseInfo> courseInfoList = courseInfoRepository.findAll();
        assertThat(courseInfoList).hasSize(databaseSizeBeforeUpdate);
        CourseInfo testCourseInfo = courseInfoList.get(courseInfoList.size() - 1);
        assertThat(testCourseInfo.getCourseCode()).isEqualTo(UPDATED_COURSE_CODE);
        assertThat(testCourseInfo.getCourseName()).isEqualTo(UPDATED_COURSE_NAME);
        assertThat(testCourseInfo.getCourseCount()).isEqualTo(UPDATED_COURSE_COUNT);
        assertThat(testCourseInfo.getCourseWeekCount()).isEqualTo(UPDATED_COURSE_WEEK_COUNT);
        assertThat(testCourseInfo.getCourseMemo()).isEqualTo(UPDATED_COURSE_MEMO);
        assertThat(testCourseInfo.getDataTime()).isEqualTo(UPDATED_DATA_TIME);
        assertThat(testCourseInfo.getCourseUser()).isEqualTo(UPDATED_COURSE_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingCourseInfo() throws Exception {
        int databaseSizeBeforeUpdate = courseInfoRepository.findAll().size();

        // Create the CourseInfo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseInfoMockMvc.perform(put("/api/course-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseInfo)))
            .andExpect(status().isBadRequest());

        // Validate the CourseInfo in the database
        List<CourseInfo> courseInfoList = courseInfoRepository.findAll();
        assertThat(courseInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourseInfo() throws Exception {
        // Initialize the database
        courseInfoService.save(courseInfo);

        int databaseSizeBeforeDelete = courseInfoRepository.findAll().size();

        // Delete the courseInfo
        restCourseInfoMockMvc.perform(delete("/api/course-infos/{id}", courseInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseInfo> courseInfoList = courseInfoRepository.findAll();
        assertThat(courseInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseInfo.class);
        CourseInfo courseInfo1 = new CourseInfo();
        courseInfo1.setId(1L);
        CourseInfo courseInfo2 = new CourseInfo();
        courseInfo2.setId(courseInfo1.getId());
        assertThat(courseInfo1).isEqualTo(courseInfo2);
        courseInfo2.setId(2L);
        assertThat(courseInfo1).isNotEqualTo(courseInfo2);
        courseInfo1.setId(null);
        assertThat(courseInfo1).isNotEqualTo(courseInfo2);
    }
}
