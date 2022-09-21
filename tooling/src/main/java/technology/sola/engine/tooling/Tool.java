package technology.sola.engine.tooling;

public interface Tool {
  String getCommand();

  /**
   * Called by the command line to execute the tool
   * @param args the commandline arguments
   * @return the output from the executed tool
   */
  String execute(String[] args);

  String getHelp();
}
