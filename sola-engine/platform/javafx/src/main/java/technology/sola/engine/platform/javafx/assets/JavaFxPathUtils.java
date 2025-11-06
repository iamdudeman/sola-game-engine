package technology.sola.engine.platform.javafx.assets;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * General path utilities for JavaFX.
 */
@NullMarked
public class JavaFxPathUtils {
  /**
   * Returns a path as a {@link URL}.
   *
   * @param path the path
   * @return the path as a URL
   * @throws MalformedURLException if the path is not a valid URL
   */
  @Nullable
  public static URL asUrl(String path) throws MalformedURLException {
    File file = new File(path);

    return file.exists() ? file.toURI().toURL() : ClassLoader.getSystemClassLoader().getResource(path);
  }

  /**
   * Returns the contents of a file located by the path as a string.
   *
   * @param path the path to load file contents for
   * @return the file contents as a string
   * @throws IOException if an IO error happens
   */
  public static String readContents(String path) throws IOException {
    Scanner scanner = new Scanner(JavaFxPathUtils.asUrl(path).openStream()).useDelimiter("\\A");
    String contents = scanner.next();

    scanner.close();

    return contents;
  }

  private JavaFxPathUtils() {
  }
}
