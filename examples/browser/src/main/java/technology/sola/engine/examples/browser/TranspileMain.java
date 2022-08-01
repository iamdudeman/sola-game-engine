package technology.sola.engine.examples.browser;

import technology.sola.engine.platform.browser.transpile.SolaJsTranspiler;

public class TranspileMain {
  public static void main(String[] args) {
    new SolaJsTranspiler().transpileJar(
      "build/libs/browser-0.0.1.jar",
      "build",
      "sola.js",
      BrowserMain.class.getName()
    );
  }
}
