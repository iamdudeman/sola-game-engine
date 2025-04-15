package technology.sola.engine.editor.core.components;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import technology.sola.engine.editor.core.config.EditorConfig;

@Deprecated
public class PlaceholderToolPanel extends ToolPanel {
  public PlaceholderToolPanel(EditorConfig editorConfig) {
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
  }

  @Override
  public String getToolLabel() {
    return "Placeholder";
  }

  @Override
  public String getToolId() {
    return "placeholder";
  }
}
