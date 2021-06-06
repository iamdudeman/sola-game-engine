package technology.sola.engine.tools;


public class Main {
  public static void main(String[] args) {
    String[] manualTestArgs = new String[] {
      "font",
    };

//    ToolCommand toolCommand = new ToolCommand(args);
    ToolCommand toolCommand = new ToolCommand(manualTestArgs);

    Tool.execute(toolCommand);
  }
}
