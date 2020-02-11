package com.abbey.vgabbey.web.rest;

import com.abbey.vgabbey.VgAbbeyApp;
import com.abbey.vgabbey.domain.Videogame;
import com.abbey.vgabbey.repository.VideogameRepository;
import com.abbey.vgabbey.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import java.util.ArrayList;
import java.util.List;

import static com.abbey.vgabbey.web.rest.TestUtil.sameInstant;
import static com.abbey.vgabbey.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link VideogameResource} REST controller.
 */
@SpringBootTest(classes = VgAbbeyApp.class)
public class VideogameResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_REPOSITORY_URL = "AAAAAAAAAA";
    private static final String UPDATED_REPOSITORY_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DEPLOY_URL = "AAAAAAAAAA";
    private static final String UPDATED_DEPLOY_URL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_RELEASE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RELEASE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_IS_SEARCHING_COLLAB = false;
    private static final Boolean UPDATED_IS_SEARCHING_COLLAB = true;

    @Autowired
    private VideogameRepository videogameRepository;

    @Mock
    private VideogameRepository videogameRepositoryMock;

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

    private MockMvc restVideogameMockMvc;

    private Videogame videogame;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VideogameResource videogameResource = new VideogameResource(videogameRepository);
        this.restVideogameMockMvc = MockMvcBuilders.standaloneSetup(videogameResource)
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
    public static Videogame createEntity(EntityManager em) {
        Videogame videogame = new Videogame()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .imageUrl(DEFAULT_IMAGE_URL)
            .repositoryUrl(DEFAULT_REPOSITORY_URL)
            .deployUrl(DEFAULT_DEPLOY_URL)
            .releaseDate(DEFAULT_RELEASE_DATE)
            .isSearchingCollab(DEFAULT_IS_SEARCHING_COLLAB);
        return videogame;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Videogame createUpdatedEntity(EntityManager em) {
        Videogame videogame = new Videogame()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .imageUrl(UPDATED_IMAGE_URL)
            .repositoryUrl(UPDATED_REPOSITORY_URL)
            .deployUrl(UPDATED_DEPLOY_URL)
            .releaseDate(UPDATED_RELEASE_DATE)
            .isSearchingCollab(UPDATED_IS_SEARCHING_COLLAB);
        return videogame;
    }

    @BeforeEach
    public void initTest() {
        videogame = createEntity(em);
    }

    @Test
    @Transactional
    public void createVideogame() throws Exception {
        int databaseSizeBeforeCreate = videogameRepository.findAll().size();

        // Create the Videogame
        restVideogameMockMvc.perform(post("/api/videogames")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(videogame)))
            .andExpect(status().isCreated());

        // Validate the Videogame in the database
        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeCreate + 1);
        Videogame testVideogame = videogameList.get(videogameList.size() - 1);
        assertThat(testVideogame.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testVideogame.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVideogame.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testVideogame.getRepositoryUrl()).isEqualTo(DEFAULT_REPOSITORY_URL);
        assertThat(testVideogame.getDeployUrl()).isEqualTo(DEFAULT_DEPLOY_URL);
        assertThat(testVideogame.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testVideogame.isIsSearchingCollab()).isEqualTo(DEFAULT_IS_SEARCHING_COLLAB);
    }

    @Test
    @Transactional
    public void createVideogameWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = videogameRepository.findAll().size();

        // Create the Videogame with an existing ID
        videogame.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideogameMockMvc.perform(post("/api/videogames")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(videogame)))
            .andExpect(status().isBadRequest());

        // Validate the Videogame in the database
        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = videogameRepository.findAll().size();
        // set the field null
        videogame.setTitle(null);

        // Create the Videogame, which fails.

        restVideogameMockMvc.perform(post("/api/videogames")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(videogame)))
            .andExpect(status().isBadRequest());

        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRepositoryUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = videogameRepository.findAll().size();
        // set the field null
        videogame.setRepositoryUrl(null);

        // Create the Videogame, which fails.

        restVideogameMockMvc.perform(post("/api/videogames")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(videogame)))
            .andExpect(status().isBadRequest());

        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsSearchingCollabIsRequired() throws Exception {
        int databaseSizeBeforeTest = videogameRepository.findAll().size();
        // set the field null
        videogame.setIsSearchingCollab(null);

        // Create the Videogame, which fails.

        restVideogameMockMvc.perform(post("/api/videogames")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(videogame)))
            .andExpect(status().isBadRequest());

        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVideogames() throws Exception {
        // Initialize the database
        videogameRepository.saveAndFlush(videogame);

        // Get all the videogameList
        restVideogameMockMvc.perform(get("/api/videogames?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videogame.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].repositoryUrl").value(hasItem(DEFAULT_REPOSITORY_URL)))
            .andExpect(jsonPath("$.[*].deployUrl").value(hasItem(DEFAULT_DEPLOY_URL)))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(sameInstant(DEFAULT_RELEASE_DATE))))
            .andExpect(jsonPath("$.[*].isSearchingCollab").value(hasItem(DEFAULT_IS_SEARCHING_COLLAB.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllVideogamesWithEagerRelationshipsIsEnabled() throws Exception {
        VideogameResource videogameResource = new VideogameResource(videogameRepositoryMock);
        when(videogameRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restVideogameMockMvc = MockMvcBuilders.standaloneSetup(videogameResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restVideogameMockMvc.perform(get("/api/videogames?eagerload=true"))
        .andExpect(status().isOk());

        verify(videogameRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllVideogamesWithEagerRelationshipsIsNotEnabled() throws Exception {
        VideogameResource videogameResource = new VideogameResource(videogameRepositoryMock);
            when(videogameRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restVideogameMockMvc = MockMvcBuilders.standaloneSetup(videogameResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restVideogameMockMvc.perform(get("/api/videogames?eagerload=true"))
        .andExpect(status().isOk());

            verify(videogameRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getVideogame() throws Exception {
        // Initialize the database
        videogameRepository.saveAndFlush(videogame);

        // Get the videogame
        restVideogameMockMvc.perform(get("/api/videogames/{id}", videogame.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(videogame.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.repositoryUrl").value(DEFAULT_REPOSITORY_URL))
            .andExpect(jsonPath("$.deployUrl").value(DEFAULT_DEPLOY_URL))
            .andExpect(jsonPath("$.releaseDate").value(sameInstant(DEFAULT_RELEASE_DATE)))
            .andExpect(jsonPath("$.isSearchingCollab").value(DEFAULT_IS_SEARCHING_COLLAB.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingVideogame() throws Exception {
        // Get the videogame
        restVideogameMockMvc.perform(get("/api/videogames/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVideogame() throws Exception {
        // Initialize the database
        videogameRepository.saveAndFlush(videogame);

        int databaseSizeBeforeUpdate = videogameRepository.findAll().size();

        // Update the videogame
        Videogame updatedVideogame = videogameRepository.findById(videogame.getId()).get();
        // Disconnect from session so that the updates on updatedVideogame are not directly saved in db
        em.detach(updatedVideogame);
        updatedVideogame
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .imageUrl(UPDATED_IMAGE_URL)
            .repositoryUrl(UPDATED_REPOSITORY_URL)
            .deployUrl(UPDATED_DEPLOY_URL)
            .releaseDate(UPDATED_RELEASE_DATE)
            .isSearchingCollab(UPDATED_IS_SEARCHING_COLLAB);

        restVideogameMockMvc.perform(put("/api/videogames")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVideogame)))
            .andExpect(status().isOk());

        // Validate the Videogame in the database
        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeUpdate);
        Videogame testVideogame = videogameList.get(videogameList.size() - 1);
        assertThat(testVideogame.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVideogame.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVideogame.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testVideogame.getRepositoryUrl()).isEqualTo(UPDATED_REPOSITORY_URL);
        assertThat(testVideogame.getDeployUrl()).isEqualTo(UPDATED_DEPLOY_URL);
        assertThat(testVideogame.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testVideogame.isIsSearchingCollab()).isEqualTo(UPDATED_IS_SEARCHING_COLLAB);
    }

    @Test
    @Transactional
    public void updateNonExistingVideogame() throws Exception {
        int databaseSizeBeforeUpdate = videogameRepository.findAll().size();

        // Create the Videogame

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideogameMockMvc.perform(put("/api/videogames")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(videogame)))
            .andExpect(status().isBadRequest());

        // Validate the Videogame in the database
        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVideogame() throws Exception {
        // Initialize the database
        videogameRepository.saveAndFlush(videogame);

        int databaseSizeBeforeDelete = videogameRepository.findAll().size();

        // Delete the videogame
        restVideogameMockMvc.perform(delete("/api/videogames/{id}", videogame.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
