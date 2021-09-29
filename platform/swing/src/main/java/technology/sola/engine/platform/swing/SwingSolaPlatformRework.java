package technology.sola.engine.platform.swing;

import technology.sola.engine.core.rework.AbstractSolaPlatformRework;
import technology.sola.engine.core.rework.AbstractSolaRework;
import technology.sola.engine.core.rework.SolaConfiguration;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.screen.AspectRatioSizing;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.function.Consumer;

public class SwingSolaPlatformRework extends AbstractSolaPlatformRework {
  private BufferedImage bufferedImage;
  private JFrame jFrame;
  private Canvas canvas;

  @Override
  public void onKeyPressed(Consumer<KeyEvent> keyEventConsumer) {
    canvas.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(java.awt.event.KeyEvent e) {
        keyEventConsumer.accept(new technology.sola.engine.input.KeyEvent(e.getKeyCode()));
      }
    });
  }

  @Override
  public void onKeyReleased(Consumer<KeyEvent> keyEventConsumer) {
    canvas.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(java.awt.event.KeyEvent e) {
        keyEventConsumer.accept(new technology.sola.engine.input.KeyEvent(e.getKeyCode()));
      }
    });
  }

  @Override
  public void onMouseMoved(Consumer<MouseEvent> mouseEventConsumer) {
    canvas.addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseMoved(java.awt.event.MouseEvent e) {
        mouseEventConsumer.accept(new technology.sola.engine.input.MouseEvent(e.getButton(), e.getX(), e.getY()));
      }
    });
  }

  @Override
  public void onMousePressed(Consumer<MouseEvent> mouseEventConsumer) {
    canvas.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(java.awt.event.MouseEvent e) {
        mouseEventConsumer.accept(new technology.sola.engine.input.MouseEvent(e.getButton(), e.getX(), e.getY()));
      }
    });
  }

  @Override
  public void onMouseReleased(Consumer<MouseEvent> mouseEventConsumer) {
    canvas.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(java.awt.event.MouseEvent e) {
        mouseEventConsumer.accept(new technology.sola.engine.input.MouseEvent(e.getButton(), e.getX(), e.getY()));
      }
    });
  }

  @Override
  protected void onInit(AbstractSolaRework abstractSolaRework, SolaConfiguration solaConfiguration, Runnable initCompleteCallback) {
    // TODO AssetPool stuff (probably will need new file system work)

    jFrame = new JFrame();
    canvas = new Canvas();

    bufferedImage = new BufferedImage(solaConfiguration.getCanvasWidth(), solaConfiguration.getCanvasHeight(), BufferedImage.TYPE_INT_ARGB);
    canvas.setPreferredSize(new Dimension(solaConfiguration.getCanvasWidth(), solaConfiguration.getCanvasHeight()));

    jFrame.getContentPane().add(canvas);

    jFrame.pack();

    canvas.createBufferStrategy(2);
    jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jFrame.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        viewport.resize(e.getComponent().getWidth(), e.getComponent().getHeight());
      }
    });
    jFrame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        // todo
//        abstractSolaRework.eventHub.emit(GameLoopEvent.STOP);
      }
    });
    jFrame.setTitle(solaConfiguration.getSolaTitle());

    canvas.requestFocus();
    jFrame.setVisible(true);
    initCompleteCallback.run();
  }

  @Override
  protected void beforeRender(Renderer renderer) {
    // Nothing to do here
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.render(pixels -> {
      int[] bufferedImageDataBuffer = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
      System.arraycopy(pixels, 0, bufferedImageDataBuffer, 0, pixels.length);

      Graphics graphics = canvas.getBufferStrategy().getDrawGraphics();

      AspectRatioSizing aspectRatioSizing = viewport.getAspectRatioSizing();

      graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
      graphics.drawImage(bufferedImage, aspectRatioSizing.getX(), aspectRatioSizing.getY(), aspectRatioSizing.getWidth(), aspectRatioSizing.getHeight(), null);
      graphics.dispose();

      canvas.getBufferStrategy().show();
    });
  }
}
