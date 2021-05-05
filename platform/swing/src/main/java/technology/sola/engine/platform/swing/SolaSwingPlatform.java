package technology.sola.engine.platform.swing;

import technology.sola.engine.assets.AssetLoader;
import technology.sola.engine.core.SolaPlatform;
import technology.sola.engine.graphics.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class SolaSwingPlatform implements SolaPlatform {
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
        // TODO somehow call AbstractSola#stop instead
        System.exit(0);
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
      Graphics graphics = jFrame.getBufferStrategy().getDrawGraphics();

      bufferedImage.setRGB(0, 0, rendererWidth, rendererHeight, pixels, 0, rendererWidth);
      graphics.drawImage(bufferedImage, 0, 0, null);
      graphics.dispose();

      jFrame.getBufferStrategy().show();
    });
  }
}
