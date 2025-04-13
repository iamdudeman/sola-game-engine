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

import java.util.LinkedList;
import java.util.List;


public class Toast {
  public static Stage primaryStage;
  private static final List<QueuedToast> queuedToastList = new LinkedList<>();
  private static boolean isPlaying = false;
  private static final AnimationConfig defaultConfig = new AnimationConfig(200, 2000, 200);

  public static void initialize(Stage primaryStage) {
    Toast.primaryStage = primaryStage;
  }

  public static void info(String text) {
    toast(text, ToastType.INFO);
  }

  public static void warn(String text) {
    toast(text, ToastType.WARNING);
  }

  public static void error(String text) {
    toast(text, ToastType.ERROR);
  }

  private static void toast(String text, ToastType toastType) {
    if (isPlaying) {
      queuedToastList.add(new QueuedToast(text, toastType, defaultConfig));
      return;
    }

    isPlaying = true;

    var toastStage = buildToast(text, toastType);

    playToastAnimation(toastStage, defaultConfig);
  }

  private static Stage buildToast(String message, ToastType toastType) {
    final Stage toastStage = new Stage();

    toastStage.initOwner(primaryStage);
    toastStage.setResizable(false);
    toastStage.initStyle(StageStyle.TRANSPARENT);

    final ThemedText text = new ThemedText(message);
    final StackPane root = new StackPane(text);

    switch (toastType) {
      case ERROR:
        root.styleProperty().set("-fx-background-radius: 20; -fx-background-color: rgba(255, 0, 0, 0.4); -fx-padding: 24px;");
        break;
      case INFO:
        root.styleProperty().set("-fx-background-radius: 20; -fx-background-color: rgba(173, 216, 230, 0.4); -fx-padding: 24px;");
        break;
      case WARNING:
        root.styleProperty().set("-fx-background-radius: 20; -fx-background-color: rgba(255, 165, 0, 0.4); -fx-padding: 24px;");
        break;
    }

    final Scene scene = new Scene(root);
    scene.setFill(Color.TRANSPARENT);
    toastStage.setScene(scene);

    return toastStage;
  }

  private static void playToastAnimation(Stage toastStage, AnimationConfig config) {
    int fadeInDelay = config.fadeInDelay();
    int toastDuration = config.duration();
    int fadeOutDelay = config.fadeOutDelay();

    var inTransition = new FadeTransition(new Duration(fadeInDelay), toastStage.getScene().getRoot());
    inTransition.setFromValue(0.0);
    inTransition.setToValue(1);

    var outTransition = new FadeTransition(new Duration(fadeOutDelay), toastStage.getScene().getRoot());
    outTransition.setFromValue(1.0);
    outTransition.setToValue(0);

    var pauseTransition = new PauseTransition(new Duration(toastDuration));

    var sequentialTransition = new SequentialTransition(inTransition, pauseTransition, outTransition);

    sequentialTransition.setOnFinished(event -> {
      toastStage.close();

      if (queuedToastList.isEmpty()) {
        isPlaying = false;
      } else {
        var next = queuedToastList.remove(0);

        playToastAnimation(buildToast(next.text, next.toastType), next.config);
      }
    });

    toastStage.show();
    sequentialTransition.play();

    // position toast at top, center of window
    toastStage.setX(primaryStage.getX() + primaryStage.getWidth() / 2 - toastStage.getWidth() / 2);
    toastStage.setY(primaryStage.getY() + 4);
  }

  public record AnimationConfig(int fadeInDelay, int duration, int fadeOutDelay) {
  }

  private record QueuedToast(String text, ToastType toastType, AnimationConfig config) {
  }

  private enum ToastType {
    INFO,
    WARNING,
    ERROR
  }

  private Toast() {
  }
}
