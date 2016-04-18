package eu.ownyourdata.sam.web.rest;

import eu.ownyourdata.sam.Application;
import eu.ownyourdata.sam.domain.Plugin;
import eu.ownyourdata.sam.repository.PluginRepository;
import eu.ownyourdata.sam.repository.UserRepository;

import eu.ownyourdata.sam.web.rest.mapper.PluginMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PluginResource REST controller.
 *
 * @see PluginResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PluginResourceIntTest {

    private static final String DEFAULT_IDENTIFIER = "AAAAA";
    private static final String UPDATED_IDENTIFIER = "BBBBB";
    private static final String DEFAULT_VERSION = "AAAAA";
    private static final String UPDATED_VERSION = "BBBBB";

    private static final Integer DEFAULT_VERSION_NUMBER = 1;
    private static final Integer UPDATED_VERSION_NUMBER = 2;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final byte[] DEFAULT_ZIP = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ZIP = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_ZIP_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ZIP_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_DOWNLOADS = 1;
    private static final Integer UPDATED_DOWNLOADS = 2;

    private static final Double DEFAULT_RATINGS = 1D;
    private static final Double UPDATED_RATINGS = 2D;

    @Inject
    private PluginRepository pluginRepository;

    @Inject
    private PluginMapper pluginMapper;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPluginMockMvc;

    private Plugin plugin;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PluginResource pluginResource = new PluginResource();
        ReflectionTestUtils.setField(pluginResource, "pluginRepository", pluginRepository);
        ReflectionTestUtils.setField(pluginResource, "pluginMapper", pluginMapper);
        this.restPluginMockMvc = MockMvcBuilders.standaloneSetup(pluginResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        plugin = new Plugin();
        plugin.setIdentifier(DEFAULT_IDENTIFIER);
        plugin.setVersion(DEFAULT_VERSION);
        plugin.setVersionNumber(DEFAULT_VERSION_NUMBER);
        plugin.setDescription(DEFAULT_DESCRIPTION);
        plugin.setZip(DEFAULT_ZIP);
        plugin.setZipContentType(DEFAULT_ZIP_CONTENT_TYPE);
        plugin.setDownloads(DEFAULT_DOWNLOADS);
        plugin.setRatings(DEFAULT_RATINGS);
        plugin.setUploadedBy(userRepository.findOneByLogin("user").get());
    }

    @Test
    @Transactional
    public void getAllPlugins() throws Exception {
        // Initialize the database
        pluginRepository.saveAndFlush(plugin);

        // Get all the plugins
        restPluginMockMvc.perform(get("/api/plugins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plugin.getId().intValue())))
            .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].versionNumber").value(hasItem(DEFAULT_VERSION_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].zipContentType").doesNotExist())
            .andExpect(jsonPath("$.[*].zip").doesNotExist())
            .andExpect(jsonPath("$.[*].downloads").value(hasItem(DEFAULT_DOWNLOADS)))
            .andExpect(jsonPath("$.[*].ratings").value(hasItem(DEFAULT_RATINGS.doubleValue())))
            .andExpect(jsonPath("$.[*].uploadedBy.resetKey").doesNotExist())
            .andExpect(jsonPath("$.[*].uploadedBy.resetDate").doesNotExist())
            .andExpect(jsonPath("$.[*].uploadedBy.email").doesNotExist())
            .andExpect(jsonPath("$.[*].uploadedBy.firstName").doesNotExist())
            .andExpect(jsonPath("$.[*].uploadedBy.lastName").doesNotExist());
    }

    @Test
    @Transactional
    public void getPlugin() throws Exception {
        // Initialize the database
        pluginRepository.saveAndFlush(plugin);

        // Get the plugin
        restPluginMockMvc.perform(get("/api/plugins/{id}", plugin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(plugin.getId().intValue()))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()))
            .andExpect(jsonPath("$.versionNumber").value(DEFAULT_VERSION_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.zipContentType").doesNotExist())
            .andExpect(jsonPath("$.zip").doesNotExist())
            .andExpect(jsonPath("$.downloads").value(DEFAULT_DOWNLOADS))
            .andExpect(jsonPath("$.ratings").value(DEFAULT_RATINGS.doubleValue()))
            .andExpect(jsonPath("$.uploadedBy.resetKey").doesNotExist())
            .andExpect(jsonPath("$.uploadedBy.resetDate").doesNotExist())
            .andExpect(jsonPath("$.uploadedBy.email").doesNotExist())
            .andExpect(jsonPath("$.uploadedBy.firstName").doesNotExist())
            .andExpect(jsonPath("$.uploadedBy.lastName").doesNotExist());
    }

    @Test
    @Transactional
    public void getNonExistingPlugin() throws Exception {
        // Get the plugin
        restPluginMockMvc.perform(get("/api/plugins/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void deletePlugin() throws Exception {
        // Initialize the database
        pluginRepository.saveAndFlush(plugin);

		int databaseSizeBeforeDelete = pluginRepository.findAll().size();

        // Get the plugin
        restPluginMockMvc.perform(delete("/api/plugins/{id}", plugin.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Plugin> plugins = pluginRepository.findAll();
        assertThat(plugins).hasSize(databaseSizeBeforeDelete - 1);
    }
}
