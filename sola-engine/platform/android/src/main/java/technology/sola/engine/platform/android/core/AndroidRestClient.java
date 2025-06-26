package technology.sola.engine.platform.android.core;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.networking.rest.HttpResponse;
import technology.sola.engine.networking.rest.RestClient;
import technology.sola.json.JsonElement;

import java.util.function.Consumer;

/**
 * The {@link technology.sola.engine.platform.android.AndroidSolaPlatform} implementation of {@link RestClient}.
 */
@NullMarked
public class AndroidRestClient implements RestClient {
  @Override
  public void request(String method, String path, JsonElement body, Consumer<HttpResponse> httpResponseSupplier) {
    // todo implement
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
