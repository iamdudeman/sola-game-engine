package technology.sola.engine.platform.android;

import android.content.Context;
import android.graphics.*;
import android.view.SurfaceView;
import androidx.constraintlayout.widget.ConstraintLayout;
import technology.sola.engine.graphics.renderer.SoftwareRenderer;
import technology.sola.engine.graphics.screen.AspectRatioSizing;
import technology.sola.engine.graphics.screen.Viewport;

class SolaSurfaceView extends SurfaceView {
  private final Paint paint = new Paint();
  private Canvas canvas;
  private Viewport viewport;

  SolaSurfaceView(Context context) {
    super(context);

    this.setLayoutParams(new ConstraintLayout.LayoutParams(
      ConstraintLayout.LayoutParams.MATCH_PARENT,
      ConstraintLayout.LayoutParams.MATCH_PARENT
    ));
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

    if (viewport != null) {
      viewport.resize(w, h);
    }
  }

  void setViewport(Viewport viewport) {
    this.viewport = viewport;

    viewport.resize(getWidth(), getHeight());
  }

  Canvas startDrawing() {
    canvas = getHolder().lockCanvas();

    return canvas;
  }

  void drawFromSoftwareRenderer(SoftwareRenderer softwareRenderer, AspectRatioSizing aspectRatioSizing) {
    if (canvas == null) {
      return;
    }

    var bitmap = Bitmap.createBitmap(softwareRenderer.getWidth(), softwareRenderer.getHeight(), Bitmap.Config.ARGB_8888);

    bitmap.setPixels(softwareRenderer.getPixels(), 0, softwareRenderer.getWidth(), 0, 0, softwareRenderer.getWidth(), softwareRenderer.getHeight());

    Rect src = new Rect(0, 0, softwareRenderer.getWidth(), softwareRenderer.getHeight());
    Rect dest = new Rect(
      aspectRatioSizing.x(),
      aspectRatioSizing.y(),
      aspectRatioSizing.width() + aspectRatioSizing.x(),
      aspectRatioSizing.height() + aspectRatioSizing.y()
    );

    canvas.drawBitmap(bitmap, src, dest, paint);
  }

  void finishDrawing() {
    if (canvas == null) {
      return;
    }

    getHolder().unlockCanvasAndPost(canvas);
    canvas = null;
  }
}
