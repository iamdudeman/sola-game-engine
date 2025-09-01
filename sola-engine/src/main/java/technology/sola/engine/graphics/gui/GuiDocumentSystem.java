package technology.sola.engine.graphics.gui;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.EcsSystem;
import technology.sola.ecs.World;

/**
 * The {@link EcsSystem} responsible for updating the attached {@link GuiDocument}.
 */
@NullMarked
public class GuiDocumentSystem extends EcsSystem {
  public static final int ORDER = 99;
  private final GuiDocument guiDocument;

  /**
   * Creates an instance of the system for the desired {@link GuiDocument}.
   *
   * @param guiDocument the {@link GuiDocument} to update
   */
  public GuiDocumentSystem(GuiDocument guiDocument) {
    this.guiDocument = guiDocument;
  }

  @Override
  public void update(World world, float deltaTime) {
    guiDocument.update();
  }

  @Override
  public int getOrder() {
    return ORDER;
  }
}
