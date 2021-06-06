package technology.sola.engine.tools.executable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FontRasterizerExecutable implements ToolExecutable {
  @Override
  public void execute(String[] toolArgs) {
    Font font = new Font("monospaced", Font.PLAIN, 12);

    BufferedImage bufferedImage = new BufferedImage(100, 80, BufferedImage.TYPE_INT_ARGB);

    Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();


    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, 100, 80);


    graphics.setColor(Color.BLACK);
    graphics.setFont(font);
//    graphics.drawString("ABCDEFG", 0, 0);

    FontMetrics fontMetrics = graphics.getFontMetrics();

    String[] characters = new String[] {
      "a", "b", "c", "d"
    };
    int widthCounter = 0;
    int heightCounter = 0;
    final int margin = 1;

    for (String character : characters) {
      Rectangle2D rectangle = fontMetrics.getStringBounds(character, graphics);

      heightCounter += rectangle.getHeight() + margin;

      graphics.drawString(character, widthCounter, heightCounter);

      widthCounter += rectangle.getWidth() + margin;
    }

//    graphics.drawString("ABCDEFG", 0, fontMetrics.getHeight());
//    graphics.drawString("abcdefg", 0, fontMetrics.getHeight() * 2);

    graphics.dispose();

    File file = new File("font_test.png");

    try {
      ImageIO.write(bufferedImage, "png", file);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
