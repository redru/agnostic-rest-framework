package com.carspark.restapi.configuration;

import com.carspark.restapi.mapper.MappingRelation;
import com.carspark.restapi.mapper.MappingResources;
import com.carspark.restapi.mapper.MappingTable;
import com.carspark.restapi.configuration.properties.AgnosticFrameworkProperties;
import com.carspark.restapi.sql.Relation;
import com.carspark.restapi.sql.Schemas;
import com.carspark.restapi.sql.TableEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchemaConfiguration {

    private final AgnosticFrameworkProperties properties;

    @Autowired
    public SchemaConfiguration(AgnosticFrameworkProperties properties) {
        this.properties = properties;
    }

    @Bean
    public MappingResources getMappingResources(ObjectMapper objectMapper) throws IOException {
        try (InputStream mappingsStream = ClassLoader.getSystemClassLoader().getResourceAsStream(properties.getFile())) {
            if (mappingsStream == null) {
                throw new FileNotFoundException(properties.getFile() + " not found in resources");
            }

            return objectMapper.readValue(mappingsStream, MappingResources.class);
        }
    }

    @Bean
    public Schemas getSchemas(MappingResources mapping) throws Exception {
        Schemas schemas = new Schemas();

        for (Entry<String, MappingTable> entry : mapping.entrySet()) {
            String name = entry.getKey();
            MappingTable mappingTable = entry.getValue();

            // Create TableEntity
            TableEntity table = TableEntity.table(mappingTable.getTable(), mappingTable.getId());

            // If MappingTable has relations, map them
            MappingRelation mappingRelation = mappingTable.getRelation();

            if (mappingRelation != null) {
                Relation relation = table.relation(mappingRelation.getTable())
                    .id(mappingRelation.getId());

                // If a relation has not links, throw exception
                if (mappingRelation.getLinks() == null || mappingRelation.getLinks().isEmpty()) {
                    throw new Exception("Mapping '" + name + "' has a relation without links");
                }

                // Map links
                mappingRelation.getLinks().forEach(mappingLink ->
                    relation.link(mappingLink.getLeft(), mappingLink.getRight()));
            }

            schemas.register(table);
        }

        return schemas;
    }

}
