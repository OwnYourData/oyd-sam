package eu.ownyourdata.sam.repository.search;

import eu.ownyourdata.sam.domain.Plugin;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Plugin entity.
 */
public interface PluginSearchRepository extends ElasticsearchRepository<Plugin, Long> {
}
