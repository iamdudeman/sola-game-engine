package technology.sola.engine.platform.swing;

import technology.sola.engine.graphics.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SwingContainer {
  private final String title;
  private final int width;
  private final int height;
  private final Renderer renderer;

  public SwingContainer(String title, int width, int height) {
    this.title = title;
    this.width = width;
    this.height = height;
    this.renderer = new Renderer(width, height);
  }

  public Renderer getRenderer() {
    return renderer;
  }

  public void show() {
    JFrame jFrame = new JFrame();

    RenderComponent renderComponent = new RenderComponent(renderer);

    renderComponent.setPreferredSize(new Dimension(width, height));

    jFrame.add(renderComponent);
    jFrame.pack();

    jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    jFrame.setTitle(title);
    jFrame.setVisible(true);
  }

  private class RenderComponent extends JComponent {
    private final transient Renderer renderer;

    public RenderComponent(Renderer renderer) {
      this.renderer = renderer;
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

      renderer.render(pixels -> {
        bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);
        g.drawImage(bufferedImage, 0, 0, null);
      });
    }
  }
}
