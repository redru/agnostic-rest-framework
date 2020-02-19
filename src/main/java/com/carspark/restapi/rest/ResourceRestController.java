package com.carspark.restapi.rest;

import com.carspark.restapi.model.AutocompleteBody;
import com.carspark.restapi.model.GetAllBody;
import com.carspark.sql.Schemas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ResourceRestController {
    private final boolean debug=true;
    private static Logger LOG= LoggerFactory.getLogger(ResourceRestController.class);

    private Schemas schemas;

    @Autowired
    public ResourceRestController(Schemas schemas) {
        this.schemas=schemas;
    }


    @RequestMapping(path = "/autocomplete/{resource}", method = RequestMethod.POST)
    public @ResponseBody List<Map<String, Object>> autocomplete(@PathVariable("resource") String resource,
                                                                @RequestParam(value="fts") String fts,
                                                                @RequestBody AutocompleteBody body) {



       return null;
    }


    public @ResponseBody List<Map<String, Object>> getAll(@PathVariable("resource") String resource,
                            @RequestParam(value = "skip", required = false, defaultValue = "0") Integer skip,
                            @RequestParam(value = "limit", defaultValue = "30") Integer limit,
                            @RequestBody  GetAllBody body) {
        body.setSkipAndLimit(skip, limit);

        return null;
    }


}
