package technology.sola.engine.platform.swing;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.assets.AssetLoaderProvider;
import technology.sola.engine.assets.graphics.SolaImage;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.core.event.GameLoopEventType;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.renderer.SoftwareRenderer;
import technology.sola.engine.graphics.screen.AspectRatioSizing;
import technology.sola.engine.input.KeyEvent;
import technology.sola.engine.input.MouseEvent;
import technology.sola.engine.platform.swing.assets.SwingAudiClipAssetLoader;
import technology.sola.engine.platform.swing.assets.SwingFontAssetLoader;
import technology.sola.engine.platform.swing.assets.SwingSolaImageAssetLoader;
import technology.sola.engine.platform.swing.assets.SpriteSheetAssetLoader;
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

public class SwingSolaPlatform extends SolaPlatform {
  private final boolean useSoftwareRendering;
  private Canvas canvas;
  private Consumer<Renderer> beforeRender;
  private Consumer<Renderer> onRender;

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
  protected void initializePlatform(SolaConfiguration solaConfiguration, SolaPlatformInitialization solaPlatformInitialization) {
    JFrame jFrame = new JFrame();
    canvas = new Canvas();
    canvas.setPreferredSize(new Dimension(solaConfiguration.canvasWidth(), solaConfiguration.canvasHeight()));
    jFrame.getContentPane().add(canvas);
    jFrame.pack();

    canvas.createBufferStrategy(2);
    if (useSoftwareRendering) {
      setupSoftwareRendering(solaConfiguration);
    } else {
      setupGraphics2dRendering();
    }

    solaEventHub.add(event -> {
      if (event.getMessage() == GameLoopEventType.STOPPED) {
        jFrame.dispose();
      }
    }, GameLoopEvent.class);

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
        solaEventHub.emit(GameLoopEvent.STOP);
      }
    });
    jFrame.setTitle(solaConfiguration.solaTitle());

    canvas.requestFocus();
    jFrame.setVisible(true);

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
    assetLoaderProvider.add(solaImageAssetLoader);
    assetLoaderProvider.add(new SwingFontAssetLoader(solaImageAssetLoader));
    assetLoaderProvider.add(new SpriteSheetAssetLoader(solaImageAssetLoader));
    assetLoaderProvider.add(new SwingAudiClipAssetLoader());
  }

  @Override
  protected Renderer buildRenderer(SolaConfiguration solaConfiguration) {
    LOGGER.info("Using {} rendering", useSoftwareRendering ? "Software" : "Graphics2D");

    return useSoftwareRendering
      ? super.buildRenderer(solaConfiguration)
      : new Graphics2dRenderer(graphics2D, solaConfiguration.canvasWidth(), solaConfiguration.canvasHeight());
  }

  private void setupSoftwareRendering(SolaConfiguration solaConfiguration) {
    BufferedImage bufferedImage = new BufferedImage(
      solaConfiguration.canvasWidth(), solaConfiguration.canvasHeight(), BufferedImage.TYPE_INT_ARGB
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
}
