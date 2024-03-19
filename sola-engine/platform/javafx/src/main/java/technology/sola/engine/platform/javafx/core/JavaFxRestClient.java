package technology.sola.engine.platform.javafx.core;

import technology.sola.engine.networking.rest.HttpResponse;
import technology.sola.engine.networking.rest.RestClient;
import technology.sola.json.JsonElement;
import technology.sola.json.SolaJson;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.function.Consumer;

public class JavaFxRestClient implements RestClient {
  @Override
  public void request(String method, String path, JsonElement body, Consumer<HttpResponse> httpResponseSupplier) {
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

          System.out.println(status + " " + response);

          httpResponseSupplier.accept(new HttpResponse(status, new SolaJson().parse(response)));
        }
      } catch (IOException ex) {
        // todo handle this
        ex.printStackTrace();
      }
    } catch (MalformedURLException ex) {
      // todo
      ex.printStackTrace();
    }
  }
}
