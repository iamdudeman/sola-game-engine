package technology.sola.engine.platform.automation;

import org.jspecify.annotations.NullMarked;
import technology.sola.engine.core.SolaPlatformConfig;
import technology.sola.engine.graphics.Color;

import java.nio.file.Path;

/**
 * AutomationTestingSolaPlatformConfig contains the configuration for an {@link AutomationTestingSolaPlatform}.
 *
 * @param backgroundColor   the background {@link Color} of the platform
 * @param snapshotDirectory the directory snapshots taken via {@link AutomationTestingSolaPlatformDriver#snapshot(String)}
 *                          are written to
 */
@NullMarked
public record AutomationTestingSolaPlatformConfig(
  Color backgroundColor,
  Path snapshotDirectory
) implements SolaPlatformConfig {
  /**
   * Creates an instance with a black background that writes snapshots to "build/snapshots".
   */
  public AutomationTestingSolaPlatformConfig() {
    this(Path.of("build", "snapshots"));
  }

  /**
   * Creates an instance with a black background that writes snapshots to the provided directory.
   *
   * @param snapshotDirectory the directory snapshots are written to
   */
  public AutomationTestingSolaPlatformConfig(Path snapshotDirectory) {
    this(Color.BLACK, snapshotDirectory);
  }

  @Override
  public boolean useSoftwareRendering() {
    return true;
  }
}
