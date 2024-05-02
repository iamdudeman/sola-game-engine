package technology.sola.engine.examples.browser;

import technology.sola.engine.platform.browser.tools.SolaBrowserFileBuilder;

/**
 * Uses {@link SolaBrowserFileBuilder} to generate HTML and JS from {@link BrowserMain}.
 */
public class GenerateBrowserFilesMain {
  public static final boolean IS_PROD_BUILD = false;

  /**
   * Entry point for program that starts Browser example transpiling.
   *
   * @param args command line args
   */
  public static void main(String[] args) {
    String buildDirectory = getCommandLineArg(args, 0, "examples/browser/build");
    String jarFile = getCommandLineArg(args, 1, "browser-0.0.1.jar");
    String buildTypeString = IS_PROD_BUILD ? "prod" : "dev";

    System.out.println("Generating " + buildTypeString + " html and js for BrowserMain using [build/libs/" + jarFile + "]");

    SolaBrowserFileBuilder solaBrowserFileBuilder = new SolaBrowserFileBuilder(buildDirectory);

    solaBrowserFileBuilder.transpileSolaJar(
      "build/libs/" + jarFile,
      BrowserMain.class.getName(),
      IS_PROD_BUILD
    );

    solaBrowserFileBuilder.createIndexHtmlWithOverlay();
    System.out.println("Successfully generated html and js");
  }

  private static String getCommandLineArg(String[] args, int index, String defaultValue) {
    return args.length > index ? args[index] : defaultValue;
  }
}
