package technology.sola.engine.platform.browser.tools;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLConnection;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class SimpleSolaBrowserFileServer {
  private final String indexHtmlDirectoryPath;
  private final String solaJsDirectoryPath;
  private final String assetsDirectoryPath;

  public SimpleSolaBrowserFileServer(String indexHtmlDirectoryPath, String assetsDirectoryPath) {
    this(indexHtmlDirectoryPath, indexHtmlDirectoryPath, assetsDirectoryPath);
  }

  public SimpleSolaBrowserFileServer(String indexHtmlDirectoryPath, String solaJsDirectoryPath, String assetsDirectoryPath) {
    this.indexHtmlDirectoryPath = indexHtmlDirectoryPath;
    this.solaJsDirectoryPath = solaJsDirectoryPath;
    this.assetsDirectoryPath = assetsDirectoryPath;
  }

  public void start(int port) throws IOException {
    HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);

    httpServer.createContext("/", buildRootHandler(httpExchange -> new File(indexHtmlDirectoryPath, "index.html")));
    httpServer.createContext("/sola.js", buildRootHandler(httpExchange -> new File(solaJsDirectoryPath, "sola.js")));

    httpServer.createContext("/assets", buildRootHandler(httpExchange -> {
      String asset = httpExchange.getRequestURI().getPath().replace("/assets", "");

      return new File(assetsDirectoryPath, asset);
    }));

    httpServer.setExecutor(Executors.newCachedThreadPool());
    httpServer.start();
    System.out.println("Started file server on port " + port);
  }

  private HttpHandler buildRootHandler(Function<HttpExchange, File> getFile) {
    return exchange -> {
      File file = getFile.apply(exchange);

      System.out.println("Serving " + file.getPath() + " " + file.exists());

      exchange.getResponseHeaders().set("Content-Type", getContentType(file));
      exchange.sendResponseHeaders(200, 0);

      OutputStream responseBody = exchange.getResponseBody();

      try (FileInputStream fileInputStream = new FileInputStream(file)) {
        responseBody.write(fileInputStream.readAllBytes());
      }

      responseBody.close();
    };
  }

  private String getContentType(File file) {
    String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase();

    return switch (extension) {
      case "html" -> "text/html";
      case "js" -> "application/javascript";
      case "json" -> "application/json";
      case "png" -> "image/png";
      case "wav" -> "audio/wave";
      default -> URLConnection.guessContentTypeFromName(file.getName());
    };
  }
}
