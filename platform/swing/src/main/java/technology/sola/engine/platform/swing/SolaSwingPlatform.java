package technology.sola.engine.platform.swing;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.core.AbstractSolaPlatform;
import technology.sola.engine.core.event.GameLoopEvent;
import technology.sola.engine.graphics.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

// TODO figure out insets rendering issue

public class SolaSwingPlatform extends AbstractSolaPlatform {
  private String title;
  private BufferedImage bufferedImage;
  private JFrame jFrame;
  private final int rendererWidth;
  private final int rendererHeight;

  public SolaSwingPlatform(String title, int rendererWidth, int rendererHeight) {
    this.title = title;
    this.rendererWidth = rendererWidth;
    this.rendererHeight = rendererHeight;
  }

  @Override
  public void init(AssetLoader assetLoader) {
    assetLoader.addAssetMapper(new SolaImageAssetMapper());

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
        eventHub.emit(GameLoopEvent.STOP);
      }
    });
    jFrame.setTitle(title);
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

      Graphics graphics = jFrame.getBufferStrategy().getDrawGraphics();

      graphics.drawImage(bufferedImage, 0, 0, null);
      graphics.dispose();

      jFrame.getBufferStrategy().show();
    });
  }
}
