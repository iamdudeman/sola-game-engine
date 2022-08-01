package technology.sola.engine.platform.browser.transpile;

import org.teavm.tooling.TeaVMTargetType;
import org.teavm.tooling.builder.BuildException;
import org.teavm.tooling.builder.BuildResult;
import org.teavm.tooling.builder.InProcessBuildStrategy;
import org.teavm.vm.TeaVMOptimizationLevel;

import java.net.URLClassLoader;

// TODO might be good to also generate html file

public class SolaJsTranspiler {
  /**
   * Note: only supports Java 8 at the moment
   * @param jarPath
   * @param outputDir
   * @param outputFile
   * @param mainClass
   */
  public void transpileJar(String jarPath, String outputDir, String outputFile, String mainClass) {
    InProcessBuildStrategy buildStrategy = new InProcessBuildStrategy(URLClassLoader::new);

    buildStrategy.init();

    // Configurable
    buildStrategy.setMainClass(mainClass);
    buildStrategy.addSourcesJar(jarPath);
    buildStrategy.setTargetFileName(outputFile);
    buildStrategy.setTargetDirectory(outputDir);


    // Should be configurable
    buildStrategy.setObfuscated(false);



    // Non-configurable
    buildStrategy.setEntryPointName("main");
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

    BuildResult buildResult = null;
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
}
