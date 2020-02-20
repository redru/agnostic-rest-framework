package com.carspark.restapi.mapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ResourcesMapper extends HashMap<String, ResourcesMapper> {

    @JsonIgnore
    public ResourcesMapper getResource(String resource) {
        return this.get(resource);
    }
}
