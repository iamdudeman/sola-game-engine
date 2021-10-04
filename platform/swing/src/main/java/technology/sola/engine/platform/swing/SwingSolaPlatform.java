package technology.sola.engine.platform.swing;

import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.assets.AssetPoolProvider;
import technology.sola.engine.core.AbstractSolaPlatform;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.graphics.SolaImage;
import technology.sola.engine.graphics.impl.SoftwareRenderer;
import technology.sola.engine.graphics.screen.AspectRatioSizing;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.platform.swing.assets.FontAssetPool;
import technology.sola.engine.platform.swing.assets.SolaImageAssetPool;
import technology.sola.engine.platform.swing.core.Graphics2dRenderer;

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

public class SwingSolaPlatform extends AbstractSolaPlatform {
  private final boolean useSoftwareRendering;
  private Canvas canvas;
  private Consumer<technology.sola.engine.graphics.Renderer> beforeRender;
  private Consumer<technology.sola.engine.graphics.Renderer> onRender;

  // For graphics2d rendering
  private Graphics2D graphics2D;

  public SwingSolaPlatform() {
    this(true);
  }

  public SwingSolaPlatform(boolean useSoftwareRendering) {
    this.useSoftwareRendering = useSoftwareRendering;
  }

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
  protected void initializePlatform(SolaConfiguration solaConfiguration, SolaPlatformInitialization solaPlatformInitialization) {
    JFrame jFrame = new JFrame();
    canvas = new Canvas();
    canvas.setPreferredSize(new Dimension(solaConfiguration.getCanvasWidth(), solaConfiguration.getCanvasHeight()));
    jFrame.getContentPane().add(canvas);
    jFrame.pack();

    canvas.createBufferStrategy(2);
    if (useSoftwareRendering) {
      setupSoftwareRendering(solaConfiguration);
    } else {
      setupGraphics2dRendering();
    }

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

    solaPlatformInitialization.finish();
  }

  @Override
  protected void beforeRender(technology.sola.engine.graphics.Renderer renderer) {
    beforeRender.accept(renderer);
  }

  @Override
  protected void onRender(technology.sola.engine.graphics.Renderer renderer) {
    onRender.accept(renderer);
  }

  @Override
  protected void populateAssetPoolProvider(AssetPoolProvider assetPoolProvider) {
    AssetPool<SolaImage> solaImageAssetPool = new SolaImageAssetPool();
    assetPoolProvider.addAssetPool(solaImageAssetPool);
    assetPoolProvider.addAssetPool(new FontAssetPool(solaImageAssetPool));
  }

  @Override
  protected technology.sola.engine.graphics.Renderer buildRenderer(SolaConfiguration solaConfiguration) {
    LOGGER.info("Using {} rendering", useSoftwareRendering ? "Software" : "Graphics2D");

    return useSoftwareRendering
      ? super.buildRenderer(solaConfiguration)
      : new Graphics2dRenderer(graphics2D, solaConfiguration.getCanvasWidth(), solaConfiguration.getCanvasHeight());
  }

  private void setupSoftwareRendering(SolaConfiguration solaConfiguration) {
    BufferedImage bufferedImage = new BufferedImage(
      solaConfiguration.getCanvasWidth(), solaConfiguration.getCanvasHeight(), BufferedImage.TYPE_INT_ARGB
    );

    beforeRender = renderer -> {
    };

    onRender = renderer -> {
      // For software rendering
      int[] pixels = ((SoftwareRenderer) renderer).getPixels();
      int[] bufferedImageDataBuffer = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
      System.arraycopy(pixels, 0, bufferedImageDataBuffer, 0, pixels.length);

      Graphics graphics = canvas.getBufferStrategy().getDrawGraphics();

      AspectRatioSizing aspectRatioSizing = viewport.getAspectRatioSizing();

      graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
      graphics.drawImage(bufferedImage, aspectRatioSizing.getX(), aspectRatioSizing.getY(), aspectRatioSizing.getWidth(), aspectRatioSizing.getHeight(), null);
      graphics.dispose();

      canvas.getBufferStrategy().show();
    };
  }

  private void setupGraphics2dRendering() {
    graphics2D = (Graphics2D) canvas.getGraphics();

    beforeRender = renderer -> {
      graphics2D = (Graphics2D) canvas.getBufferStrategy().getDrawGraphics();
      ((Graphics2dRenderer) renderer).updateGraphics2D(graphics2D);
      graphics2D.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

      AspectRatioSizing aspectRatioSizing = viewport.getAspectRatioSizing();
      graphics2D.translate(aspectRatioSizing.getX(), aspectRatioSizing.getY());
      graphics2D.scale(aspectRatioSizing.getWidth() / (double) renderer.getWidth(), aspectRatioSizing.getHeight() / (double) renderer.getHeight());
    };

    onRender = renderer -> {
      canvas.getBufferStrategy().show();
      graphics2D.dispose();
    };
  }
}
