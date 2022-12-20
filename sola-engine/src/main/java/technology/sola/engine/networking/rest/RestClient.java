package technology.sola.engine.networking.rest;

public interface RestClient {
  HttpResponse send(HttpRequest httpRequest);
}
