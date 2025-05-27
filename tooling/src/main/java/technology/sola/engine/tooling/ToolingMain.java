package technology.sola.engine.tooling;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.tooling.font.FontListTool;
import technology.sola.engine.tooling.font.FontRasterizerTool;

/**
 * Runs the {@link ToolExecutor} for the set of available {@link Tool}s.
 */
@NullMarked
public class ToolingMain {
  /**
   * Entry point for running {@link Tool}s on the command line.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    new ToolExecutor(
      new Tool[]{
        new FontRasterizerTool(),
        new FontListTool(),
      }
    ).execute(args);
  }
}
