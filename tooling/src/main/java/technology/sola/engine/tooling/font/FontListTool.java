package technology.sola.engine.tooling.font;

import technology.sola.engine.tooling.Tool;

import java.awt.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@link Tool} that lists available font families that can be used in the {@link FontRasterizerTool}.
 */
public class FontListTool implements Tool {
  @Override
  public String getName() {
    return "fontList";
  }

  @Override
  public String execute(String... args) {
    Set<String> fontFamilies = getLocalFontFamilies();
    long limit = args.length > 0 ? Long.parseLong(args[0]) : fontFamilies.size();
    long offset = args.length > 1 ? limit * Long.parseLong(args[1]) : 0;

    return fontFamilies.stream().skip(offset).limit(limit).collect(Collectors.joining("\n"));
  }

  @Override
  public String getHelp() {
    return """
      arg1 - page limit
      arg2 - page offset
      """;
  }

  /**
   * @return a Set of available font families
   */
  public java.util.Set<String> getLocalFontFamilies() {
    java.util.Set<String> fontFamilies = new HashSet<>();
    GraphicsEnvironment ge = GraphicsEnvironment
      .getLocalGraphicsEnvironment();

    Font[] allFonts = ge.getAllFonts();

    for (Font font : allFonts) {
      fontFamilies.add(font.getFamily());
    }

    return fontFamilies.stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
  }
}
