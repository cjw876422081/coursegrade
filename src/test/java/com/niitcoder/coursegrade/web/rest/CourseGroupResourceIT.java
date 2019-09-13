package com.niitcoder.coursegrade.web.rest;

import com.niitcoder.coursegrade.CoursegradeApp;
import com.niitcoder.coursegrade.domain.CourseGroup;
import com.niitcoder.coursegrade.repository.CourseGroupRepository;
import com.niitcoder.coursegrade.service.CourseGroupService;
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
 * Integration tests for the {@link CourseGroupResource} REST controller.
 */
@SpringBootTest(classes = CoursegradeApp.class)
public class CourseGroupResourceIT {

    private static final String DEFAULT_GROUP_CODE = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_GROUP_COUNT = 1;
    private static final Integer UPDATED_GROUP_COUNT = 2;
    private static final Integer SMALLER_GROUP_COUNT = 1 - 1;

    private static final ZonedDateTime DEFAULT_DATA_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private CourseGroupRepository courseGroupRepository;

    @Autowired
    private CourseGroupService courseGroupService;

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

    private MockMvc restCourseGroupMockMvc;

    private CourseGroup courseGroup;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourseGroupResource courseGroupResource = new CourseGroupResource(courseGroupService);
        this.restCourseGroupMockMvc = MockMvcBuilders.standaloneSetup(courseGroupResource)
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
    public static CourseGroup createEntity(EntityManager em) {
        CourseGroup courseGroup = new CourseGroup()
            .groupCode(DEFAULT_GROUP_CODE)
            .groupName(DEFAULT_GROUP_NAME)
            .groupCount(DEFAULT_GROUP_COUNT)
            .dataTime(DEFAULT_DATA_TIME);
        return courseGroup;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseGroup createUpdatedEntity(EntityManager em) {
        CourseGroup courseGroup = new CourseGroup()
            .groupCode(UPDATED_GROUP_CODE)
            .groupName(UPDATED_GROUP_NAME)
            .groupCount(UPDATED_GROUP_COUNT)
            .dataTime(UPDATED_DATA_TIME);
        return courseGroup;
    }

    @BeforeEach
    public void initTest() {
        courseGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourseGroup() throws Exception {
        int databaseSizeBeforeCreate = courseGroupRepository.findAll().size();

        // Create the CourseGroup
        restCourseGroupMockMvc.perform(post("/api/course-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseGroup)))
            .andExpect(status().isCreated());

        // Validate the CourseGroup in the database
        List<CourseGroup> courseGroupList = courseGroupRepository.findAll();
        assertThat(courseGroupList).hasSize(databaseSizeBeforeCreate + 1);
        CourseGroup testCourseGroup = courseGroupList.get(courseGroupList.size() - 1);
        assertThat(testCourseGroup.getGroupCode()).isEqualTo(DEFAULT_GROUP_CODE);
        assertThat(testCourseGroup.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testCourseGroup.getGroupCount()).isEqualTo(DEFAULT_GROUP_COUNT);
        assertThat(testCourseGroup.getDataTime()).isEqualTo(DEFAULT_DATA_TIME);
    }

    @Test
    @Transactional
    public void createCourseGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseGroupRepository.findAll().size();

        // Create the CourseGroup with an existing ID
        courseGroup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseGroupMockMvc.perform(post("/api/course-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseGroup)))
            .andExpect(status().isBadRequest());

        // Validate the CourseGroup in the database
        List<CourseGroup> courseGroupList = courseGroupRepository.findAll();
        assertThat(courseGroupList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCourseGroups() throws Exception {
        // Initialize the database
        courseGroupRepository.saveAndFlush(courseGroup);

        // Get all the courseGroupList
        restCourseGroupMockMvc.perform(get("/api/course-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupCode").value(hasItem(DEFAULT_GROUP_CODE.toString())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME.toString())))
            .andExpect(jsonPath("$.[*].groupCount").value(hasItem(DEFAULT_GROUP_COUNT)))
            .andExpect(jsonPath("$.[*].dataTime").value(hasItem(sameInstant(DEFAULT_DATA_TIME))));
    }
    
    @Test
    @Transactional
    public void getCourseGroup() throws Exception {
        // Initialize the database
        courseGroupRepository.saveAndFlush(courseGroup);

        // Get the courseGroup
        restCourseGroupMockMvc.perform(get("/api/course-groups/{id}", courseGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(courseGroup.getId().intValue()))
            .andExpect(jsonPath("$.groupCode").value(DEFAULT_GROUP_CODE.toString()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME.toString()))
            .andExpect(jsonPath("$.groupCount").value(DEFAULT_GROUP_COUNT))
            .andExpect(jsonPath("$.dataTime").value(sameInstant(DEFAULT_DATA_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingCourseGroup() throws Exception {
        // Get the courseGroup
        restCourseGroupMockMvc.perform(get("/api/course-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourseGroup() throws Exception {
        // Initialize the database
        courseGroupService.save(courseGroup);

        int databaseSizeBeforeUpdate = courseGroupRepository.findAll().size();

        // Update the courseGroup
        CourseGroup updatedCourseGroup = courseGroupRepository.findById(courseGroup.getId()).get();
        // Disconnect from session so that the updates on updatedCourseGroup are not directly saved in db
        em.detach(updatedCourseGroup);
        updatedCourseGroup
            .groupCode(UPDATED_GROUP_CODE)
            .groupName(UPDATED_GROUP_NAME)
            .groupCount(UPDATED_GROUP_COUNT)
            .dataTime(UPDATED_DATA_TIME);

        restCourseGroupMockMvc.perform(put("/api/course-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourseGroup)))
            .andExpect(status().isOk());

        // Validate the CourseGroup in the database
        List<CourseGroup> courseGroupList = courseGroupRepository.findAll();
        assertThat(courseGroupList).hasSize(databaseSizeBeforeUpdate);
        CourseGroup testCourseGroup = courseGroupList.get(courseGroupList.size() - 1);
        assertThat(testCourseGroup.getGroupCode()).isEqualTo(UPDATED_GROUP_CODE);
        assertThat(testCourseGroup.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testCourseGroup.getGroupCount()).isEqualTo(UPDATED_GROUP_COUNT);
        assertThat(testCourseGroup.getDataTime()).isEqualTo(UPDATED_DATA_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingCourseGroup() throws Exception {
        int databaseSizeBeforeUpdate = courseGroupRepository.findAll().size();

        // Create the CourseGroup

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseGroupMockMvc.perform(put("/api/course-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseGroup)))
            .andExpect(status().isBadRequest());

        // Validate the CourseGroup in the database
        List<CourseGroup> courseGroupList = courseGroupRepository.findAll();
        assertThat(courseGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourseGroup() throws Exception {
        // Initialize the database
        courseGroupService.save(courseGroup);

        int databaseSizeBeforeDelete = courseGroupRepository.findAll().size();

        // Delete the courseGroup
        restCourseGroupMockMvc.perform(delete("/api/course-groups/{id}", courseGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseGroup> courseGroupList = courseGroupRepository.findAll();
        assertThat(courseGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseGroup.class);
        CourseGroup courseGroup1 = new CourseGroup();
        courseGroup1.setId(1L);
        CourseGroup courseGroup2 = new CourseGroup();
        courseGroup2.setId(courseGroup1.getId());
        assertThat(courseGroup1).isEqualTo(courseGroup2);
        courseGroup2.setId(2L);
        assertThat(courseGroup1).isNotEqualTo(courseGroup2);
        courseGroup1.setId(null);
        assertThat(courseGroup1).isNotEqualTo(courseGroup2);
    }
}
