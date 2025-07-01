package technology.sola.engine.platform.android;

import android.content.Context;
import android.graphics.*;
import android.view.SurfaceView;
import androidx.constraintlayout.widget.ConstraintLayout;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import technology.sola.engine.graphics.renderer.SoftwareRenderer;
import technology.sola.engine.graphics.screen.AspectRatioSizing;
import technology.sola.engine.graphics.screen.Viewport;

@NullMarked
class SolaSurfaceView extends SurfaceView {
  private final Paint paint = new Paint();
  @Nullable
  private Canvas canvas;
  @Nullable
  private Viewport viewport;
  @Nullable
  private Bitmap softwareRendererBitmap;
  @Nullable
  private Rect softwareRendererSrcRect;
  @Nullable
  private SoftwareRenderer softwareRenderer;

  SolaSurfaceView(Context context) {
    super(context);

    this.setLayoutParams(new ConstraintLayout.LayoutParams(
      ConstraintLayout.LayoutParams.MATCH_PARENT,
      ConstraintLayout.LayoutParams.MATCH_PARENT
    ));
  }

  @Override
  protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
    super.onSizeChanged(width, height, oldWidth, oldHeight);

    if (viewport != null) {
      viewport.resize(width, height);
    }
  }

  void setViewport(Viewport viewport) {
    this.viewport = viewport;

    viewport.resize(getWidth(), getHeight());
  }

  @Nullable
  Canvas startDrawing() {
    canvas = getHolder().lockHardwareCanvas();

    return canvas;
  }

  void drawFromSoftwareRenderer(SoftwareRenderer softwareRenderer, AspectRatioSizing aspectRatioSizing) {
    if (canvas == null) {
      return;
    }

    int rendererWidth = softwareRenderer.getWidth();
    int rendererHeight = softwareRenderer.getHeight();

    if (softwareRendererBitmap == null || this.softwareRenderer != softwareRenderer) {
      this.softwareRenderer = softwareRenderer;
      softwareRendererSrcRect = new Rect(0, 0, rendererWidth, rendererHeight);
      softwareRendererBitmap = Bitmap.createBitmap(rendererWidth, rendererHeight, Bitmap.Config.ARGB_8888);
    }

    softwareRendererBitmap.setPixels(softwareRenderer.getPixels(), 0, rendererWidth, 0, 0, rendererWidth, rendererHeight);

    Rect dest = new Rect(
      aspectRatioSizing.x(),
      aspectRatioSizing.y(),
      aspectRatioSizing.width() + aspectRatioSizing.x(),
      aspectRatioSizing.height() + aspectRatioSizing.y()
    );

    canvas.drawBitmap(softwareRendererBitmap, softwareRendererSrcRect, dest, paint);
  }

  void finishDrawing() {
    if (canvas == null) {
      return;
    }

    getHolder().unlockCanvasAndPost(canvas);
    canvas = null;
  }
}
