package technology.sola.engine.examples.browser;

import technology.sola.engine.platform.browser.transpile.SolaBrowserFileBuilder;

public class TranspileMain {
  public static void main(String[] args) {
    SolaBrowserFileBuilder solaBrowserFileBuilder = new SolaBrowserFileBuilder("build");

    solaBrowserFileBuilder.transpileSolaJar(
      "build/libs/browser-0.0.1.jar",
      BrowserMain.class.getName()
    );

    solaBrowserFileBuilder.createIndexHtml();
  }
}
