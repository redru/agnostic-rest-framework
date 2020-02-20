package com.carspark.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetAllBody {

    @JsonIgnore private Integer MAX_RESULT = 30;

    public Map<String, Boolean> prj;
    public Map<String, Boolean> sort;
    public String filter;
    public Integer skip = 0;
    public Integer limit = MAX_RESULT;

    @JsonIgnore
    public List<String> getPrjFields() {
        return this.prj.entrySet().stream()
            .filter(Map.Entry::getValue)
            .map(Map.Entry::getKey)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @JsonIgnore
    public void setSkipAndLimit(Integer skip, Integer limit) {
        this.skip = (skip == null ? 0 : skip);

        if (limit == null) {
            this.limit = MAX_RESULT;
        } else {
            this.limit = (limit > MAX_RESULT ? MAX_RESULT : limit);
        }
    }

    /* public GetAllBody(int maxResult) {
        this.limit=maxResult;
        if(this.limit>MAX_RESULT) {
            this.limit=MAX_RESULT;
        }
    }

    @JsonIgnore
    public void setLimit(Integer limit) {
        this.limit=limit;
        if(this.limit==null) {
            this.limit=MAX_RESULT;
            return;
        }
        if(this.limit>MAX_RESULT) {
            this.limit=MAX_RESULT;
            return;
        }
    } */

}
