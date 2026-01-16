package technology.sola.engine.asset;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.assets.BulkAssetLoader;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BulkAssetLoaderTest {
  @Test
  void whenNoAssetsAdded_shouldImmediatelyCompleteWithEmptyArray() throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    var mockAssetLoaderProvider = Mockito.mock(AssetLoaderProvider.class);
    BulkAssetLoader bulkAssetLoader = new BulkAssetLoader(mockAssetLoaderProvider);

    bulkAssetLoader.loadAll()
      .onComplete(assets -> {
        assertEquals(0, assets.length);

        countDownLatch.countDown();
      });

    assertTrue(
      countDownLatch.await(1L, TimeUnit.SECONDS),
      "Timed out waiting for BulkAssetLoader to complete"
    );
  }
}
