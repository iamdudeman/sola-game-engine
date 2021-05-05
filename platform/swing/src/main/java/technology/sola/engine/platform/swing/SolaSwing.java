package technology.sola.engine.platform.swing;

import technology.sola.engine.core.AbstractSola;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

// TODO figure out insets rendering issue

public abstract class SolaSwing extends AbstractSola {
  private String title;
  private BufferedImage bufferedImage;
  private JFrame jFrame;

  protected SolaSwing(String title, int width, int height, int updatesPerSecond) {
    this.title = title;
    config(width, height, updatesPerSecond, true);
  }

  public void show() {
    jFrame = new JFrame();

    bufferedImage = new BufferedImage(rendererWidth, rendererHeight, BufferedImage.TYPE_INT_ARGB);

    jFrame.setPreferredSize(new Dimension(rendererWidth, rendererHeight));

    jFrame.pack();

    jFrame.createBufferStrategy(2);
    jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jFrame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        stop();
      }
    });
    jFrame.setTitle(title);
    jFrame.setVisible(true);

//    start();
  }

  @Override
  protected void onRender() {
    renderer.render(pixels -> {
      Graphics graphics = jFrame.getBufferStrategy().getDrawGraphics();

      bufferedImage.setRGB(0, 0, rendererWidth, rendererHeight, pixels, 0, rendererWidth);
      graphics.drawImage(bufferedImage, 0, 0, null);
      graphics.dispose();

      jFrame.getBufferStrategy().show();
    });
  }
}
