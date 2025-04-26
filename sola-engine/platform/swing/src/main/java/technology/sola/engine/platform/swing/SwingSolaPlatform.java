package technology.sola.engine.platform.swing;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.assets.graphics.font.FontAssetLoader;
import technology.sola.engine.assets.graphics.SpriteSheetAssetLoader;
import technology.sola.engine.assets.input.ControlsConfigAssetLoader;
import technology.sola.engine.assets.json.JsonElementAsset;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.SolaPlatformIdentifier;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopState;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.renderer.SoftwareRenderer;
import technology.sola.engine.graphics.screen.AspectRatioSizing;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.platform.swing.assets.SwingPathUtils;
import technology.sola.engine.platform.swing.assets.audio.SwingAudioClipAssetLoader;
import technology.sola.engine.platform.swing.assets.SwingJsonAssetLoader;
import technology.sola.engine.platform.swing.assets.graphics.SwingSolaImageAssetLoader;
import technology.sola.engine.platform.swing.core.Graphics2dRenderer;
import technology.sola.engine.platform.swing.core.SwingRestClient;
import technology.sola.engine.platform.swing.core.SwingSocketClient;
import technology.sola.logging.SolaLogger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * SwingSolaPlatform is a {@link SolaPlatform} implementation for running a {@link technology.sola.engine.core.Sola} in
 * a Swing powered window.
 */
public class SwingSolaPlatform extends SolaPlatform {
  private static final SolaLogger LOGGER = SolaLogger.of(SwingSolaPlatform.class);
  private final boolean useSoftwareRendering;
  private final Dimension initialWindowSize;
  private Canvas canvas;
  private Consumer<Renderer> beforeRender;
  private Consumer<Renderer> onRender;

  // For Graphics2d rendering
  private Graphics2D graphics2D;

  /**
   * Creates a SwingSolaPlatform instance with default {@link SwingSolaPlatformConfig}.
   */
  public SwingSolaPlatform() {
    this(new SwingSolaPlatformConfig());
  }

  /**
   * Creates a SwingSolaPlatformConfig instance with desired configuration.
   *
   * @param platformConfig the {@link SwingSolaPlatformConfig}
   */
  public SwingSolaPlatform(SwingSolaPlatformConfig platformConfig) {
    this.useSoftwareRendering = platformConfig.useSoftwareRendering();
    this.initialWindowSize = platformConfig.initialWindowSize();

    socketClient = new SwingSocketClient();
    restClient = new SwingRestClient();
  }

