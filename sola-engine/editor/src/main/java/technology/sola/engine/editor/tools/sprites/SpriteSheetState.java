package technology.sola.engine.editor.tools.sprites;

import technology.sola.engine.assets.graphics.spritesheet.SpriteSheetInfo;
import technology.sola.engine.assets.graphics.spritesheet.SpriteSheetInfoJsonMapper;
import technology.sola.engine.editor.core.utils.FileUtils;
import technology.sola.engine.editor.core.utils.ToastService;
import technology.sola.logging.SolaLogger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class SpriteSheetState {
  private static final SolaLogger LOGGER = SolaLogger.of(SpriteSheetState.class, "logs/sola-editor.log");
  private final List<Consumer<SpriteSheetInfo>> spriteSheetInfoConsumers = new ArrayList<>();
  private SpriteSheetInfo spriteSheetInfo;
  private File spriteSheetFile;
  private File spriteSheetImageFile;

  SpriteSheetInfo getCurrentSpriteSheetInfo() {
    return spriteSheetInfo;
  }

  File getSpriteSheetImageFile() {
    return spriteSheetImageFile;
  }

  void setCurrentSpriteFile(File file) {
    this.spriteSheetFile = file;
  }

  void setCurrentSpriteSheetImageFile(File file) {
    this.spriteSheetImageFile = file;
  }

  void setCurrentSpriteSheetWithoutSave(SpriteSheetInfo spriteSheetInfo) {
    this.spriteSheetInfo = spriteSheetInfo;

    spriteSheetInfoConsumers.forEach(listener -> listener.accept(spriteSheetInfo));
  }

  void setCurrentSpriteSheetInfo(SpriteSheetInfo spriteSheetInfo) {
    setCurrentSpriteSheetWithoutSave(spriteSheetInfo);

    if (spriteSheetFile != null) {
      try {
        FileUtils.writeJson(spriteSheetFile, new SpriteSheetInfoJsonMapper().toJson(spriteSheetInfo));
      } catch (IOException ex) {
        ToastService.error(ex.getMessage());
        LOGGER.error(ex.getMessage(), ex);
      }
    }
  }

  void addListener(Consumer<SpriteSheetInfo> spriteSheetInfoConsumer) {
    this.spriteSheetInfoConsumers.add(spriteSheetInfoConsumer);
  }

  void removeListener(Consumer<SpriteSheetInfo> spriteSheetInfoConsumer) {
    this.spriteSheetInfoConsumers.remove(spriteSheetInfoConsumer);
  }
}
