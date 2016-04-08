package eu.ownyourdata.sam.web.rest.mapper;

import eu.ownyourdata.sam.domain.Plugin;
import eu.ownyourdata.sam.web.rest.dto.PluginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by michael on 08.04.16.
 */
public abstract class PluginMapperDecorator implements PluginMapper {

    @Autowired
    @Qualifier("delegate")
    private PluginMapper delegate;

    @Override
    public PluginDTO pluginToPluginDTO(Plugin plugin) {
        PluginDTO pluginDTO = delegate.pluginToPluginDTO(plugin);
        if (pluginDTO != null) {
            pluginDTO.setZip(null);
            pluginDTO.setZipContentType(null);
            pluginDTO.setUploadedByName(plugin.getUploadedBy().getFirstName() + " " + plugin.getUploadedBy().getLastName());
        }
        return pluginDTO;
    }
}
