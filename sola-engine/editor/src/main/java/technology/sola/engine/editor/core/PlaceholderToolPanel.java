package technology.sola.engine.editor.core;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import technology.sola.engine.editor.core.components.EditorPanel;
import technology.sola.engine.editor.core.components.TabbedPanel;
import technology.sola.engine.editor.core.components.ThemedText;
import technology.sola.engine.editor.core.components.ToolPanel;
import technology.sola.engine.editor.core.config.EditorConfig;
import technology.sola.json.JsonObject;

@Deprecated
class PlaceholderToolPanel extends ToolPanel<PlaceholderToolPanel.Config> {
  PlaceholderToolPanel(EditorConfig editorConfig) {
    super(editorConfig);

    orientationProperty().set(Orientation.VERTICAL);

    var topPane = new SplitPane();
    var centerPanel = new TabbedPanel();

    centerPanel.addTab("temp" , "Title", new PlaceholderPanel());
    centerPanel.addTab("temp2" , "Title 2", new PlaceholderPanel());

    topPane.getItems().addAll(
      new PlaceholderPanel(),
      centerPanel,
      new PlaceholderPanel()
    );

    getItems().addAll(
      topPane,
      new PlaceholderPanel()
    );

    topPane.getDividers().get(0).setPosition(0.2);
    topPane.getDividers().get(1).setPosition(0.8);
    getDividers().get(0).setPosition(0.75);
  }

  @Override
  public String getToolLabel() {
    return "Placeholder";
  }

  @Override
  public String getToolId() {
    return "placeholder";
  }

  @Override
  public JsonObject buildToolConfigForSaving() {
    return new JsonObject();
  }

  @Override
  protected Config buildToolConfigFromEditorConfig(EditorConfig editorConfig) {
    return new Config();
  }

  @Deprecated
  record Config() {
  }

  @Deprecated
  static class PlaceholderPanel extends EditorPanel {
    @Deprecated
    public PlaceholderPanel() {
      super(new VBox(new ThemedText("Placeholder")));
    }
  }
}
