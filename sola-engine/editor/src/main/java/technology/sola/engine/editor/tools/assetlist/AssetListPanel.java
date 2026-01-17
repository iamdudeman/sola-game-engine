package technology.sola.engine.editor.tools.assetlist;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import technology.sola.engine.assets.Asset;
import technology.sola.engine.assets.list.AssetList;
import technology.sola.engine.assets.list.AssetListJsonMapper;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.components.ThemedText;
import technology.sola.engine.editor.core.components.assets.AssetType;
import technology.sola.engine.editor.core.components.input.LabelWrapper;
import technology.sola.engine.editor.core.utils.ToastService;
import technology.sola.json.SolaJson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AssetListPanel extends EditorPanel {
  public void update() throws IOException {
    // clear old stuff
    getChildren().clear();

    AssetList assetList = new SolaJson().parse(
      Files.readString(Path.of(AssetList.PATH)),
      new AssetListJsonMapper()
    );

    // prepare rows
    var audioStuff = buildRowsAndGroup("Audio", AssetType.AUDIO_CLIP, assetList.audioAssets());
    var fontStuff = buildRowsAndGroup("Font", AssetType.FONT, assetList.fontAssets());
    var guiStuff = buildRowsAndGroup("Gui", AssetType.GUI, assetList.guiAssets());
    var imageStuff = buildRowsAndGroup("Images", AssetType.IMAGES, assetList.imageAssets());
    var sceneSheetStuff = buildRowsAndGroup("Scene", AssetType.SCENES, assetList.sceneAssets());
    var spriteSheetStuff = buildRowsAndGroup("SpriteSheet", AssetType.SPRITES, assetList.spriteSheetAssets());

    // save button
    Button saveButton = new Button("Save Asset List");

    saveButton.setOnAction(e -> {
      AssetList updatedList = new AssetList(
        audioStuff.assetListRows.stream().map(AssetListRow::toDetail).toList(),
        fontStuff.assetListRows.stream().map(AssetListRow::toDetail).toList(),
        guiStuff.assetListRows.stream().map(AssetListRow::toDetail).toList(),
        imageStuff.assetListRows.stream().map(AssetListRow::toDetail).toList(),
        spriteSheetStuff.assetListRows.stream().map(AssetListRow::toDetail).toList(),
        sceneSheetStuff.assetListRows.stream().map(AssetListRow::toDetail).toList()
      );

      try {
        Files.writeString(
          Path.of(AssetList.PATH),
          new AssetListJsonMapper().toJson(updatedList).toString()
        );
        ToastService.info("Updated Asset List");
      } catch (IOException ex) {
        ToastService.error(ex.getMessage());
      }
    });

    // rebuild ui
    VBox vBox = new VBox();

    vBox.getChildren().addAll(
      saveButton,
      audioStuff.titledPane, fontStuff.titledPane, guiStuff.titledPane,
      imageStuff.titledPane, spriteSheetStuff.titledPane, sceneSheetStuff.titledPane
    );

    ScrollPane scrollPane = new ScrollPane(vBox);

    scrollPane.setFitToWidth(true);

    getChildren().add(scrollPane);
  }

  private <T extends Asset> List<AssetListRow<T>> populateAssetListRows(
    String assetFolder,
    AssetType assetType,
    List<AssetListRow<T>> assetListRows
  ) {
    File assetTypeRoot = new File(assetFolder);
    String[] files = assetTypeRoot.list();

    if (files == null) {
      return  assetListRows;
    }

    for (var file : files) {
      File nestedFile =  new File(assetTypeRoot, file);

      if (nestedFile.isDirectory()) {
        populateAssetListRows(nestedFile.getPath(), assetType, assetListRows);
      } else if (assetType.matchesFilename(nestedFile.getName())) {
        assetListRows.add(new AssetListRow<>(nestedFile.getPath().replaceAll("\\\\", "/")));
      }
    }

    return assetListRows;
  }

  private <T extends Asset> void applyAssetListToRelatedRows(
    List<AssetList.AssetDetails<T>> assetDetails,
    List<AssetListRow<T>> assetListRows
  ) {
    for (var assetDetail : assetDetails) {
      assetListRows.stream()
        .filter(assetListRow -> assetListRow.path.equals(assetDetail.path()))
        .findFirst()
        .ifPresent(assetListRow -> {
          assetListRow.id =  assetDetail.id();
          assetListRow.isBlocking = assetDetail.isBlocking();
        });
    }
  }

  private <T extends Asset> TitledPane buildAssetGroup(String label, List<AssetListRow<T>> assetListRows) {
    TitledPane titledPane = new TitledPane();

    titledPane.setText(label);

    VBox vBox = new VBox();

    vBox.setSpacing(8);
    vBox.getChildren().addAll(
      assetListRows.stream()
        .map(this::buildRowUi)
        .toList()
    );

    titledPane.setContent(vBox);

    return titledPane;
  }

  private HBox buildRowUi(AssetListRow<?> assetListRow) {
    HBox hBox = new HBox();

    hBox.setSpacing(16);
    hBox.setAlignment(Pos.CENTER_LEFT);

    TextField idField = new TextField();

    idField.setPrefWidth(300);
    idField.setPromptText("Id");
    idField.setText(assetListRow.id);
    idField.textProperty().addListener((observable, oldValue, newValue) -> {
      assetListRow.id = newValue;
    });

    CheckBox isBlockingCheckbox = new CheckBox("Blocks rendering?");

    isBlockingCheckbox.setSelected(assetListRow.isBlocking);
    isBlockingCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
      assetListRow.isBlocking = newValue;
    });

    hBox.getChildren().addAll(
      LabelWrapper.horizontal(idField, "Id:"),
      isBlockingCheckbox,
      new ThemedText(ThemedText.Kind.PARAGRAPH, assetListRow.path)
    );

    return hBox;
  }

  private <T extends Asset> RowsAndGroup<T> buildRowsAndGroup(
    String label,
    AssetType assetType,
    List<AssetList.AssetDetails<T>> assetDetails
  ) {
    var path = "assets/" + assetType.path;
    var assetListRows = this.<T>populateAssetListRows(path, assetType, new ArrayList<>());

    this.<T>applyAssetListToRelatedRows(assetDetails, assetListRows);

    var assetGroup = this.<T>buildAssetGroup(label, assetListRows);

    return new RowsAndGroup<T>(
      assetListRows,
      assetGroup
    );
  }

  private record RowsAndGroup<T extends Asset>(
    List<AssetListRow<T>> assetListRows,
    TitledPane titledPane
  ) {

  }

  private static class AssetListRow<T extends Asset> {
    private String id;
    private String path;
    private boolean isBlocking;

    AssetListRow(String path) {
      this.id = path;
      this.path = path;
      this.isBlocking = false;
    }

    AssetList.AssetDetails<T> toDetail() {
      return new AssetList.AssetDetails<>(id, path, isBlocking);
    }
  }
}
