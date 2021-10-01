package technology.sola.engine.platform.swing;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.core.rework.AbstractSolaPlatformRework;
import technology.sola.engine.core.rework.AbstractSolaRework;
import technology.sola.engine.core.rework.SolaConfiguration;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.screen.AspectRatioSizing;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.platform.swing.assets.FontAssetPool;
import technology.sola.engine.platform.swing.assets.SolaImageAssetPool;
import technology.sola.engine.platform.swing.core.SwingRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

public class SwingSolaPlatformReworkSwingRenderer extends AbstractSolaPlatformRework {
  private JFrame jFrame;
  private Canvas canvas;
  private Graphics2D graphics2D;

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
  protected void initializePlatform(AbstractSolaRework abstractSolaRework, SolaConfiguration solaConfiguration, Runnable initCompleteCallback) {
    jFrame = new JFrame();
    canvas = new Canvas();

    canvas.setPreferredSize(new Dimension(solaConfiguration.getCanvasWidth(), solaConfiguration.getCanvasHeight()));

    jFrame.getContentPane().add(canvas);

    jFrame.pack();

    canvas.createBufferStrategy(2);
    graphics2D = (Graphics2D) canvas.getGraphics();
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
        solaEventHub.emit(GameLoopEvent.STOP);
      }
    });
    jFrame.setTitle(solaConfiguration.getSolaTitle());

    canvas.requestFocus();
    jFrame.setVisible(true);

    // Note: Always run last
    initCompleteCallback.run();
  }

  @Override
  protected void beforeRender(Renderer renderer) {
    graphics2D = (Graphics2D) canvas.getBufferStrategy().getDrawGraphics();
    ((SwingRenderer) renderer).updateGraphics2D(graphics2D);
    graphics2D.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    AspectRatioSizing aspectRatioSizing = viewport.getAspectRatioSizing();
    graphics2D.translate(aspectRatioSizing.getX(), aspectRatioSizing.getY());
    graphics2D.scale(aspectRatioSizing.getWidth() / (double)renderer.getWidth(), aspectRatioSizing.getHeight() / (double)renderer.getHeight());

    // todo is this the right place for this?
    renderer.getLayers().forEach(layer -> layer.draw(renderer));
  }

  @Override
  protected void onRender(Renderer renderer) {
    canvas.getBufferStrategy().show();
    graphics2D.dispose();
  }

  @Override
  protected void populateAssetPoolProvider(AssetPoolProvider assetPoolProvider) {
    AssetPool<SolaImage> solaImageAssetPool = new SolaImageAssetPool();
    assetPoolProvider.addAssetPool(solaImageAssetPool);
    assetPoolProvider.addAssetPool(new FontAssetPool(solaImageAssetPool));
  }

  @Override
  protected Renderer buildRenderer(SolaConfiguration solaConfiguration) {
    return new SwingRenderer(graphics2D, solaConfiguration.getCanvasWidth(), solaConfiguration.getCanvasHeight());
  }
}
