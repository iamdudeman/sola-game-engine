package technology.sola.engine.platform.swing;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.core.AbstractSolaPlatform;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.screen.AspectRatioSizing;
import technology.sola.engine.platform.swing.assets.FontAssetPool;
import technology.sola.engine.platform.swing.assets.SolaImageAssetPool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class SwingSolaPlatform extends AbstractSolaPlatform {
  private String title;
  private BufferedImage bufferedImage;
  private JFrame jFrame;
  private Canvas canvas;

  public SwingSolaPlatform(String title) {
    this.title = title;
  }

  @Override
  public void init() {
    AssetPool<SolaImage> solaImageAssetPool = new SolaImageAssetPool();
    assetPoolProvider.addAssetPool(solaImageAssetPool);
    assetPoolProvider.addAssetPool(new FontAssetPool(solaImageAssetPool));

    jFrame = new JFrame();
    canvas = new Canvas();

    bufferedImage = new BufferedImage(abstractSola.getRendererWidth(), abstractSola.getRendererHeight(), BufferedImage.TYPE_INT_ARGB);

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
    canvas.addMouseMotionListener(new MouseMotionAdapter() {
      @Override
      public void mouseMoved(MouseEvent e) {
        onMouseMoved(new technology.sola.engine.input.MouseEvent(e.getButton(), e.getX(), e.getY()));
      }
    });
    canvas.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        onMousePressed(new technology.sola.engine.input.MouseEvent(e.getButton(), e.getX(), e.getY()));
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        onMouseReleased(new technology.sola.engine.input.MouseEvent(e.getButton(), e.getX(), e.getY()));
      }
    });
    canvas.setPreferredSize(new Dimension(abstractSola.getRendererWidth(), abstractSola.getRendererHeight()));

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
    renderer.renderLayers();
    int[] bufferedImageDataBuffer = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
    System.arraycopy(renderer.getPixels(), 0, bufferedImageDataBuffer, 0, renderer.getPixels().length);

    Graphics graphics = canvas.getBufferStrategy().getDrawGraphics();

    AspectRatioSizing aspectRatioSizing = viewport.getAspectRatioSizing();

    graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    graphics.drawImage(bufferedImage, aspectRatioSizing.getX(), aspectRatioSizing.getY(), aspectRatioSizing.getWidth(), aspectRatioSizing.getHeight(), null);
    graphics.dispose();

    canvas.getBufferStrategy().show();
  }
}
