package technology.sola.engine.graphics.font;

import technology.sola.engine.graphics.SolaImage;

import java.util.ArrayList;
import java.util.List;

public class DefaultFont {
  public static Font createDefaultFont(SolaImage fontImage) {
    return Font.createFont(fontImage, getFontInfo());
  }

  private static FontInfo getFontInfo() {
    List<FontGlyph> glyphList = new ArrayList<>();

    glyphList.add(new FontGlyph(' ', 0, 19, 11, 19));
    glyphList.add(new FontGlyph('!', 12, 19, 11, 19));
    glyphList.add(new FontGlyph('"', 24, 19, 11, 19));
    glyphList.add(new FontGlyph('#', 36, 19, 11, 19));
    glyphList.add(new FontGlyph('$', 48, 19, 11, 19));
    glyphList.add(new FontGlyph('%', 60, 19, 11, 19));
    glyphList.add(new FontGlyph('&', 72, 19, 11, 19));
    glyphList.add(new FontGlyph('\'', 84, 19, 11, 19));
    glyphList.add(new FontGlyph('(', 96, 19, 11, 19));
    glyphList.add(new FontGlyph(')', 108, 19, 11, 19));
    glyphList.add(new FontGlyph('*', 120, 19, 11, 19));
    glyphList.add(new FontGlyph('+', 132, 19, 11, 19));
    glyphList.add(new FontGlyph(',', 144, 19, 11, 19));
    glyphList.add(new FontGlyph('-', 156, 19, 11, 19));
    glyphList.add(new FontGlyph('.', 168, 19, 11, 19));
    glyphList.add(new FontGlyph('/', 0, 38, 11, 19));
    glyphList.add(new FontGlyph('0', 12, 38, 11, 19));
    glyphList.add(new FontGlyph('1', 24, 38, 11, 19));
    glyphList.add(new FontGlyph('2', 36, 38, 11, 19));
    glyphList.add(new FontGlyph('3', 48, 38, 11, 19));
    glyphList.add(new FontGlyph('4', 60, 38, 11, 19));
    glyphList.add(new FontGlyph('5', 72, 38, 11, 19));
    glyphList.add(new FontGlyph('6', 84, 38, 11, 19));
    glyphList.add(new FontGlyph('7', 96, 38, 11, 19));
    glyphList.add(new FontGlyph('8', 108, 38, 11, 19));
    glyphList.add(new FontGlyph('9', 120, 38, 11, 19));
    glyphList.add(new FontGlyph(':', 132, 38, 11, 19));
    glyphList.add(new FontGlyph(';', 144, 38, 11, 19));
    glyphList.add(new FontGlyph('<', 156, 38, 11, 19));
    glyphList.add(new FontGlyph('=', 168, 38, 11, 19));
    glyphList.add(new FontGlyph('>', 0, 57, 11, 19));
    glyphList.add(new FontGlyph('?', 12, 57, 11, 19));
    glyphList.add(new FontGlyph('@', 24, 57, 11, 19));
    glyphList.add(new FontGlyph('A', 36, 57, 11, 19));
    glyphList.add(new FontGlyph('B', 48, 57, 11, 19));
    glyphList.add(new FontGlyph('C', 60, 57, 11, 19));
    glyphList.add(new FontGlyph('D', 72, 57, 11, 19));
    glyphList.add(new FontGlyph('E', 84, 57, 11, 19));
    glyphList.add(new FontGlyph('F', 96, 57, 11, 19));
    glyphList.add(new FontGlyph('G', 108, 57, 11, 19));
    glyphList.add(new FontGlyph('H', 120, 57, 11, 19));
    glyphList.add(new FontGlyph('I', 132, 57, 11, 19));
    glyphList.add(new FontGlyph('J', 144, 57, 11, 19));
    glyphList.add(new FontGlyph('K', 156, 57, 11, 19));
    glyphList.add(new FontGlyph('L', 168, 57, 11, 19));
    glyphList.add(new FontGlyph('M', 0, 76, 11, 19));
    glyphList.add(new FontGlyph('N', 12, 76, 11, 19));
    glyphList.add(new FontGlyph('O', 24, 76, 11, 19));
    glyphList.add(new FontGlyph('P', 36, 76, 11, 19));
    glyphList.add(new FontGlyph('Q', 48, 76, 11, 19));
    glyphList.add(new FontGlyph('R', 60, 76, 11, 19));
    glyphList.add(new FontGlyph('S', 72, 76, 11, 19));
    glyphList.add(new FontGlyph('T', 84, 76, 11, 19));
    glyphList.add(new FontGlyph('U', 96, 76, 11, 19));
    glyphList.add(new FontGlyph('V', 108, 76, 11, 19));
    glyphList.add(new FontGlyph('W', 120, 76, 11, 19));
    glyphList.add(new FontGlyph('X', 132, 76, 11, 19));
    glyphList.add(new FontGlyph('Y', 144, 76, 11, 19));
    glyphList.add(new FontGlyph('Z', 156, 76, 11, 19));
    glyphList.add(new FontGlyph('[', 168, 76, 11, 19));
    glyphList.add(new FontGlyph('\\', 0, 95, 11, 19));
    glyphList.add(new FontGlyph(']', 12, 95, 11, 19));
    glyphList.add(new FontGlyph('^', 24, 95, 11, 19));
    glyphList.add(new FontGlyph('_', 36, 95, 11, 19));
    glyphList.add(new FontGlyph('`', 48, 95, 11, 19));
    glyphList.add(new FontGlyph('a', 60, 95, 11, 19));
    glyphList.add(new FontGlyph('b', 72, 95, 11, 19));
    glyphList.add(new FontGlyph('c', 84, 95, 11, 19));
    glyphList.add(new FontGlyph('d', 96, 95, 11, 19));
    glyphList.add(new FontGlyph('e', 108, 95, 11, 19));
    glyphList.add(new FontGlyph('f', 120, 95, 11, 19));
    glyphList.add(new FontGlyph('g', 132, 95, 11, 19));
    glyphList.add(new FontGlyph('h', 144, 95, 11, 19));
    glyphList.add(new FontGlyph('i', 156, 95, 11, 19));
    glyphList.add(new FontGlyph('j', 168, 95, 11, 19));
    glyphList.add(new FontGlyph('k', 0, 114, 11, 19));
    glyphList.add(new FontGlyph('l', 12, 114, 11, 19));
    glyphList.add(new FontGlyph('m', 24, 114, 11, 19));
    glyphList.add(new FontGlyph('n', 36, 114, 11, 19));
    glyphList.add(new FontGlyph('o', 48, 114, 11, 19));
    glyphList.add(new FontGlyph('p', 60, 114, 11, 19));
    glyphList.add(new FontGlyph('q', 72, 114, 11, 19));
    glyphList.add(new FontGlyph('r', 84, 114, 11, 19));
    glyphList.add(new FontGlyph('s', 96, 114, 11, 19));
    glyphList.add(new FontGlyph('t', 108, 114, 11, 19));
    glyphList.add(new FontGlyph('u', 120, 114, 11, 19));
    glyphList.add(new FontGlyph('v', 132, 114, 11, 19));
    glyphList.add(new FontGlyph('w', 144, 114, 11, 19));
    glyphList.add(new FontGlyph('x', 156, 114, 11, 19));
    glyphList.add(new FontGlyph('y', 168, 114, 11, 19));
    glyphList.add(new FontGlyph('z', 0, 133, 11, 19));
    glyphList.add(new FontGlyph('{', 12, 133, 11, 19));
    glyphList.add(new FontGlyph('|', 24, 133, 11, 19));
    glyphList.add(new FontGlyph('}', 36, 133, 11, 19));
    glyphList.add(new FontGlyph('~', 48, 133, 11, 19));

    return new FontInfo("default", 18, FontStyle.NORMAL, glyphList);
  }
}
