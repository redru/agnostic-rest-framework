package com.carspark.restapi.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;


@Configuration
public class MapperConfiguration {

    private static Logger LOG= LoggerFactory.getLogger(MapperConfiguration.class);
    private static ObjectMapper MAPPER=new ObjectMapper();


    @Bean
    public ResourcesMapper getResourcesMapper() {
        ResourcesMapper ret=null;
        try {
            InputStream stream = new ClassPathResource("mapping.json").getInputStream();
            ret=MAPPER.readValue(stream, ResourcesMapper.class);
        }
        catch (Exception e) {
            LOG.error("Mapping field error", e);
        }
        return ret;
    }
}
