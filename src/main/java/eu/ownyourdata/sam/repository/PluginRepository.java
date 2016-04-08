package eu.ownyourdata.sam.repository;

import eu.ownyourdata.sam.domain.Plugin;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Plugin entity.
 */
public interface PluginRepository extends JpaRepository<Plugin,Long> {

    @Query("select plugin from Plugin plugin where plugin.uploadedBy.login = ?#{principal.username}")
    List<Plugin> findByUploadedByIsCurrentUser();

}
