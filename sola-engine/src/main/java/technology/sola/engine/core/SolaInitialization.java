package technology.sola.engine.core;

public class SolaInitialization {
  private boolean isAsync = false;
  private final Runnable onComplete;

  SolaInitialization(Runnable onComplete) {
    this.onComplete = onComplete;
  }

  public boolean isAsync() {
    return isAsync;
  }

  public void setAsyncInitialization() {
    isAsync = true;
  }

  public void completeAsync() {
    if (!isAsync) {
      throw new RuntimeException("Should not manually call completeAsync when not using async initialization");
    }

    onComplete.run();
  }
}
