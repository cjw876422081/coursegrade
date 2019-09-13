package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.CoursegradeApp;
import com.niitcoder.coursegrade.domain.CoursePlan;
import com.niitcoder.coursegrade.repository.CoursePlanRepository;
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
 * Integration tests for the {@link CoursePlanResource} REST controller.
 */
@SpringBootTest(classes = CoursegradeApp.class)
public class CoursePlanResourceIT {

    private static final Integer DEFAULT_PLAN_CODE = 1;
    private static final Integer UPDATED_PLAN_CODE = 2;
    private static final Integer SMALLER_PLAN_CODE = 1 - 1;

    private static final String DEFAULT_PLAN_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_PLAN_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_PLAN_TARGET = "AAAAAAAAAA";
    private static final String UPDATED_PLAN_TARGET = "BBBBBBBBBB";

    private static final Integer DEFAULT_PLAN_COUNT = 1;
    private static final Integer UPDATED_PLAN_COUNT = 2;
    private static final Integer SMALLER_PLAN_COUNT = 1 - 1;

    private static final Integer DEFAULT_PLAN_PARENT_CODE = 1;
    private static final Integer UPDATED_PLAN_PARENT_CODE = 2;
    private static final Integer SMALLER_PLAN_PARENT_CODE = 1 - 1;

    private static final ZonedDateTime DEFAULT_DATA_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private CoursePlanRepository coursePlanRepository;

    @Autowired
    private CoursePlanService coursePlanService;

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

    private MockMvc restCoursePlanMockMvc;

