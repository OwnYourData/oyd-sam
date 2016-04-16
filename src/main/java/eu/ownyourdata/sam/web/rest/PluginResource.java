package eu.ownyourdata.sam.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.ownyourdata.sam.domain.Plugin;
import eu.ownyourdata.sam.repository.PluginRepository;
import eu.ownyourdata.sam.repository.UserRepository;
import eu.ownyourdata.sam.repository.search.PluginSearchRepository;
import eu.ownyourdata.sam.web.rest.dto.PluginDTO;
import eu.ownyourdata.sam.web.rest.dto.PluginUploadDTO;
import eu.ownyourdata.sam.web.rest.mapper.PluginMapper;
import eu.ownyourdata.sam.web.rest.util.HeaderUtil;
import eu.ownyourdata.sam.web.rest.util.PaginationUtil;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing Plugin.
 */
@RestController
@RequestMapping("/api")
public class PluginResource {

    private final Logger log = LoggerFactory.getLogger(PluginResource.class);

    @Inject
    private PluginRepository pluginRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PluginMapper pluginMapper;

    @Inject
    private PluginSearchRepository pluginSearchRepository;

    /**
     * POST  /plugins -> Create a new plugin.
     */
    @RequestMapping(value = "/plugins/upload",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PluginDTO> createPlugin(@Valid @RequestBody PluginUploadDTO upload,Principal principal) throws URISyntaxException {
        String manifest = null;
        try {
            ZipInputStream z = new ZipInputStream(new ByteArrayInputStream(upload.getZip()));
            ZipEntry entry;

            while ((entry = z.getNextEntry()) != null) {
                if (entry.getName().equals("manifest.json")) {
                    manifest = IOUtils.toString(z);
                    break;
                }
            }
        } catch (IOException e) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("plugin", "zip", "zip file has invalid content")).body(null);
        }

        if (manifest == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("plugin", "zip", "No manifest.json found in zip file")).body(null);
        } else {
            try {
                JSONObject object = new JSONObject(manifest);
                Plugin plugin = new Plugin();
                plugin.setIdentifier(object.getString("identifier"));
                plugin.setZipContentType(upload.getZipContentType());
                plugin.setZip(upload.getZip());
                plugin.setUploadedBy(userRepository.findCurrentUser().get());
                plugin.setDescription(object.getString("description"));
                plugin.setDownloads(0);
                plugin.setRatings(-1d);
                plugin.setVersion(object.optString("version","1.0.0"));
                plugin.setVersionNumber(object.optInt("build",1));

                if (object.has("requires")) {
                    plugin.setRequires(object.getJSONArray("requires").toString());
                }
                if (object.has("permissions")) {
                    plugin.setPermissions(object.getJSONArray("permissions").toString());
                }

                pluginRepository.save(plugin);

                PluginDTO result = pluginMapper.pluginToPluginDTO(plugin);
                pluginSearchRepository.save(plugin);
                return ResponseEntity.created(new URI("/api/plugins/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert("plugin", result.getId().toString()))
                    .body(result);
            } catch (JSONException e) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("plugin", "zip", "manifest.json is invalid: "+e.getMessage())).body(null);
            }
        }
    }

    /**
     * GET  /plugins -> get all the plugins.
     */
    @RequestMapping(value = "/plugins",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<PluginDTO>> getAllPlugins(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Plugins");
        Page<Plugin> page = pluginRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/plugins");
        return new ResponseEntity<>(page.getContent().stream()
            .map(pluginMapper::pluginToPluginDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /plugins/:id -> get the "id" plugin.
     */
    @RequestMapping(value = "/plugins/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PluginDTO> getPlugin(@PathVariable Long id) {
        log.debug("REST request to get Plugin : {}", id);
        Plugin plugin = pluginRepository.findOne(id);
        PluginDTO pluginDTO = pluginMapper.pluginToPluginDTO(plugin);
        return Optional.ofNullable(pluginDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /plugins/:id -> get the "id" plugin.
     */
    @RequestMapping(value = "/plugins/{id}/download",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public void getPlugin(@PathVariable Long id, HttpServletResponse response) {
        Plugin plugin = pluginRepository.findOne(id);
        response.setContentType(plugin.getZipContentType());

        try {
            IOUtils.write(plugin.getZip(), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        plugin.setDownloads(plugin.getDownloads() + 1);
        pluginRepository.save(plugin);
    }

    /**
     * DELETE  /plugins/:id -> delete the "id" plugin.
     */
    @RequestMapping(value = "/plugins/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePlugin(@PathVariable Long id) {
        log.debug("REST request to delete Plugin : {}", id);
        pluginRepository.delete(id);
        pluginSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("plugin", id.toString())).build();
    }

    /**
     * SEARCH  /_search/plugins/:query -> search for the plugin corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/plugins/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PluginDTO> searchPlugins(@PathVariable String query) {
        log.debug("REST request to search Plugins for query {}", query);
        return StreamSupport
            .stream(pluginSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(pluginMapper::pluginToPluginDTO)
            .collect(Collectors.toList());
    }
}
