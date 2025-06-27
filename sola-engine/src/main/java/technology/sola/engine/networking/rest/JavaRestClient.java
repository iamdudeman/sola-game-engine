package technology.sola.engine.networking.rest;

import org.jspecify.annotations.NullMarked;
import technology.sola.json.JsonElement;
import technology.sola.json.SolaJson;
import technology.sola.logging.SolaLogger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * The Java-based implementation of {@link RestClient} utilizing {@link HttpURLConnection}.
 */
@NullMarked
public class JavaRestClient implements RestClient {
  private static final SolaLogger LOGGER = SolaLogger.of(JavaRestClient.class);

  @Override
  public void request(String method, String path, JsonElement body, Consumer<HttpResponse> httpResponseSupplier) {
    new Thread(new RequestRunnable(method, path, body, httpResponseSupplier)).start();
  }

  private record RequestRunnable(
    String method,
    String path,
    JsonElement body,
    Consumer<HttpResponse> httpResponseSupplier
  ) implements Runnable {
    @Override
    public void run() {
      try {
        URL url = new URL(path);

        try {
          HttpURLConnection connection = (HttpURLConnection) url.openConnection();

          connection.setInstanceFollowRedirects(false);
          connection.setRequestMethod(method);
          connection.setRequestProperty("accept", "application/json");
          connection.setRequestProperty("charset", "utf-8");
          connection.setUseCaches(false);

          if (!method.equalsIgnoreCase("GET")) {
            byte[] bodyBytes = body.toString().getBytes(StandardCharsets.UTF_8);

            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Length", Integer.toString(bodyBytes.length));

            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
              wr.write(bodyBytes);
            }
          }

          // send the request and get a response
          try (Scanner scanner = new Scanner(connection.getInputStream())) {
            StringBuilder stringBuilder = new StringBuilder();

            while (scanner.hasNext()) {
              stringBuilder.append(scanner.next());
            }

            int status = connection.getResponseCode();
            String response = stringBuilder.toString();

            httpResponseSupplier.accept(new HttpResponse(status, new SolaJson().parse(response)));
          }
        } catch (IOException ex) {
          LOGGER.error("Request %s %s failed", ex, method, path);
        }
      } catch (MalformedURLException ex) {
        LOGGER.error("Invalid url: %s", ex, path);
      }
    }
  }
}
