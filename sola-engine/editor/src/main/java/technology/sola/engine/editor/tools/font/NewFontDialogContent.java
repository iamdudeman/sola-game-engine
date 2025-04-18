package technology.sola.engine.editor.tools.font;

import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import technology.sola.engine.assets.graphics.font.FontStyle;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.components.input.IntegerSpinner;
import technology.sola.engine.tooling.font.FontListTool;
import technology.sola.engine.tooling.font.FontRasterizerTool;

import java.io.File;
import java.util.Arrays;

/**
 * NewFontDialogContent is a form for creating a new font asset that can be easily nested in
 * a {@link technology.sola.engine.editor.core.notifications.DialogService#custom(String, Parent)}.
 */
public class NewFontDialogContent extends EditorPanel {
  private final ComboBox<String> fontChoice;
  private final ComboBox<String> styleChoice;
  private final IntegerSpinner sizeChoice;
  private final TextArea charactersArea;
  private final Button cancelButton;
  private final Button createButton;

  /**
   * Creates a new instance. The parentFolder specified is where the new font asset will be created if the create
   * button is clicked. After a new font is created the onAfterCreate {@link Runnable} will fire.
   *
   * @param parentFolder  the parent {@link File} to create the font asset in
   * @param onAfterCreate the callback to run after font creation completes
   */
  public NewFontDialogContent(File parentFolder, Runnable onAfterCreate) {
    setSpacing(8);

    // font options
    fontChoice = new ComboBox<>(FXCollections.observableArrayList(
      new FontListTool().execute().split("\n")
    ));
    styleChoice = new ComboBox<>(FXCollections.observableArrayList(
      Arrays.stream(FontStyle.values()).map(FontStyle::toString).toArray(String[]::new)
    ));
    sizeChoice = new IntegerSpinner(12, 120);

    // characters
    charactersArea = new TextArea();

    // buttons
    cancelButton = new Button("Cancel");
    createButton = new Button("Create");

    initializeUiStateAndEvents(parentFolder, onAfterCreate);

    getChildren().addAll(
      buildFontOptionsUi(),
      charactersArea,
      buildButtonsUi()
    );
  }

  private void initializeUiStateAndEvents(File parentFolder, Runnable onAfterCreate) {
    fontChoice.getSelectionModel().select(0);
    styleChoice.getSelectionModel().select(0);

    fontChoice.valueProperty().addListener((observable, oldValue, newValue) -> charactersArea.setFont(getFont()));
    styleChoice.valueProperty().addListener((observable, oldValue, newValue) -> charactersArea.setFont(getFont()));
    sizeChoice.valueProperty().addListener((observable, oldValue, newValue) -> charactersArea.setFont(getFont()));

    charactersArea.textProperty().addListener((observable, oldValue, newValue) -> {
      createButton.setDisable(newValue.isEmpty());
    });

    createButton.setOnAction(event -> {
      new FontRasterizerTool(parentFolder)
        .execute(
          fontChoice.getValue(),
          sizeChoice.getValue().toString(),
          styleChoice.getValue(),
          charactersArea.getText()
        );

      onAfterCreate.run();
      closeParentStage();
    });
    cancelButton.setOnAction((event) -> {
      closeParentStage();
    });

    // remove whitespace and add it back in later
    charactersArea.setText(FontRasterizerTool.DEFAULT_CHARACTERS.replace(" ", ""));
    charactersArea.setWrapText(true);
    charactersArea.setFont(getFont());
    charactersArea.setPrefWidth(600);
    charactersArea.setPrefHeight(400);
  }

  private Font getFont() {
    FontWeight fontWeight = FontWeight.NORMAL;
    FontPosture fontPosture = FontPosture.REGULAR;
    FontStyle fontStyle = FontStyle.valueOf(styleChoice.getValue());

    switch (fontStyle) {
      case BOLD:
        fontWeight = FontWeight.BOLD;
        break;
      case ITALIC:
        fontPosture = FontPosture.ITALIC;
        break;
      case BOLD_ITALIC:
        fontWeight = FontWeight.BOLD;
        fontPosture = FontPosture.ITALIC;
        break;
    }

    return Font.font(
      fontChoice.getValue(),
      fontWeight,
      fontPosture,
      sizeChoice.getValue()
    );
  }

  private HBox buildFontOptionsUi() {
    HBox container = new HBox();

    container.setSpacing(8);

    container.getChildren().addAll(
      fontChoice,
      styleChoice,
      sizeChoice
    );

    return container;
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
