package technology.sola.engine.examples.browser;

import technology.sola.engine.platform.browser.tools.SolaBrowserFileBuilder;

/**
 * Uses {@link SolaBrowserFileBuilder} to generate HTML and JS from {@link BrowserMain}.
 */
public class GenerateBrowserFilesMain {
  /**
   * Entry point for program that starts Browser example transpiling.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    String buildDirectory = getCommandLineArg(args, 0, "examples/browser/build");
    String jarFile = getCommandLineArg(args, 1, "browser-0.0.1.jar");
    boolean isDebug = getCommandLineArg(args, 2, "").equals("debug");

    System.out.println("Generating" + (isDebug ? " debug" : "") + " html and js for BrowserMain using [build/libs/" + jarFile + "]");

    SolaBrowserFileBuilder solaBrowserFileBuilder = new SolaBrowserFileBuilder(buildDirectory);

    solaBrowserFileBuilder.transpileSolaJar(
      "build/libs/" + jarFile,
      BrowserMain.class.getName(),
      !isDebug
    );

    solaBrowserFileBuilder.createIndexHtmlWithOverlay();
    System.out.println("Successfully generated html and js");
  }

  private static String getCommandLineArg(String[] args, int index, String defaultValue) {
    return args.length > index ? args[index] : defaultValue;
  }
}
