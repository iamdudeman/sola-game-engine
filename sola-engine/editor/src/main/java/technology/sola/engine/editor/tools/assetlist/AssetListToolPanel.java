package technology.sola.engine.editor.tools.assetlist;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jspecify.annotations.NullMarked;
import technology.sola.engine.editor.core.components.input.LabelWrapper;
import technology.sola.engine.editor.core.config.EditorConfig;
import technology.sola.engine.editor.core.utils.ToastService;
import technology.sola.engine.editor.tools.ToolPanel;
import technology.sola.json.JsonObject;

import java.io.IOException;

/**
 * AssetListToolPanel is a {@link ToolPanel} for managing the list of assets loaded via
 * the main {@link technology.sola.engine.assets.list.AssetList}.
 */
@NullMarked
public class AssetListToolPanel extends ToolPanel<AssetListToolConfig> {
  private final AssetListPanel assetListPanel = new AssetListPanel();

  /**
   * Creates an instance of AssetListToolPanel initialized via the {@link EditorConfig}.
   *
   * @param editorConfig the {@link EditorConfig} instance
   */
  public AssetListToolPanel(EditorConfig editorConfig) {
    super(editorConfig);
    var items = getItems();

    //    items.addAll(audioClipAssetTree, tabbedPanel);

    //    Platform.runLater(() -> {
    //      audioClipAssetTree.restoreOpenedFilesAndSelection(toolConfig.openedFileIds(), toolConfig.openId());

    //      setDividerPositions(toolConfig.dividerPosition());
    //    });

    items.add(assetListPanel);

//    TitledPane titledPane = new TitledPane();
//    titledPane.setText("Audio");
//
//    VBox vBox = new VBox();
//
//    vBox.setAlignment(Pos.TOP_CENTER);
//
//    HBox hBox = new HBox();
//    hBox.setSpacing(10);
//    hBox.setAlignment(Pos.CENTER_LEFT);
//    hBox.getChildren().addAll(
//      new CheckBox("Include?"),
//      LabelWrapper.vertical(new TextField(), "Id"),
//      LabelWrapper.vertical(new TextField(), "Path"),
//      new CheckBox("Blocking?")
//    );
//    vBox.getChildren().addAll(hBox);
//
//    titledPane.setContent(vBox);
//
//    items.add(titledPane);
  }

  @Override
  public String getToolLabel() {
    return "Asset List";
  }

  @Override
  public String getToolId() {
    return "assetList";
  }

  @Override
  public JsonObject buildToolConfigForSaving() {
    var config = new AssetListToolConfig();

    return new AssetListToolConfig.ConfigJsonMapper().toJson(config);
  }

  @Override
  public void onSwitch() {
    try {
      assetListPanel.update();
    } catch (IOException ex) {
      ToastService.error("Error onSwitch to AssetListToolPanel:" + ex.getMessage());
    }
  }

  @Override
  protected AssetListToolConfig buildToolConfigFromEditorConfig(EditorConfig editorConfig) {
    var toolConfigJson = editorConfig.toolConfigurations().get(getToolId());

    if (toolConfigJson == null) {
      return new AssetListToolConfig();
    }

    return new AssetListToolConfig.ConfigJsonMapper().toObject(toolConfigJson);
  }
}
