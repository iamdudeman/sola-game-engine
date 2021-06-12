package technology.sola.engine.tools;

public class ToolCommand {
  private final String toolName;
  private final String[] toolArgs;

  public ToolCommand(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException("Must have a tool name");
    }

    toolName = args[0];
    toolArgs = new String[args.length - 1];

    System.arraycopy(args, 1, toolArgs, 0, args.length - 1);
  }

  public String getToolName() {
    return toolName;
  }

  public String[] getToolArgs() {
    return toolArgs;
  }
}
