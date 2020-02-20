package com.carspark.restapi.rest;

import com.carspark.restapi.model.AutocompleteBody;
import com.carspark.restapi.model.GetAllBody;
import com.carspark.restapi.sql.Schemas;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceRestController {

    private final boolean debug = true;
    private final Logger LOG = LoggerFactory.getLogger(ResourceRestController.class);

    private final Schemas schemas;

    @Autowired
    public ResourceRestController(Schemas schemas) {
        this.schemas = schemas;
    }


    @PostMapping(path = "/autocomplete/{resource}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Map<String, Object>> autocomplete(
        @PathVariable("resource") String resource,
        @RequestParam(value = "fts") String fts,
        @RequestBody AutocompleteBody body
    ) {

        return null;
    }

    @PostMapping(path = "/{resource}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Map<String, Object>> getAll(
        @PathVariable("resource") String resource,
        @RequestParam(value = "skip", required = false, defaultValue = "0") Integer skip,
        @RequestParam(value = "limit", defaultValue = "30") Integer limit,
        @RequestBody  GetAllBody body
    ) {
        body.setSkipAndLimit(skip, limit);

        return null;
    }

}
