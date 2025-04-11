package technology.sola.engine.editor.core.notifications;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import technology.sola.engine.editor.core.components.ThemedText;

// todo initial logic here but a lot of cleanup needed
//  better styling
//  different kinds (info, success, error)
//  allow for multiple on screen at same time
class Toast {
  // todo find better way to get primary stage (maybe required init method that causes all methods to fail if not called)
  public static Stage primaryStage;

  public static void makeText(Stage ownerStage, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay) {
    final Stage toastStage = new Stage();

    toastStage.initOwner(primaryStage);
    toastStage.setResizable(false);
    toastStage.initStyle(StageStyle.TRANSPARENT);

    final ThemedText text = new ThemedText(toastMsg);
    final StackPane root = new StackPane(text);

    root.styleProperty().set("-fx-background-radius: 20; -fx-background-color: rgba(0, 0, 0, 0.2); -fx-padding: 50px;");

    final Scene scene = new Scene(root);
    scene.setFill(Color.TRANSPARENT);
    toastStage.setScene(scene);
    toastStage.show();

    // make effect: fade-in, pause, then fade-out effect varying the opacity of the whole window
    final FadeTransition inTransition = new FadeTransition(new Duration(fadeInDelay), toastStage.getScene().getRoot());
    inTransition.setFromValue(0.0);
    inTransition.setToValue(1);

    final FadeTransition outTransition = new FadeTransition(new Duration(fadeOutDelay), toastStage.getScene().getRoot());
    outTransition.setFromValue(1.0);
    outTransition.setToValue(0);

    final PauseTransition pauseTransition = new PauseTransition(new Duration(toastDelay));

    final SequentialTransition mainTransition = new SequentialTransition(inTransition, pauseTransition, outTransition);
    mainTransition.setOnFinished(ae -> toastStage.close());
    mainTransition.play();
  }

  private Toast() {
  }
}
