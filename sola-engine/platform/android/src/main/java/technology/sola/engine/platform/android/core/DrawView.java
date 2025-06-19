package technology.sola.engine.platform.android.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.SoftwareRenderer;

public class DrawView extends View {
  private final Paint paint = new Paint();
  private Bitmap bitmap;
  private final SoftwareRenderer softwareRenderer;
  int width = 100;
  int height = 100;


  public DrawView(Context context) {
    super(context);

    this.setLayoutParams(new ConstraintLayout.LayoutParams(
      ConstraintLayout.LayoutParams.MATCH_PARENT,
      ConstraintLayout.LayoutParams.MATCH_PARENT
    ));

    softwareRenderer = new SoftwareRenderer(width, height);
  }



  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

    bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
  }

  @Override
  protected void onDraw(@NonNull Canvas canvas) {
    super.onDraw(canvas);

    softwareRenderer.fillCircle(20, 20, 10, Color.BLUE);
    softwareRenderer.drawString("Test", 5, 50, Color.YELLOW);

    bitmap.setPixels(softwareRenderer.getPixels(), 0, width, 0, 0, width, height);

    canvas.drawBitmap(bitmap, 0, 0, paint);
  }
}
