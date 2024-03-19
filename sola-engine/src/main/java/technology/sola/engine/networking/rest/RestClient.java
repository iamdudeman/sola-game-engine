package technology.sola.engine.networking.rest;

import technology.sola.json.JsonElement;
import technology.sola.json.JsonObject;

import java.util.function.Consumer;

public interface RestClient {
  void request(String method, String path, JsonElement body, Consumer<HttpResponse> httpResponseSupplier);

  default void get(String path, Consumer<HttpResponse> httpResponseSupplier) {
    get(path, new JsonElement(new JsonObject()), httpResponseSupplier);
  }

  default void get(String path, JsonElement body, Consumer<HttpResponse> httpResponseSupplier) {
    request("GET", path, body, httpResponseSupplier);
  }

  default void post(String path, Consumer<HttpResponse> httpResponseSupplier) {
    request("POST", path, new JsonElement(new JsonObject()), httpResponseSupplier);
  }

  default void post(String path, JsonElement body, Consumer<HttpResponse> httpResponseSupplier) {
    request("POST", path, body, httpResponseSupplier);
  }

  default void put(String path, Consumer<HttpResponse> httpResponseSupplier) {
    put(path, new JsonElement(new JsonObject()), httpResponseSupplier);
  }

  default void put(String path, JsonElement body, Consumer<HttpResponse> httpResponseSupplier) {
    request("PUT", path, body, httpResponseSupplier);
  }

  default void delete(String path, Consumer<HttpResponse> httpResponseSupplier) {
    delete(path, new JsonElement(new JsonObject()), httpResponseSupplier);
  }

  default void delete(String path, JsonElement body, Consumer<HttpResponse> httpResponseSupplier) {
    request("DELETE", path, body, httpResponseSupplier);
  }
}
