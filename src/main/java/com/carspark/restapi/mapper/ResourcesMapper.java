package com.carspark.restapi.mapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResourcesMapper extends HashMap<String, ResourceMapper> {

    @JsonIgnore
    public ResourceMapper getResource(String resource) {
        return this.get(resource);
    }
}
