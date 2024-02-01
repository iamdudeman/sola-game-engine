package technology.sola.engine.tooling;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 * ToolExecutor parses command line arguments and executes a {@link Tool} based on the input.
 */
public class ToolExecutor {
  private final Tool[] tools;

  /**
   * Creates a ToolExecutor for a set of {@link Tool}s.
   *
   * @param tools the {@code Tool}s available
   */
  public ToolExecutor(Tool[] tools) {
    this.tools = Arrays.stream(tools).sorted(Comparator.comparing(Tool::getName)).toArray(Tool[]::new);
  }

  /**
   * Runs a tool based on the command line arguments passed in or offers help if tool is not found.
   *
   * @param args command line arguments
   */
  public void execute(String... args) {
    String[] commandArgs = args;
    boolean isDone = true;
    boolean hasSeenPrompt = false;

    do {
      // If command arguments are initially empty then start "interactive mode"
      if (commandArgs == null || commandArgs.length == 0) {
        Scanner scanner = new Scanner(System.in);

        if (!hasSeenPrompt) {
          System.out.println("Must provide at least one argument for the tool to run. [help] if you need it. [quit] to stop.");
          hasSeenPrompt = true;
        }

        System.out.print("Command: ");
        commandArgs = scanner.nextLine().split(" ");
        isDone = false;
      }

      String toolName = commandArgs[0];
      String[] toolArgs = Arrays.copyOfRange(commandArgs, 1, commandArgs.length);

      if (toolName.equalsIgnoreCase("quit")) {
        isDone = true;
      } else if (toolName.equalsIgnoreCase("help")) {
        Arrays.stream(tools)
          .forEach(tool -> {
            System.out.println("----" + tool.getName());
            System.out.println(tool.getHelp().trim());
            System.out.println();
          });
      } else {
        Arrays.stream(tools)
          .filter(tool -> tool.getName().equalsIgnoreCase(toolName))
          .findFirst()
          .ifPresentOrElse(
            tool -> System.out.println(tool.execute(toolArgs)),
            () -> System.err.println("No tool with name [" + toolName + "]")
          );
        System.out.println();
      }

      commandArgs = null;
    } while (!isDone);
  }
}
