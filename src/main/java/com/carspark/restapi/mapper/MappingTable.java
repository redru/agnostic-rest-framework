package com.carspark.restapi.mapper;

public class MappingTable {

  private String table;
  private String id;
  private MappingRelation relation;

  public String getTable() {
    return table;
  }

  public void setTable(String table) {
    this.table = table;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public MappingRelation getRelation() {
    return relation;
  }

  public void setRelation(MappingRelation relation) {
    this.relation = relation;
  }

}
