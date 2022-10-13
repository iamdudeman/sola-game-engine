package technology.sola.engine.tooling;

import java.util.Arrays;
import java.util.Comparator;

public class ToolExecutor {
  private final Tool[] tools;

  public ToolExecutor(Tool[] tools) {
    this.tools = tools;
  }

  public void execute(String... args) {
    if (args.length == 0) {
      throw new RuntimeException("Must provide at least one argument for the tool to run. [help] if you need it");
    }

    String toolName = args[0];
    String[] toolArgs = Arrays.copyOfRange(args, 1, args.length);

    if (toolName.equalsIgnoreCase("help")) {
      Arrays.stream(tools)
        .sorted(Comparator.comparing(Tool::getName))
        .forEach(tool -> {
          System.out.println("----" + tool.getName());
          System.out.println(tool.getHelp().trim());
          System.out.println();
        });
    } else {
      Arrays.stream(tools)
        .sorted(Comparator.comparing(Tool::getName))
        .filter(tool -> tool.getName().equalsIgnoreCase(toolName))
        .findFirst()
        .ifPresentOrElse(tool -> {
          System.out.println(tool.execute(toolArgs));
        }, () -> {
          System.out.println("No tool with name [" + toolName + "]");
        });
    }
  }
}
