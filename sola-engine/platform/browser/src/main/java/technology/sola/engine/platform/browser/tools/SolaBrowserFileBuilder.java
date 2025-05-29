package technology.sola.engine.platform.browser.tools;

import org.jspecify.annotations.NullMarked;
import org.teavm.backend.javascript.JSModuleType;
import org.teavm.tooling.TeaVMTargetType;
import org.teavm.tooling.builder.BuildException;
import org.teavm.tooling.builder.BuildResult;
import org.teavm.tooling.builder.InProcessBuildStrategy;
import org.teavm.vm.TeaVMOptimizationLevel;
import technology.sola.engine.platform.browser.javascript.JsCanvasUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * SolaBrowserFileBuilder is a wrapper around {@link org.teavm.vm.TeaVM} for generating the "sola.js" bundle for a
 * {@link technology.sola.engine.core.Sola}.
 * <p>
 * Example usage:
 * <pre>
 * SolaBrowserFileBuilder solaBrowserFileBuilder = new SolaBrowserFileBuilder("browser/build");
 *
 * solaBrowserFileBuilder.transpileSolaJar(
 *   "build/libs/browser-1.0.0.jar",
 *   BrowserMain.class.getName(),
 * );
 *
 * solaBrowserFileBuilder.createIndexHtmlWithOverlay();
 * </pre>
 */
@NullMarked
public class SolaBrowserFileBuilder {
  private static final String OUTPUT_FILE_JS = "sola.js";
  private static final String OUTPUT_FILE_HTML = "index.html";
  private final String buildDirectory;

  /**
   * Creates a SolaBrowserFileBuilder with targeted build output directory.
   *
   * @param buildDirectory the build output directory
   */
  public SolaBrowserFileBuilder(String buildDirectory) {
    this.buildDirectory = buildDirectory;
  }

  /**
   * Transpiles a jar file into an obfuscated sola.js bundle.
   * <strong>Note: supports up to Java 17</strong>
   *
   * @param jarPath   path to the jar file
   * @param mainClass the main class
   */
  public void transpileSolaJar(String jarPath, String mainClass) {
    transpileSolaJar(jarPath, mainClass, true);
  }

  /**
   * Transpiles a jar file into a sola.js bundle.
   * <strong>Note: supports up to Java 17</strong>
   *
   * @param jarPath   path to the jar file
   * @param mainClass the main class
   * @param obfuscate whether to obfuscate the js bundle or not
   */
  public void transpileSolaJar(String jarPath, String mainClass, boolean obfuscate) {
    InProcessBuildStrategy buildStrategy = new InProcessBuildStrategy();

    buildStrategy.init();

    // Configurable
    buildStrategy.setMainClass(mainClass);
    buildStrategy.addSourcesJar(jarPath);
    buildStrategy.setOptimizationLevel(obfuscate ? TeaVMOptimizationLevel.ADVANCED : TeaVMOptimizationLevel.SIMPLE);
    buildStrategy.setObfuscated(obfuscate);

    // Non-configurable
    buildStrategy.setEntryPointName("main");
    buildStrategy.setTargetFileName(OUTPUT_FILE_JS);
    buildStrategy.setTargetDirectory(buildDirectory);
    buildStrategy.setTargetType(TeaVMTargetType.JAVASCRIPT);
    buildStrategy.setJsModuleType(JSModuleType.NONE);

    buildStrategy.setStrict(false);
    buildStrategy.setDebugInformationGenerated(false);
    buildStrategy.setSourceMapsFileGenerated(false);
    buildStrategy.setSourceFilesCopied(false);
    buildStrategy.setIncremental(false);
    buildStrategy.setCacheDirectory("build/teavm-cache");
    buildStrategy.setShortFileNames(false);

    BuildResult buildResult;
    try {
      buildResult = buildStrategy.build();
    } catch (BuildException ex) {
      throw new RuntimeException(ex);
    }

    if (!buildResult.getProblems().getSevereProblems().isEmpty()) {
      buildResult.getProblems().getSevereProblems().forEach(problem -> {
        if (problem.getParams() != null && problem.getParams().length > 0) {
          System.out.print(problem.getText() + " ");
          System.out.println(Arrays.toString(problem.getParams()));
        } else {
          System.out.println(problem.getText());
        }

        System.out.println(problem.getLocation().getSourceLocation());
      });

      throw new RuntimeException("Build failed");
    }
  }

  /**
   * Creates an index.html file that immediately executes the {@link technology.sola.engine.core.Sola} when the page
   * loads.
   */
  public void createIndexHtml() {
    String template = """
      <html>
      <head>
          <link rel="icon" type="image/x-icon" href="/assets/icon.ico">
          <script type="text/javascript" charset="utf-8" src="%s"></script>
          <style>
            body {
              margin: 0;
              touch-action: manipulation;
            }

            canvas {
              outline: none;
            }
          </style>
          <script>
              window.start = function () {
                  main();
              };
          </script>
      </head>
      <body onload="start()">
        <div id="%s"></div>
      </body>
      </html>
      """;

    String html = template.formatted(OUTPUT_FILE_JS, JsCanvasUtils.ID_SOLA_ANCHOR);

    try {
      Files.createDirectories(new File(buildDirectory).toPath());
      Files.writeString(new File(buildDirectory, OUTPUT_FILE_HTML).toPath(), html);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Creates an index.html file with an overlay to begin executing the {@link technology.sola.engine.core.Sola} when the
   * "run game" button is clicked.
   */
  public void createIndexHtmlWithOverlay() {
    String template = """
      <html>
      <head>
        <link rel="icon" type="image/x-icon" href="/assets/icon.ico">
        <script type="text/javascript" charset="utf-8" src="%s"></script>
        <style>
          body {
            margin: 0;
            touch-action: manipulation;
          }

          canvas {
            outline: none;
          }

          #overlay {
            align-items: center;
            background: gray;
            display: flex;
            justify-content: center;
            height: 100vh;
            width: 100vw;
          }

          #overlay button {
            border-radius: 8px;
            font-size: 24px;
            padding: 16px;
          }

          #overlay button:hover {
            opacity: 80%%;
          }
        </style>
      </head>
      <script>
        function runGame() {
          document.getElementById("overlay").remove();
          main();
        }
      </script>
      <body>
        <div id="overlay">
          <button onclick="runGame()">Run game</button>
        </div>
        <div id="%s"></div>
      </body>
      </html>
      """;

    String html = template.formatted(OUTPUT_FILE_JS, JsCanvasUtils.ID_SOLA_ANCHOR);

    try {
      Files.createDirectories(new File(buildDirectory).toPath());
      Files.writeString(new File(buildDirectory, OUTPUT_FILE_HTML).toPath(), html);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