  @Override
  public SolaPlatformIdentifier getIdentifier() {
    return SolaPlatformIdentifier.SWING;
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
      public void mouseMoved(java.awt.event.MouseEvent mouseEvent) {
        mouseEventConsumer.accept(swingToSola(mouseEvent));
      }
    });
  }

  @Override
  public void onMousePressed(Consumer<MouseEvent> mouseEventConsumer) {
    canvas.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
        mouseEventConsumer.accept(swingToSola(mouseEvent));
      }
    });
  }

  @Override
  public void onMouseReleased(Consumer<MouseEvent> mouseEventConsumer) {
    canvas.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
        mouseEventConsumer.accept(swingToSola(mouseEvent));
      }
    });
  }

  @Override
  public void onMouseWheel(Consumer<technology.sola.engine.input.MouseWheelEvent> mouseWheelEventConsumer) {
    canvas.addMouseWheelListener(event -> {
      boolean isHorizontal = event.isShiftDown();

      boolean isUp = !isHorizontal && event.getWheelRotation() < 0;
      boolean isDown = !isHorizontal && event.getWheelRotation() > 0;
      boolean isLeft = isHorizontal && event.getWheelRotation() > 0;
      boolean isRight = isHorizontal && event.getWheelRotation() < 0;

      mouseWheelEventConsumer.accept(new technology.sola.engine.input.MouseWheelEvent(isUp, isDown, isLeft, isRight));
    });
  }

  @Override
  protected void initializePlatform(SolaConfiguration solaConfiguration, SolaPlatformInitialization solaPlatformInitialization) {
    JFrame jFrame = new JFrame();
    canvas = new Canvas();
    canvas.setPreferredSize(new Dimension(solaConfiguration.rendererWidth(), solaConfiguration.rendererHeight()));
    jFrame.getContentPane().add(canvas);
    if (initialWindowSize != null) {
      jFrame.setPreferredSize(initialWindowSize);
    }
    jFrame.pack();
    if (initialWindowSize != null) {
      viewport.resize(canvas.getWidth(), canvas.getHeight());
    }

    canvas.createBufferStrategy(2);
    if (useSoftwareRendering) {
      setupSoftwareRendering(solaConfiguration);
    } else {
      setupGraphics2dRendering();
    }

    solaEventHub.add(GameLoopEvent.class, event -> {
      if (event.state() == GameLoopState.STOPPED) {
        socketClient.disconnect();
        jFrame.dispose();
      }
    });

    jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    canvas.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        viewport.resize(e.getComponent().getWidth(), e.getComponent().getHeight());
      }
    });
    jFrame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        solaEventHub.emit(new GameLoopEvent(GameLoopState.STOP));
      }
    });
    jFrame.addWindowStateListener(e -> {
      if (e.getNewState() == Frame.ICONIFIED) {
        solaEventHub.emit(new GameLoopEvent(GameLoopState.PAUSE));
      } else if (e.getNewState() != Frame.ICONIFIED) {
        solaEventHub.emit(new GameLoopEvent(GameLoopState.RESUME));
      }
    });
    jFrame.setTitle(solaConfiguration.title());
    jFrame.setLocationRelativeTo(null);

    canvas.requestFocus();
    jFrame.setVisible(true);

    setApplicationIcon(jFrame);

    solaPlatformInitialization.finish();
  }

  @Override
  protected void beforeRender(Renderer renderer) {
    beforeRender.accept(renderer);
  }

  @Override
  protected void onRender(Renderer renderer) {
    onRender.accept(renderer);
  }

  @Override
  protected void populateAssetLoaderProvider(AssetLoaderProvider assetLoaderProvider) {
    AssetLoader<SolaImage> solaImageAssetLoader = new SwingSolaImageAssetLoader();
    AssetLoader<JsonElementAsset> jsonElementAssetAssetLoader = new SwingJsonAssetLoader();

    assetLoaderProvider.add(solaImageAssetLoader);
    assetLoaderProvider.add(jsonElementAssetAssetLoader);
    assetLoaderProvider.add(new SwingAudioClipAssetLoader());

    assetLoaderProvider.add(new FontAssetLoader(
      jsonElementAssetAssetLoader, solaImageAssetLoader
    ));
    assetLoaderProvider.add(new SpriteSheetAssetLoader(
      jsonElementAssetAssetLoader, solaImageAssetLoader
    ));
    assetLoaderProvider.add(new ControlsConfigAssetLoader(
      jsonElementAssetAssetLoader
    ));
  }

  @Override
  protected Renderer buildRenderer(SolaConfiguration solaConfiguration) {
    LOGGER.info("Using %s rendering", useSoftwareRendering ? "Software" : "Graphics2D");

    return useSoftwareRendering
      ? super.buildRenderer(solaConfiguration)
      : new Graphics2dRenderer(graphics2D, solaConfiguration.rendererWidth(), solaConfiguration.rendererHeight());
  }

  private void setupSoftwareRendering(SolaConfiguration solaConfiguration) {
    BufferedImage bufferedImage = new BufferedImage(
      solaConfiguration.rendererWidth(), solaConfiguration.rendererHeight(), BufferedImage.TYPE_INT_ARGB
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
      graphics.drawImage(bufferedImage, aspectRatioSizing.x(), aspectRatioSizing.y(), aspectRatioSizing.width(), aspectRatioSizing.height(), null);
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
      graphics2D.translate(aspectRatioSizing.x(), aspectRatioSizing.y());
      graphics2D.scale(aspectRatioSizing.width() / (double) renderer.getWidth(), aspectRatioSizing.height() / (double) renderer.getHeight());
    };

    onRender = renderer -> {
      canvas.getBufferStrategy().show();
      graphics2D.dispose();
    };
  }

  private MouseEvent swingToSola(java.awt.event.MouseEvent swingMouseEvent) {
    MouseCoordinate adjusted = adjustMouseForViewport(swingMouseEvent.getX(), swingMouseEvent.getY());

    return new MouseEvent(swingMouseEvent.getButton(), adjusted.x(), adjusted.y());
  }

  private void setApplicationIcon(JFrame jFrame) {
    try {
      var url = SwingPathUtils.asUrl("assets/icon.jpg");

      if (url == null) {
        url = SwingPathUtils.asUrl("assets/icon.png");
      }

      if (url == null) {
        LOGGER.warning("Icon not found");
      } else {
        jFrame.setIconImage(new ImageIcon(url).getImage());
      }
    } catch (IOException ex) {
      LOGGER.error("Failed to load icon", ex);
    }
  }
}
