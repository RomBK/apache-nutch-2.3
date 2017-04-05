package org.apache.nutch.indexer.selector;

public class EntityIndexer {

  private String domain;
  private String label;
  private String type;

  public EntityIndexer() {
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