    private CoursePlan coursePlan;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CoursePlanResource coursePlanResource = new CoursePlanResource(coursePlanService);
        this.restCoursePlanMockMvc = MockMvcBuilders.standaloneSetup(coursePlanResource)
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
    public static CoursePlan createEntity(EntityManager em) {
        CoursePlan coursePlan = new CoursePlan()
            .planCode(DEFAULT_PLAN_CODE)
            .planMemo(DEFAULT_PLAN_MEMO)
            .planTarget(DEFAULT_PLAN_TARGET)
            .planCount(DEFAULT_PLAN_COUNT)
            .planParentCode(DEFAULT_PLAN_PARENT_CODE)
            .dataTime(DEFAULT_DATA_TIME);
        return coursePlan;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoursePlan createUpdatedEntity(EntityManager em) {
        CoursePlan coursePlan = new CoursePlan()
            .planCode(UPDATED_PLAN_CODE)
            .planMemo(UPDATED_PLAN_MEMO)
            .planTarget(UPDATED_PLAN_TARGET)
            .planCount(UPDATED_PLAN_COUNT)
            .planParentCode(UPDATED_PLAN_PARENT_CODE)
            .dataTime(UPDATED_DATA_TIME);
        return coursePlan;
    }

    @BeforeEach
    public void initTest() {
        coursePlan = createEntity(em);
    }

    @Test
    @Transactional
    public void createCoursePlan() throws Exception {
        int databaseSizeBeforeCreate = coursePlanRepository.findAll().size();

        // Create the CoursePlan
        restCoursePlanMockMvc.perform(post("/api/course-plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coursePlan)))
            .andExpect(status().isCreated());

        // Validate the CoursePlan in the database
        List<CoursePlan> coursePlanList = coursePlanRepository.findAll();
        assertThat(coursePlanList).hasSize(databaseSizeBeforeCreate + 1);
        CoursePlan testCoursePlan = coursePlanList.get(coursePlanList.size() - 1);
        assertThat(testCoursePlan.getPlanCode()).isEqualTo(DEFAULT_PLAN_CODE);
        assertThat(testCoursePlan.getPlanMemo()).isEqualTo(DEFAULT_PLAN_MEMO);
        assertThat(testCoursePlan.getPlanTarget()).isEqualTo(DEFAULT_PLAN_TARGET);
        assertThat(testCoursePlan.getPlanCount()).isEqualTo(DEFAULT_PLAN_COUNT);
        assertThat(testCoursePlan.getPlanParentCode()).isEqualTo(DEFAULT_PLAN_PARENT_CODE);
        assertThat(testCoursePlan.getDataTime()).isEqualTo(DEFAULT_DATA_TIME);
    }

    @Test
    @Transactional
    public void createCoursePlanWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = coursePlanRepository.findAll().size();

        // Create the CoursePlan with an existing ID
        coursePlan.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoursePlanMockMvc.perform(post("/api/course-plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coursePlan)))
            .andExpect(status().isBadRequest());

        // Validate the CoursePlan in the database
        List<CoursePlan> coursePlanList = coursePlanRepository.findAll();
        assertThat(coursePlanList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCoursePlans() throws Exception {
        // Initialize the database
        coursePlanRepository.saveAndFlush(coursePlan);

        // Get all the coursePlanList
        restCoursePlanMockMvc.perform(get("/api/course-plans?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coursePlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].planCode").value(hasItem(DEFAULT_PLAN_CODE)))
            .andExpect(jsonPath("$.[*].planMemo").value(hasItem(DEFAULT_PLAN_MEMO.toString())))
            .andExpect(jsonPath("$.[*].planTarget").value(hasItem(DEFAULT_PLAN_TARGET.toString())))
            .andExpect(jsonPath("$.[*].planCount").value(hasItem(DEFAULT_PLAN_COUNT)))
            .andExpect(jsonPath("$.[*].planParentCode").value(hasItem(DEFAULT_PLAN_PARENT_CODE)))
            .andExpect(jsonPath("$.[*].dataTime").value(hasItem(sameInstant(DEFAULT_DATA_TIME))));
    }
    
    @Test
    @Transactional
    public void getCoursePlan() throws Exception {
        // Initialize the database
        coursePlanRepository.saveAndFlush(coursePlan);

        // Get the coursePlan
        restCoursePlanMockMvc.perform(get("/api/course-plans/{id}", coursePlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(coursePlan.getId().intValue()))
            .andExpect(jsonPath("$.planCode").value(DEFAULT_PLAN_CODE))
            .andExpect(jsonPath("$.planMemo").value(DEFAULT_PLAN_MEMO.toString()))
            .andExpect(jsonPath("$.planTarget").value(DEFAULT_PLAN_TARGET.toString()))
            .andExpect(jsonPath("$.planCount").value(DEFAULT_PLAN_COUNT))
            .andExpect(jsonPath("$.planParentCode").value(DEFAULT_PLAN_PARENT_CODE))
            .andExpect(jsonPath("$.dataTime").value(sameInstant(DEFAULT_DATA_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingCoursePlan() throws Exception {
        // Get the coursePlan
        restCoursePlanMockMvc.perform(get("/api/course-plans/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoursePlan() throws Exception {
        // Initialize the database
        coursePlanService.save(coursePlan);

        int databaseSizeBeforeUpdate = coursePlanRepository.findAll().size();

        // Update the coursePlan
        CoursePlan updatedCoursePlan = coursePlanRepository.findById(coursePlan.getId()).get();
        // Disconnect from session so that the updates on updatedCoursePlan are not directly saved in db
        em.detach(updatedCoursePlan);
        updatedCoursePlan
            .planCode(UPDATED_PLAN_CODE)
            .planMemo(UPDATED_PLAN_MEMO)
            .planTarget(UPDATED_PLAN_TARGET)
            .planCount(UPDATED_PLAN_COUNT)
            .planParentCode(UPDATED_PLAN_PARENT_CODE)
            .dataTime(UPDATED_DATA_TIME);

        restCoursePlanMockMvc.perform(put("/api/course-plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCoursePlan)))
            .andExpect(status().isOk());

        // Validate the CoursePlan in the database
        List<CoursePlan> coursePlanList = coursePlanRepository.findAll();
        assertThat(coursePlanList).hasSize(databaseSizeBeforeUpdate);
        CoursePlan testCoursePlan = coursePlanList.get(coursePlanList.size() - 1);
        assertThat(testCoursePlan.getPlanCode()).isEqualTo(UPDATED_PLAN_CODE);
        assertThat(testCoursePlan.getPlanMemo()).isEqualTo(UPDATED_PLAN_MEMO);
        assertThat(testCoursePlan.getPlanTarget()).isEqualTo(UPDATED_PLAN_TARGET);
        assertThat(testCoursePlan.getPlanCount()).isEqualTo(UPDATED_PLAN_COUNT);
        assertThat(testCoursePlan.getPlanParentCode()).isEqualTo(UPDATED_PLAN_PARENT_CODE);
        assertThat(testCoursePlan.getDataTime()).isEqualTo(UPDATED_DATA_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingCoursePlan() throws Exception {
        int databaseSizeBeforeUpdate = coursePlanRepository.findAll().size();

        // Create the CoursePlan

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoursePlanMockMvc.perform(put("/api/course-plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coursePlan)))
            .andExpect(status().isBadRequest());

        // Validate the CoursePlan in the database
        List<CoursePlan> coursePlanList = coursePlanRepository.findAll();
        assertThat(coursePlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCoursePlan() throws Exception {
        // Initialize the database
        coursePlanService.save(coursePlan);

        int databaseSizeBeforeDelete = coursePlanRepository.findAll().size();

        // Delete the coursePlan
        restCoursePlanMockMvc.perform(delete("/api/course-plans/{id}", coursePlan.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CoursePlan> coursePlanList = coursePlanRepository.findAll();
        assertThat(coursePlanList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoursePlan.class);
        CoursePlan coursePlan1 = new CoursePlan();
        coursePlan1.setId(1L);
        CoursePlan coursePlan2 = new CoursePlan();
        coursePlan2.setId(coursePlan1.getId());
        assertThat(coursePlan1).isEqualTo(coursePlan2);
        coursePlan2.setId(2L);
        assertThat(coursePlan1).isNotEqualTo(coursePlan2);
        coursePlan1.setId(null);
        assertThat(coursePlan1).isNotEqualTo(coursePlan2);
    }
}
