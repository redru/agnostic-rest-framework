package com.carspark.restapi.sql;

import java.util.HashMap;
import java.util.Map;

public class Schemas {

    private Map<String, TableEntity> tables=new HashMap<>();



    public void register(TableEntity tableEntity) {
        this.tables.put(tableEntity.getName(), tableEntity);
    }

    public TableEntity getTable(String name) {
        return this.tables.get(name);
    }


}
