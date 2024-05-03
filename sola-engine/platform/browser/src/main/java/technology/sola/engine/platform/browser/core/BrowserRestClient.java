package technology.sola.engine.platform.browser.core;

import org.teavm.jso.ajax.XMLHttpRequest;
import technology.sola.engine.networking.rest.HttpResponse;
import technology.sola.engine.networking.rest.RestClient;
import technology.sola.json.JsonElement;
import technology.sola.json.SolaJson;

import java.util.function.Consumer;

/**
 * The {@link technology.sola.engine.platform.browser.BrowserSolaPlatform} implementation of {@link RestClient}.
 */
public class BrowserRestClient implements RestClient {
  @Override
  public void request(String method, String path, JsonElement body, Consumer<HttpResponse> httpResponseSupplier) {
    XMLHttpRequest request = new XMLHttpRequest();

    request.open(method, path, true);
    request.setRequestHeader("accept", "application/json");
    request.setRequestHeader("charset", "UTF-8");
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

    request.onComplete(() -> {
      int status = request.getStatus();
      String responseString = request.getResponse().toString();

      httpResponseSupplier.accept(new HttpResponse(
        status,
        new SolaJson().parse(responseString)
      ));
    });

    if (method.equalsIgnoreCase("GET")) {
      request.send();
    } else {
      request.send(body.toString());
    }
  }
}
