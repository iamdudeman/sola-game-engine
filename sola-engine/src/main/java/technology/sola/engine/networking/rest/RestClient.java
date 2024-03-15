package technology.sola.engine.networking.rest;

import technology.sola.json.JsonElement;
import technology.sola.json.JsonObject;

import java.util.function.Supplier;

// todo ability to set headers
public interface RestClient {
  String getUrlBase();

  void request(String method, String path, JsonElement body, Supplier<HttpResponse> httpResponseSupplier);

  default void get(String path, Supplier<HttpResponse> httpResponseSupplier) {
    get(path, new JsonElement(new JsonObject()), httpResponseSupplier);
  }

  default void get(String path, JsonElement body, Supplier<HttpResponse> httpResponseSupplier) {
    request("GET", path, body, httpResponseSupplier);
  }

  default void post(String path, Supplier<HttpResponse> httpResponseSupplier) {
    request("POST", path, new JsonElement(new JsonObject()), httpResponseSupplier);
  }

  default void post(String path, JsonElement body, Supplier<HttpResponse> httpResponseSupplier) {
    request("POST", path, body, httpResponseSupplier);
  }

  default void put(String path, Supplier<HttpResponse> httpResponseSupplier) {
    put(path, new JsonElement(new JsonObject()), httpResponseSupplier);
  }

  default void put(String path, JsonElement body, Supplier<HttpResponse> httpResponseSupplier) {
    request("PUT", path, body, httpResponseSupplier);
  }

  default void delete(String path, Supplier<HttpResponse> httpResponseSupplier) {
    delete(path, new JsonElement(new JsonObject()), httpResponseSupplier);
  }

  default void delete(String path, JsonElement body, Supplier<HttpResponse> httpResponseSupplier) {
    request("DELETE", path, body, httpResponseSupplier);
  }
}
