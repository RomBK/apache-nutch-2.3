package org.apache.nutch.parse.selector;

public class EntityParser
{
  private String domain;
  private String label;
  private String[] selector;
  private String type;
  
  public EntityParser() {}
  
  private boolean deffaulContent = true;
  
  public String getDomain() {
    return domain;
  }
  
  public void setDomain(String domain) {
    this.domain = domain;
  }
  
  public String[] getSelector() {
    return selector;
  }
  
  public void setSelector(String[] selector) {
    this.selector = selector;
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
  
  public boolean isDeffaulContent() {
    return deffaulContent;
  }
  
  public void setDeffaulContent(boolean deffaulContent) {
    this.deffaulContent = deffaulContent;
  }
}
