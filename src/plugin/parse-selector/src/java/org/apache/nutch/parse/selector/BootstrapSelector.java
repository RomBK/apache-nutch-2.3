package org.apache.nutch.parse.selector;

import java.io.StringReader;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class BootstrapSelector {

  public BootstrapSelector() {
  }

  public ArrayList<EntityParser> load(String url, String pathConfig) {
    ArrayList<EntityParser> listItemConf = new ArrayList();
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      InputSource is = new InputSource(new StringReader(pathConfig));
      Document doc = dBuilder.parse(is);
      doc.getDocumentElement().normalize();
      NodeList nLists = doc.getElementsByTagName("parse");

      for (int temp = 0; temp < nLists.getLength(); temp++) {
        Node nNode = nLists.item(temp);
        if (nNode.getNodeType() == 1) {
          Element eElement = (Element) nNode;
          String domain = eElement.getAttribute("domain");
          if (url.indexOf(domain) > 0) {
            EntityParser entitySelector = new EntityParser();
            entitySelector.setDomain(domain);
            entitySelector
                .setLabel(eElement.getElementsByTagName("label").item(0).getTextContent());
            entitySelector.setType(eElement.getElementsByTagName("type").item(0).getTextContent());
            if (entitySelector.getLabel().equals("content")) {
              boolean defaultContent = Boolean
                  .parseBoolean(eElement.getAttribute("default_content"));
              entitySelector.setDeffaulContent(defaultContent);
            }
            String selector = eElement.getElementsByTagName("selector").item(0).getTextContent();
            String[] list_selectors = selector.split("\\|", -1);
            entitySelector.setSelector(list_selectors);
            listItemConf.add(entitySelector);
          }
        }
      }
    } catch (Exception ex) {
    }
    return listItemConf;
  }
}
