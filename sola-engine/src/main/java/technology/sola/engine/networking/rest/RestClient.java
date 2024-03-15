package technology.sola.engine.networking.rest;

import java.util.function.Supplier;

// todo make public later once implemented
public interface RestClient {
  HttpResponse sendSync(HttpRequest httpRequest);

  void send(HttpRequest httpRequest, Supplier<HttpResponse> httpResponseSupplier);
}
