package org.apache.nutch.parse.selector;

import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.avro.util.Utf8;
import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.parse.HTMLMetaTags;
import org.apache.nutch.parse.Parse;
import org.apache.nutch.parse.ParseFilter;
import org.apache.nutch.storage.WebPage;
import org.json.simple.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DocumentFragment;

public class ParseSelector implements ParseFilter {

  private Configuration conf;
  private Logger LOG = LoggerFactory.getLogger(ParseSelector.class);

  public ParseSelector() {
  }

  public Parse filter(String url, WebPage page, Parse parse, HTMLMetaTags metaTags,
      DocumentFragment doc) {
    try {
      InputStream ppInputStream = null;
      ppInputStream = conf.getConfResourceAsInputStream(conf.get("parse.selector.file"));
      DataInputStream dataInputStream = new DataInputStream(ppInputStream);
      String xml = "";
      while (dataInputStream.available() != 0) {
        xml = xml + dataInputStream.readLine();
      }
      BootstrapSelector bootstrapSelector = new BootstrapSelector();
      ArrayList<EntityParser> listItemConf = new ArrayList();
      listItemConf = bootstrapSelector.load(url, xml);
      String html = new String(page.getContent().array(), Charset.forName("UTF-8"));
      String text = "";
      for (int i = 0; i < listItemConf.size(); i++) {
        if (!listItemConf.get(i).isDeffaulContent()) {
          text = parseSelector(html, listItemConf.get(i).getSelector(),
              listItemConf.get(i).getType());
          parse.setText(text);
        }
        page.getMetadata().put(new Utf8(listItemConf.get(i).getLabel()), ByteBuffer.wrap(
            parseSelector(html, listItemConf.get(i).getSelector(), listItemConf.get(i).getType())
                .getBytes()));
      }
    } catch (Exception ex) {
      LOG.error("Plugin parse-selector error : " + ex.toString());
    }
    return parse;
  }

  private String parseSelector(String html, String[] selectors, String type) {
    String value = "";
    Document document = Jsoup.parse(html);

    ArrayList<String> imgs = new ArrayList();
    for (int i = 0; i < selectors.length; i++) {
      Elements element = document.select(selectors[i]);
      if (type.equals("text")) {
        value = element.text();
      }
      if (type.equals("img")) {
        Elements elements_img = element.select("img");
        for (int j = 0; j < elements_img.size(); j++) {
          String img = elements_img.get(j).attr("src");
          if (img.indexOf(".gif") < 0) {
            imgs.add(img);
          }
        }
        JSONArray arrJson_imgs = new JSONArray();
        arrJson_imgs.addAll(imgs);
        value = arrJson_imgs.toJSONString();
      }
      if (!value.equals("")) {
        break;
      }
    }
    value = value.replace(">", "");
    return value;
  }


  public void setConf(Configuration configuration) {
    conf = configuration;
  }

  public Configuration getConf() {
    return conf;
  }

  public Collection<WebPage.Field> getFields() {
    return null;
  }
}
