package technology.sola.engine.examples.browser;

import technology.sola.engine.platform.browser.tools.SolaBrowserFileBuilder;

public class GenerateBrowserFilesMain {
  public static void main(String[] args) {
    String buildDirectory = args.length > 0 ? args[0] : "examples/browser/build";
    String jarFile = args.length > 1 ? args[1] : "browser-0.0.1.jar";

    System.out.println("Generating html and js for BrowserMain using [build/libs/" + jarFile + "]");
    System.out.println("Output at:");

    SolaBrowserFileBuilder solaBrowserFileBuilder = new SolaBrowserFileBuilder(buildDirectory);

    solaBrowserFileBuilder.transpileSolaJar(
      "build/libs/" + jarFile,
      BrowserMain.class.getName(),
      false
    );

    solaBrowserFileBuilder.createIndexHtmlWithOverlay();
  }
}
