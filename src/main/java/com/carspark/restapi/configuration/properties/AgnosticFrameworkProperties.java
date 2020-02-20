package com.carspark.restapi.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "agnostic-framework")
public class AgnosticFrameworkProperties {

  private String file = "mappings.json";

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

}
