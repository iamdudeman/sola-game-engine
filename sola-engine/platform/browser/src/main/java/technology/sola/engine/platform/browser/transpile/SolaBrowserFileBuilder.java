package technology.sola.engine.platform.browser.transpile;

import org.teavm.tooling.TeaVMTargetType;
import org.teavm.tooling.builder.BuildException;
import org.teavm.tooling.builder.BuildResult;
import org.teavm.tooling.builder.InProcessBuildStrategy;
import org.teavm.vm.TeaVMOptimizationLevel;
import technology.sola.engine.platform.browser.javascript.JsCanvasUtils;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Files;

public class SolaBrowserFileBuilder {
  private static final String OUTPUT_FILE_JS = "sola.js";
  private static final String OUTPUT_FILE_HTML = "index.html";
  private final String buildDirectory;

  public SolaBrowserFileBuilder(String buildDirectory) {
    this.buildDirectory = buildDirectory;
  }

  public void transpileSolaJar(String jarPath, String mainClass) {
    transpileSolaJar(jarPath, mainClass, true);
  }

  /**
   * Note: supports up to Java 17
   * @param jarPath
   * @param mainClass
   * @param obfuscate
   */
  public void transpileSolaJar(String jarPath, String mainClass, boolean obfuscate) {
    InProcessBuildStrategy buildStrategy = new InProcessBuildStrategy(URLClassLoader::new);

    buildStrategy.init();

    // Configurable
    buildStrategy.setMainClass(mainClass);
    buildStrategy.addSourcesJar(jarPath);
    buildStrategy.setObfuscated(obfuscate);


    // Non-configurable
    buildStrategy.setEntryPointName("main");
    buildStrategy.setTargetFileName(OUTPUT_FILE_JS);
    buildStrategy.setTargetDirectory(buildDirectory);
    buildStrategy.setTargetType(TeaVMTargetType.JAVASCRIPT);

    buildStrategy.setStrict(false);
    buildStrategy.setDebugInformationGenerated(false);
    buildStrategy.setSourceMapsFileGenerated(false);
    buildStrategy.setSourceFilesCopied(false);
    buildStrategy.setIncremental(false);
    buildStrategy.setOptimizationLevel(TeaVMOptimizationLevel.SIMPLE);
    buildStrategy.setCacheDirectory("build/teavm-cache");
    buildStrategy.setMinHeapSize(4);
    buildStrategy.setMaxHeapSize(128);
    buildStrategy.setShortFileNames(false);
    buildStrategy.setMaxTopLevelNames(10000);
    buildStrategy.setLongjmpSupported(true);

    BuildResult buildResult;
    try {
      buildResult = buildStrategy.build();
    } catch (BuildException ex) {
      throw new RuntimeException(ex);
    }

    buildResult.getGeneratedFiles().forEach(System.out::println);

    if (!buildResult.getProblems().getSevereProblems().isEmpty()) {
      buildResult.getProblems().getSevereProblems().forEach(problem -> {
        System.out.println(problem.getText());
        System.out.println(problem.getLocation().getSourceLocation());
      });

      throw new RuntimeException("Build failed");
    }
  }

  public void createIndexHtml() {
    String template = """
      <html>
      <head>
          <script type="text/javascript" charset="utf-8" src="%s"></script>
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
}
