package com.carspark.restapi.mapper;

import java.util.List;

public class MappingRelation {

  private String table;
  private String id;
  private List<MappingLink> links;

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

  public List<MappingLink> getLinks() {
    return links;
  }

  public void setLinks(List<MappingLink> links) {
    this.links = links;
  }

}
