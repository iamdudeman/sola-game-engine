package technology.sola.engine.platform.android.core;

import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.Font;
import technology.sola.engine.graphics.AffineTransform;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Layer;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.renderer.blend.BlendFunction;

import java.util.Collections;
import java.util.List;

public class AndroidRenderer implements Renderer {
  @Override
  public void setBlendFunction(BlendFunction blendFunction) {

  }

  @Override
  public BlendFunction getBlendFunction() {
    return null;
  }

  @Override
  public Font getFont() {
    return null;
  }

  @Override
  public void setFont(Font font) {

  }

  @Override
  public int getWidth() {
    return 0;
  }

  @Override
  public int getHeight() {
    return 0;
  }

  @Override
  public Renderer createRendererForImage(SolaImage solaImage) {
    return null;
  }

  @Override
  public List<Layer> getLayers() {
    return Collections.emptyList();
  }

  @Override
  public void setClamp(int x, int y, int width, int height) {

  }

  @Override
  public void clear(Color color) {

  }

  @Override
  public void setPixel(int x, int y, Color color) {

  }

  @Override
  public void drawLine(float x, float y, float x2, float y2, Color color) {

  }

  @Override
  public void drawRect(float x, float y, float width, float height, Color color) {

  }

  @Override
  public void fillRect(float x, float y, float width, float height, Color color) {

  }

  @Override
  public void drawCircle(float x, float y, float radius, Color color) {

  }

  @Override
  public void fillCircle(float x, float y, float radius, Color color) {

  }

  @Override
  public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3, Color color) {

  }

  @Override
  public void drawImage(SolaImage solaImage, float x, float y) {

  }

  @Override
  public void drawImage(SolaImage solaImage, AffineTransform affineTransform) {

  }

  @Override
  public void drawImage(SolaImage solaImage, float x, float y, float width, float height) {

  }
}
