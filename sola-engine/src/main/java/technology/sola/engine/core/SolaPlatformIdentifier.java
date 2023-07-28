package technology.sola.engine.core;

/**
 * SolaPlatformIdentifier is a unique identifier for a {@link SolaPlatform} implementation. This can be used in
 * conditional checks for platform specific logic (or {@link SolaPlatformType} specific logic).
 *
 * @param name the unique name for the platform
 * @param type the type of platform
 */
public record SolaPlatformIdentifier(String name, SolaPlatformType type) {
  /**
   * Identifier for the Swing {@link SolaPlatform} implementation.
   */
  public static SolaPlatformIdentifier SWING = new SolaPlatformIdentifier("Swing", SolaPlatformType.Desktop);
  /**
   * Identifier for the JavaFx {@link SolaPlatform} implementation.
   */
  public static SolaPlatformIdentifier JAVA_FX = new SolaPlatformIdentifier("JavaFX", SolaPlatformType.Desktop);
  /**
   * Identifier for the Browser {@link SolaPlatform} implementation.
   */
  public static SolaPlatformIdentifier WEB_BROWSER = new SolaPlatformIdentifier("Web Browser", SolaPlatformType.Web);
  /**
   * Identifier for the Android {@link SolaPlatform} implementation.
   */
  public static SolaPlatformIdentifier ANDROID = new SolaPlatformIdentifier("Android", SolaPlatformType.Mobile);
}
