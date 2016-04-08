package eu.ownyourdata.sam.web.rest.mapper;

import eu.ownyourdata.sam.domain.Plugin;
import eu.ownyourdata.sam.domain.User;
import eu.ownyourdata.sam.web.rest.dto.PluginDTO;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Plugin and its DTO PluginDTO.
 */
@Mapper(componentModel = "spring", uses = {})
@DecoratedWith(PluginMapperDecorator.class)
public interface PluginMapper {

    @Mapping(source = "uploadedBy.id", target = "uploadedById")
    @Mapping(target = "uploadedByName", ignore = true) //provided by PluginMapperDecorator
    @Mapping(target = "zip", ignore=true) //provided by PluginMapperDecorator
    @Mapping(target = "zipContentType" , ignore=true) //provided by PluginMapperDecorator
    PluginDTO pluginToPluginDTO(Plugin plugin);

    @Mapping(source = "uploadedById", target = "uploadedBy")
    Plugin pluginDTOToPlugin(PluginDTO pluginDTO);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
