package technology.sola.engine.core;

import technology.sola.engine.graphics.Renderer;

class NoSolaPlatform extends AbstractSolaPlatform {
  @Override
  protected void init() {
    // Not needed
  }

  @Override
  protected void start() {
    // Not needed
  }

  @Override
  protected void render(Renderer renderer) {
    // Not needed
  }

  @Override
  public void launch(AbstractSola abstractSola) {
    throw new IllegalStateException("NoSolaPlatform should never be used to Launch an AbstractSola instance");
  }
}
