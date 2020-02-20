package com.carspark.restapi.sql;

import java.util.*;

public class Collector {
    private TableEntity tableEntity;
    public Map<Object, Map<String, Object>> results=new HashMap<>();

    public Collector(TableEntity tableEntity) {
        this.tableEntity=tableEntity;
    }


    public void setResults(List<Map<String, Object>> tableResult) {
        for(Map result : tableResult) {
            Object id=result.get(this.tableEntity.getName()+"."+this.tableEntity.getId());
            this.results.put(id, result);
        }
    }

    public void setRelationResults(String name, List<Map<String, Object>> results) {

        for(Map<String, Object> result : results) {
            Object id=result.get("ID");
            Object ref=result.get("REF");
            Map<String, Object> item=this.results.get(ref);
            if(item!=null) {
                List<Object> list= (List<Object>) item.get(name);
                if(list==null) {
                    list=new ArrayList<>();
                    item.put(name, list);
                }
                list.add(id);
            }
        }
    }
}
