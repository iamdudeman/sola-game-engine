package technology.sola.engine.networking.rest;

import java.util.function.Supplier;

public interface RestClient {
  HttpResponse sendSync(HttpRequest httpRequest);

  void send(HttpRequest httpRequest, Supplier<HttpResponse> httpResponseSupplier);
}
