package com.carspark.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GetAllBody {
    @JsonIgnore private Integer MAX_RESULT=30;


    public Map<String, Boolean> prj;
    public Map<String, Boolean> sort;
    public String filter;
    public Integer skip=0;
    public Integer limit=MAX_RESULT;

    public GetAllBody() {}


    @JsonIgnore
    public List<String> getPrjFields() {
        List<String> ret=new LinkedList<>();
        for (Map.Entry<String, Boolean> entry: this.prj.entrySet()) {
            if(entry.getValue()) {
                ret.add(entry.getKey());
            }
        }
        return ret;
    }

    @JsonIgnore
    public void setSkipAndLimit(Integer skip, Integer limit) {
        if(skip==null) {
            this.skip=0;
        }
        else {
            this.skip=skip;
        }

        if(limit==null) {
            limit=MAX_RESULT;
        }
        if(limit>MAX_RESULT) {
            this.limit=MAX_RESULT;
        }
        else {
            this.limit=limit;
        }
    }

    /*
    public GetAllBody(int maxResult) {
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
    }

     */
}
