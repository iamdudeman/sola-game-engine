package technology.sola.engine.editor.tools.sprites;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.utils.DialogService;

import java.io.File;

/**
 * SpritesFontDialogContent is a form for creating a new sprites asset that can be easily nested in
 * a {@link DialogService#custom(String, Parent)}.
 */
public class NewSpriteSheetDialogContent extends EditorPanel {
  private final Button cancelButton;
  private final Button createButton;

  /**
   * Creates a new instance. The parentFolder specified is where the new font asset will be created if the create
   * button is clicked. After a new font is created the onAfterCreate {@link Runnable} will fire.
   *
   * @param parentFolder  the parent {@link File} to create the sprites asset in
   * @param onAfterCreate the callback to run after font creation completes
   */
  public NewSpriteSheetDialogContent(File parentFolder, Runnable onAfterCreate) {
    setSpacing(8);

    // buttons
    cancelButton = new Button("Cancel");
    createButton = new Button("Create");

    initializeUiStateAndEvents(parentFolder, onAfterCreate);

    getChildren().addAll(
      buildButtonsUi()
    );
  }

  private void initializeUiStateAndEvents(File parentFolder, Runnable onAfterCreate) {
    createButton.setOnAction(event -> {
      // todo create new asset file(s) / maybe copy image from place selected

      onAfterCreate.run();
      closeParentStage();
    });
    cancelButton.setOnAction((event) -> {
      closeParentStage();
    });
  }

  private HBox buildButtonsUi() {
    HBox container = new HBox();

    container.setSpacing(8);

    container.getChildren().addAll(
      cancelButton,
      createButton
    );

    return container;
  }
}
