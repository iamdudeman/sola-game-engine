package technology.sola.engine.platform.swing;

import technology.sola.engine.core.AbstractSolaPlatform;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.graphics.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class SolaSwingPlatform extends AbstractSolaPlatform {
  private final int rendererWidth;
  private final int rendererHeight;
  private String title;
  private BufferedImage bufferedImage;
  private JFrame jFrame;
  private Canvas canvas;

  public SolaSwingPlatform(String title, int rendererWidth, int rendererHeight) {
    this.title = title;
    this.rendererWidth = rendererWidth;
    this.rendererHeight = rendererHeight;
  }

  @Override
  public void init() {
    assetLoader.addAssetMapper(new SolaImageAssetMapper());

    jFrame = new JFrame();
    canvas = new Canvas();

    bufferedImage = new BufferedImage(rendererWidth, rendererHeight, BufferedImage.TYPE_INT_ARGB);

    canvas.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        onKeyPressed(new technology.sola.engine.input.KeyEvent(e.getKeyCode()));
      }

      @Override
      public void keyReleased(KeyEvent e) {
        onKeyReleased(new technology.sola.engine.input.KeyEvent(e.getKeyCode()));
      }
    });
    canvas.setPreferredSize(new Dimension(rendererWidth, rendererHeight));

    jFrame.getContentPane().add(canvas);

    jFrame.pack();

    canvas.createBufferStrategy(2);
    jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jFrame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        eventHub.emit(GameLoopEvent.STOP);
      }
    });
    jFrame.setTitle(title);

    canvas.requestFocus();
  }

  @Override
  public void start() {
    jFrame.setVisible(true);
  }

  @Override
  public void render(Renderer renderer) {
    renderer.render(pixels -> {
      int[] bufferedImageDataBuffer = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
      System.arraycopy(pixels, 0, bufferedImageDataBuffer, 0, pixels.length);

      Graphics graphics = canvas.getBufferStrategy().getDrawGraphics();

      graphics.drawImage(bufferedImage, 0, 0, null);
      graphics.dispose();

      canvas.getBufferStrategy().show();
    });
  }
}
