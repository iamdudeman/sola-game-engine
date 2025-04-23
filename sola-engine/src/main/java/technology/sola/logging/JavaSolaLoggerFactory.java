package technology.sola.logging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class JavaSolaLoggerFactory implements SolaLoggerFactory {
  private static final Map<String, FileHandler> FILE_HANDLER_MAP = new HashMap<>();

  @Override
  public SolaLogger getLogger(Class<?> clazz, String logFile) {
    return new SolaLogger(initLogger(clazz, logFile));
  }

  private static Handler buildConsoleHandler() {
    var handler = new ConsoleHandler();

    handler.setFormatter(new SolaLogMessageFormatter());

    return handler;
  }

  private static FileHandler buildFileHandler(String logFile) throws IOException {
    // todo change to sola.log
    FileHandler fileHandler = new FileHandler(logFile, 0, 1, true);

    fileHandler.setFormatter(new SolaLogMessageFormatter());

    return fileHandler;
  }

  private static Logger initLogger(Class<?> clazz, String logFile) {
    Logger logger = Logger.getLogger(clazz.getName());

    try {
      // todo confirm if this is needed or not
      if (!Files.exists(Paths.get("logs"))) {
        Files.createDirectory(Paths.get("logs"));
      }

      var fileHandler = FILE_HANDLER_MAP.get(logFile);

      if (fileHandler == null) {
        fileHandler = buildFileHandler(logFile);
        FILE_HANDLER_MAP.put(logFile, fileHandler);
      }

      logger.addHandler(fileHandler);
      logger.addHandler(buildConsoleHandler());
      logger.setUseParentHandlers(false);
    } catch (IOException ex) {
      logger.log(SolaLogger.SolaLevel.ERROR, ex, ex::getMessage);
    }

    return logger;
  }
}
