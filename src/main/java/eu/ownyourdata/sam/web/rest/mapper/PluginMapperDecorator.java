package eu.ownyourdata.sam.web.rest.mapper;

import eu.ownyourdata.sam.domain.Plugin;
import eu.ownyourdata.sam.web.rest.dto.PluginDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by michael on 08.04.16.
 */
public abstract class PluginMapperDecorator implements PluginMapper {
    private final Logger log = LoggerFactory.getLogger(PluginMapper.class);

    @Autowired
    @Qualifier("delegate")
    private PluginMapper delegate;

    @Override
    public PluginDTO pluginToPluginDTO(Plugin plugin) {
        PluginDTO pluginDTO = delegate.pluginToPluginDTO(plugin);
        if (pluginDTO != null) {
            pluginDTO.setZip(null);
            pluginDTO.setZipContentType(null);
            pluginDTO.setUploadedByName(plugin.getUploadedBy().getLogin());
            try {
                if (plugin.getPermissions() != null) {
                    pluginDTO.setPermissions(new JSONArray(plugin.getPermissions()));
                }
                if (plugin.getRequires() != null) {
                    pluginDTO.setRequires(new JSONArray(plugin.getRequires()));
                }
            } catch (JSONException e) {
                log.error("should never happen",e);
            }
        }
        return pluginDTO;
    }
}
