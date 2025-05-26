package technology.sola.engine.networking.rest;

import org.jspecify.annotations.NullMarked;
import technology.sola.json.JsonElement;

/**
 * HttpResponse contains details from the server's response to an http request.
 *
 * @param status the status code of the response
 * @param body   the json response body
 */
@NullMarked
public record HttpResponse(
  int status,
  JsonElement body
) {
}
