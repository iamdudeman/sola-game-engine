package technology.sola.engine.platform.android.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceView;
import androidx.constraintlayout.widget.ConstraintLayout;
import technology.sola.engine.graphics.renderer.SoftwareRenderer;
import technology.sola.engine.graphics.screen.AspectRatioSizing;
import technology.sola.engine.graphics.screen.Viewport;

public class SolaSurfaceView extends SurfaceView {
  private final Paint paint = new Paint();
  private Canvas canvas;
  private Viewport viewport;

  public SolaSurfaceView(Context context) {
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

  public void setViewport(Viewport viewport) {
    this.viewport = viewport;

    viewport.resize(getWidth(), getHeight());
  }

  public void startDrawing() {
    canvas = getHolder().lockCanvas();
  }

  public void drawFromSoftwareRenderer(SoftwareRenderer softwareRenderer, AspectRatioSizing aspectRatioSizing) {
    if (canvas == null) {
      return;
    }

    var bitmap = Bitmap.createBitmap(softwareRenderer.getWidth(), softwareRenderer.getHeight(), Bitmap.Config.ARGB_8888);

    bitmap.setPixels(softwareRenderer.getPixels(), 0, softwareRenderer.getWidth(), 0, 0, softwareRenderer.getWidth(), softwareRenderer.getHeight());

    Rect src = new Rect(0, 0, softwareRenderer.getWidth(), softwareRenderer.getHeight());
    Rect dest = new Rect(aspectRatioSizing.x(), aspectRatioSizing.y(), aspectRatioSizing.width(), aspectRatioSizing.height());

    canvas.drawBitmap(bitmap, src, dest, paint);
  }

  public void finishDrawing() {
    if (canvas == null) {
      return;
    }

    getHolder().unlockCanvasAndPost(canvas);
    canvas = null;
  }
}
