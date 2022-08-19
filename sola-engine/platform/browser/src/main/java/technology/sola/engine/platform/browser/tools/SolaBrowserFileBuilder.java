package technology.sola.engine.platform.browser.tools;

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
import java.nio.file.StandardOpenOption;

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
    } else {
      try {
        Files.write(new File(buildDirectory, OUTPUT_FILE_JS).toPath(), "main();".getBytes(), StandardOpenOption.APPEND);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void createIndexHtml() {
    String template = """
      <html>
      <head>
          <script type="text/javascript" charset="utf-8" src="%s"></script>
          <style>
            body {
              margin: 0;
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

  public void createIndexPocHtml() {
    String template = """
      <!DOCTYPE html>
      <html lang="en">
      <head>
        <meta charset="UTF-8">
        <title>web worker test</title>
        <style>
          body {
            margin: 0;
          }

          canvas {
            outline: none;
          }
        </style>
      </head>
      <body>
      <canvas tabindex="1" id="sola-canvas" width="100" height="100"></canvas>
      <script>
      var solaCanvas = document.getElementById("sola-canvas");
      var solaContext2d = solaCanvas.getContext("2d");
      var solaWorker = new Worker("%s");

      solaWorker.onmessage = function (event) {
        console.log("main received", event.data);

        var data = event.data;
        var payload = data.payload;

        switch (data.type) {
          case "initKeyboard": {
            initializeKeyboardForEvent(payload.eventName);
            break;
          }
          case "initMouse": {
            initializeMouseForEvent(payload.eventName);
            break;
          }
          case "render": {
            render(payload.rendererData, payload.width, payload.height, payload.viewportX, payload.viewportY, payload.viewportWidth, payload.viewportHeight);
            break;
          }
        }
      }

      window.onload = function() {
        initCanvas();
      }



      function initCanvas() {
        function resizeCanvas() {
          solaWorker.postMessage({
            type: "resize",
            payload: {
              width: solaCanvas.width,
              height: solaCanvas.height,
            }
          });
        }

        new ResizeObserver(resizeCanvas).observe(window.solaCanvas);

        function onWindowResize() {
          window.solaCanvas.width = window.innerWidth;
          window.solaCanvas.height = window.innerHeight;
        }

        window.addEventListener("resize", onWindowResize);

        solaCanvas.width = window.innerWidth;
        solaCanvas.height = window.innerHeight;

        solaCanvas.oncontextmenu = function(e) {
          e.preventDefault(); e.stopPropagation();
        };

        solaCanvas.focus();
      }

      function initializeKeyboardForEvent(eventName) {
        window.keyboardListeners = window.keyboardListeners || {};

        if (window.keyboardListeners[eventName]) {
          window.removeEventListener(eventName, window.keyboardListeners[eventName], false);
        }

        window.keyboardListeners[eventName] = function(event) {
          if (event.target === solaCanvas) {
            event.stopPropagation();
            event.preventDefault();

            solaWorker.postMessage({
              type: "keyboard",
              payload: {
                eventName: eventName,
                keyCode: event.keyCode,
              },
            });
          }
        };

        window.addEventListener(eventName, window.keyboardListeners[eventName], false);
      }

      function initializeMouseForEvent(eventName) {
        window.mouseListeners = window.mouseListeners || {};

        if (window.mouseListeners[eventName]) {
          solaCanvas.removeEventListener(eventName, window.mouseListeners[eventName], false);
        }

        window.mouseListeners[eventName] = function(event) {
          if (event.target === window.solaCanvas) {
            var rect = event.target.getBoundingClientRect();
            var x = event.clientX - rect.left;
            var y = event.clientY - rect.top;

            solaWorker.postMessage({
              type: "mouse",
              payload: {
                eventName: eventName,
                which: event.which,
                x: x,
                y: y,
              },
            });
          }
        };

        solaCanvas.addEventListener(eventName, window.mouseListeners[eventName], false);
      }

      function render(rendererData, width, height, viewportX, viewportY, viewportWidth, viewportHeight) {
        solaContext2d.clearRect(0, 0, solaCanvas.width, solaCanvas.height);

        var imageData = new ImageData(rendererData, width, height);

        var tempCanvas = document.createElement("canvas");

        tempCanvas.width = width;
        tempCanvas.height = height;
        tempCanvas.getContext("2d").putImageData(imageData, 0, 0);

        window.solaContext2d.drawImage(tempCanvas, viewportX, viewportY, viewportWidth, viewportHeight);
      }
      </script>
      </body>
      </html>
      """;

    String html = template.formatted(OUTPUT_FILE_JS);

    try {
      Files.createDirectories(new File(buildDirectory).toPath());
      Files.writeString(new File(buildDirectory, OUTPUT_FILE_HTML).toPath(), html);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
