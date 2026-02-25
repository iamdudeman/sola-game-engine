package technology.sola.engine.editor.tools.scene;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import technology.sola.engine.editor.core.components.EditorPanel;

class SceneActions extends EditorPanel {
  SceneActions(Node wrappedNode) {
    Button saveButton = new Button("Save");
    Button loadSceneButton = new Button("Load");
    Button newSceneButton = new Button("New");

    HBox hBox = new HBox();

    hBox.setSpacing(8);
    hBox.getChildren().addAll(saveButton, loadSceneButton, newSceneButton);

    setSpacing(8);
    setVgrow(wrappedNode, Priority.ALWAYS);

    getChildren().addAll(hBox, wrappedNode);
  }
}
