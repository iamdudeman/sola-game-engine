package technology.sola.engine.graphics.gui;

// TODO study https://github.com/iamdudeman/sol2d/blob/master/src/main/java/me/solhub/sol2d/core/gamegui/GameGuiPanel.java

import java.util.List;

public class GuiScene {
  private List<GameGuiComponentLocationDescription> gameGuiComponentLocationDescriptions = null;
  private List<GameGuiComponentPosition> gameGuiComponentPositions = null;


  private class GameGuiComponentLocationDescription {
    private AbstractGuiElement gameGuiComponent = null;
    private GuiElementAnchor anchor = null;
    private Double percentPositionX = null;
    private Double percentPositionY = null;

    GameGuiComponentLocationDescription(AbstractGuiElement gameGuiComponent, GuiElementAnchor anchor, Double percentPositionX, Double percentPositionY) {
      this.gameGuiComponent = gameGuiComponent;
      this.anchor = anchor;
      this.percentPositionX = percentPositionX;
      this.percentPositionY = percentPositionY;
    }
  }

  private class GameGuiComponentPosition {
    private AbstractGuiElement gameGuiComponent = null;
    private Integer x = null;
    private Integer y = null;

    GameGuiComponentPosition(AbstractGuiElement gameGuiComponent, Integer x, Integer y) {
      this.gameGuiComponent = gameGuiComponent;
      this.x = x;
      this.y = y;
    }
  }
}
