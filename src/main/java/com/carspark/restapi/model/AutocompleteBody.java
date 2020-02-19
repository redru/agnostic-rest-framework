package com.carspark.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AutocompleteBody {
    @JsonIgnore public Integer MAX_RESULT=5;

    public String id;
    public String label;
    public List<String> fields;

    public AutocompleteBody() {}

}
