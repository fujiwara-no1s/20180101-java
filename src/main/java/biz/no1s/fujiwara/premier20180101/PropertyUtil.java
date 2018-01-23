package biz.no1s.fujiwara.premier20180101;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
  private static final String INIT_FILE_PATH = "setting.properties";
  private static final Properties properties;

  static {
    properties = new Properties();
    try {
      ClassLoader classLoader = PropertyUtil.class.getClassLoader();
      File file = new File(classLoader.getResource(INIT_FILE_PATH).getFile());
      if( !file.exists()) {
        file = new File(INIT_FILE_PATH);
      }
      InputStream fileStream = new FileInputStream(file);
      properties.load(fileStream);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("fail to load setting.propertiess");
    }
  }

  public static String getProperty(final String key) {
    return properties.getProperty(key, "");
  }
}
