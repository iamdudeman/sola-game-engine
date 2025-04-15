package technology.sola.engine.editor.font;

import javafx.application.Platform;
import technology.sola.engine.editor.core.components.TabbedPanel;
import technology.sola.engine.editor.core.components.ToolPanel;
import technology.sola.engine.editor.core.config.EditorConfig;
import technology.sola.json.JsonArray;
import technology.sola.json.JsonObject;
import technology.sola.json.mapper.JsonMapper;

import java.util.ArrayList;
import java.util.List;

public class FontToolPanel extends ToolPanel<FontToolPanel.Config> {
  private final TabbedPanel tabbedPanel;
  private final FontAssetTree fontAssetTree;

  public FontToolPanel(EditorConfig editorConfig) {
    super(editorConfig);
    var items = getItems();

    tabbedPanel = new TabbedPanel();
    fontAssetTree = new FontAssetTree(tabbedPanel);

    var config = buildToolConfigFromEditorConfig(editorConfig);

    items.addAll(fontAssetTree, tabbedPanel);

    Platform.runLater(() -> {
      fontAssetTree.restoreOpenedFilesAndSelection(config.openedFileIds(), config.openId());

      setDividerPositions(config.dividerPosition());
    });
  }

  @Override
  public String getToolLabel() {
    return "Font";
  }

  @Override
  public String getToolId() {
    return "font";
  }

  @Override
  public JsonObject buildToolConfigForSaving() {
    var config = new Config(
      tabbedPanel.getOpenedTabIds(),
      getDividers().get(0).getPosition(),
      tabbedPanel.getSelectedId()
    );

    return new ConfigJsonMapper().toJson(config);
  }

  @Override
  protected Config buildToolConfigFromEditorConfig(EditorConfig editorConfig) {
    var toolConfigJson = editorConfig.toolConfigurations().get(getToolId());

    if (toolConfigJson == null) {
      return new Config();
    }

    return new ConfigJsonMapper().toObject(toolConfigJson);
  }

  public record Config(
    List<String> openedFileIds,
    double dividerPosition,
    String openId
  ) {
    public Config() {
      this(List.of(), 0.2, null);
    }
  }

  public static class ConfigJsonMapper implements JsonMapper<Config> {
    @Override
    public Class<Config> getObjectClass() {
      return Config.class;
    }

    @Override
    public JsonObject toJson(Config config) {
      JsonObject json = new JsonObject();
      JsonArray openedFiles = new JsonArray();

      config.openedFileIds.forEach(openedFiles::add);

      json.put("openedFiles", openedFiles);
      json.put("dividerPosition", config.dividerPosition());
      if (config.openId == null) {
        json.putNull("openId");
      } else {
        json.put("openId", config.openId());
      }

      return json;
    }

    @Override
    public Config toObject(JsonObject jsonObject) {
      List<String> openedFileIds = new ArrayList<>();

      jsonObject.getArray("openedFiles").forEach(jsonElement -> {
        openedFileIds.add(jsonElement.asString());
      });

      return new Config(
        openedFileIds,
        jsonObject.getDouble("dividerPosition"),
        jsonObject.getString("openId", null)
      );
    }
  }
}
