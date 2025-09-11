package technology.sola.engine.defaults.graphics.modules;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.World;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.math.linear.Matrix3D;

/**
 * SolaGraphicsModule adds additional rendering functionality to {@link technology.sola.engine.defaults.SolaGraphics}.
 */
@NullMarked
public abstract class SolaGraphicsModule implements Comparable<SolaGraphicsModule> {
  private boolean isActive = true;

  /**
   * The main render method for a graphics module that is called once per frame. It will call the
   * {@link #renderMethod(Renderer, World, Matrix3D, Matrix3D)} if the module is active.
   *
   * @param renderer                   tbe {@link Renderer} instance
   * @param world                      the {@link World} instance
   * @param cameraScaleTransform       the camera's scale
   * @param cameraTranslationTransform the camera's translation
   */
  public void render(Renderer renderer, World world, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform) {
    renderMethod(renderer, world, cameraScaleTransform, cameraTranslationTransform);
  }

  /**
   * The specific render method for a graphics module that is called once per frame if active.
   *
   * @param renderer                   tbe {@link Renderer} instance
   * @param world                      the {@link World} instance
   * @param cameraScaleTransform       the camera's scale
   * @param cameraTranslationTransform the camera's translation
   */
  public abstract void renderMethod(Renderer renderer, World world, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform);

  /**
   * @return true if this graphics module is actively rendering
   */
  public boolean isActive() {
    return isActive;
  }

  /**
   * Sets the active state of this graphics module.
   *
   * @param active the new active state
   */
  public void setActive(boolean active) {
    isActive = active;
  }

  /**
   * Gets the order of this graphics module. A higher value means it will be rendered on top.
   *
   * @return the order of this graphics module
   */
  public int getOrder() {
    return 0;
  }

  @Override
  public int compareTo(SolaGraphicsModule solaGraphicsModule) {
    return Integer.compare(getOrder(), solaGraphicsModule.getOrder());
  }
}
