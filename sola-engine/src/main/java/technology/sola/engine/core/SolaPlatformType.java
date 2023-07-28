package technology.sola.engine.core;

/**
 * SolaPlatformType represents the broad types of devices that a particular {@link SolaPlatform} is implemented for.
 */
public enum SolaPlatformType {
  /**
   * The custom platform type is used for platforms that do not fit nicely in the main three types desktop, mobile and
   * web. Platforms of this type will rely on their unique name for platform specific logic in a game.
   */
  Custom,
  /**
   * The desktop platform type is used for platforms implemented for Windows, Linux or Mac.
   */
  Desktop,
  /**
   * The mobile platform type is used for platforms implemented for Android or iOS.
   */
  Mobile,
  /**
   * The web platform type is used for platforms implemented for web browsers like Chrome, Firefox or Safari.
   */
  Web,
}
