package technology.sola.engine.platform.android.core;

import technology.sola.engine.networking.rest.HttpResponse;
import technology.sola.engine.networking.rest.RestClient;
import technology.sola.json.JsonElement;

import java.util.function.Consumer;

public class AndroidRestClient implements RestClient {
  @Override
  public void request(String method, String path, JsonElement body, Consumer<HttpResponse> httpResponseSupplier) {
    // todo
  }
}
