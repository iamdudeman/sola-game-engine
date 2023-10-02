package technology.sola.engine.platform.javafx.assets;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class JavaFxPathUtils {
  public static URL asUrl(String path) throws MalformedURLException {
    File file = new File(path);

    return file.exists() ? file.toURI().toURL() : ClassLoader.getSystemClassLoader().getResource(path);
  }

  public static String readContents(String path) throws IOException {
    Scanner scanner = new Scanner(JavaFxPathUtils.asUrl(path).openStream()).useDelimiter("\\A");
    String contents = scanner.next();

    scanner.close();

    return contents;
  }

  public static String getExtension(String path) {
    return path.substring(path.lastIndexOf('.')).toLowerCase();
  }

  public static String getParentPath(String path) {
    return path.substring(0, path.lastIndexOf("/"));
  }

  private JavaFxPathUtils() {
  }
}
