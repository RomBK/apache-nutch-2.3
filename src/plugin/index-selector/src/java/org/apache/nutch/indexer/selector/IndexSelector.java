package org.apache.nutch.indexer.selector;

import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.avro.util.Utf8;
import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.indexer.IndexingException;
import org.apache.nutch.indexer.IndexingFilter;
import org.apache.nutch.indexer.NutchDocument;
import org.apache.nutch.storage.WebPage;
import org.apache.nutch.storage.WebPage.Field;
import org.apache.nutch.util.Bytes;

/**
 * Created by rombk on 06/04/2017.
 */
public class IndexSelector implements IndexingFilter {

  private Configuration conf;
  private static Map<Utf8, String> parseFieldnames;
  private static final String PARSE_CONF_PROPERTY = "index.metadata";
  private static final String INDEX_PREFIX = "meta_";
  private static final String PARSE_META_PREFIX = "meta_";

  public NutchDocument filter(NutchDocument doc, String url, WebPage page)
      throws IndexingException {

    // just in case
    if (doc == null) {
      return doc;
    }

    try {
      InputStream ppInputStream = conf
          .getConfResourceAsInputStream(conf.get("index.selector.file"));
      DataInputStream dataInputStream = new DataInputStream(ppInputStream);
      String xml = "";
      while (dataInputStream.available() != 0) {
        xml = xml + dataInputStream.readLine();
      }
      BootstrapIndexer bootstrapIndexer = new BootstrapIndexer();
      ArrayList<EntityIndexer> listItemConf = new ArrayList();
      listItemConf = bootstrapIndexer.load(url, xml);

      // add the fields from parsemd
      for (Entry<Utf8, String> metatag : parseFieldnames.entrySet()) {
        for (EntityIndexer entity : listItemConf) {
          if (entity.getType().equals(metatag.getKey())) {
            String value = Bytes.toString(page.getMetadata().get(metatag.getValue()).array());
            doc.add(entity.getLabel(), value);
          }
        }
      }
    } catch (Exception ex) {
    }

    return doc;
  }

  public void setConf(Configuration conf) {
    this.conf = conf;
    String[] metatags = conf.getStrings(PARSE_CONF_PROPERTY);
    parseFieldnames = new TreeMap<Utf8, String>();
    for (int i = 0; i < metatags.length; i++) {
      parseFieldnames.put(
          new Utf8(PARSE_META_PREFIX + metatags[i].toLowerCase(Locale.ROOT)),
          INDEX_PREFIX + metatags[i]);
    }
    // TODO check conflict between field names e.g. could have same label
    // from different sources
  }

  public Configuration getConf() {
    return this.conf;
  }

  @Override
  public Collection<Field> getFields() {
    return null;
  }
}
