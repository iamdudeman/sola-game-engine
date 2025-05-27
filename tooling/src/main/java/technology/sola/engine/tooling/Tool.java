package technology.sola.engine.tooling;

import org.jspecify.annotations.NullMarked;

/**
 * Tools can be executed from the command line to provide some useful functionality for building games.
 */
@NullMarked
public interface Tool {
  /**
   * @return the name of the Tool
   */
  String getName();

  /**
   * Called by the command line to execute the tool
   *
   * @param args the commandline arguments
   * @return the output from the executed tool
   */
  String execute(String... args);

  /**
   * @return the help text for the Tool
   */
  String getHelp();
}
