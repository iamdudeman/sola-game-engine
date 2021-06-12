package technology.sola.engine.tools;

import technology.sola.engine.tools.font.FontRasterizerExecutable;

public enum Tool {
  FONT("font", new FontRasterizerExecutable()),
  ;

  public static void execute(ToolCommand toolCommand) {
    for (Tool tool : values()) {
      if (tool.toolName.equals(toolCommand.getToolName())) {
        tool.toolExecutable.execute(toolCommand.getToolArgs());
        return;
      }
    }

    throw new IllegalArgumentException("Tool with name " + toolCommand.getToolName() + " not found");
  }

  private final String toolName;
  private final ToolExecutable toolExecutable;

  Tool(String toolName, ToolExecutable toolExecutable) {
    this.toolName = toolName;
    this.toolExecutable = toolExecutable;
  }
}
