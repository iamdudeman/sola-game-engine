package technology.sola.engine.tooling;

import technology.sola.engine.tooling.font.FontListTool;
import technology.sola.engine.tooling.font.FontRasterizerTool;

public class ToolingMain {
  public static void main(String[] args) {
    new ToolExecutor(
      new Tool[]{
        new FontRasterizerTool(),
        new FontListTool(),
      }
    ).execute(args);
  }
}
