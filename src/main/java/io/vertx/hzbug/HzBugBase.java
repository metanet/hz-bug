package io.vertx.hzbug;


import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class HzBugBase {

  protected Config loadConfigFromClasspath() {
    Config cfg = null;
    try (InputStream is = getClass().getClassLoader().getResourceAsStream("default-cluster.xml");
         InputStream bis = new BufferedInputStream(is)) {
      cfg = new XmlConfigBuilder(bis).build();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return cfg;
  }
}
