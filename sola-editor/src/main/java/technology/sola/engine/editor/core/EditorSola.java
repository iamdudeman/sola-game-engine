package technology.sola.engine.editor.core;

import javafx.scene.control.MultipleSelectionModel;
import technology.sola.engine.assets.AssetPool;
import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.core.graphics.GraphicsUtils;
import technology.sola.engine.core.graphics.SolaGraphics;
import technology.sola.engine.core.physics.SolaPhysics;
import technology.sola.engine.ecs.EcsSystem;
import technology.sola.engine.ecs.Entity;
import technology.sola.engine.ecs.World;
import technology.sola.engine.event.gameloop.GameLoopEvent;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.Renderer;
import technology.sola.engine.graphics.components.CameraComponent;
import technology.sola.engine.graphics.components.CircleRendererComponent;
import technology.sola.engine.graphics.font.Font;
import technology.sola.engine.input.Key;
import technology.sola.engine.input.MouseButton;
import technology.sola.math.geometry.Rectangle;
import technology.sola.math.linear.Vector2D;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditorSola extends Sola {
  public static final String EDITOR_CAMERA_ENTITY_NAME = "editorCamera";
  private SolaGraphics solaGraphics;
  private final List<EcsSystem> previouslyActiveSystems = new ArrayList<>();
  private final MultipleSelectionModel<Entity> entitySelectionModel;
  private final SolaEditorContext solaEditorContext;
  private SolaConfiguration solaConfiguration;
  private String[] layers = new String[0];
  private boolean isLivePreview = false;

  public EditorSola(SolaConfiguration solaConfiguration, MultipleSelectionModel<Entity> entitySelectionModel, SolaEditorContext solaEditorContext) {
    this.solaConfiguration = solaConfiguration;
    this.entitySelectionModel = entitySelectionModel;
    this.solaEditorContext = solaEditorContext;
  }

  public void setWorld(World world) {
    ecsSystemContainer.setWorld(world);
  }

  public void setSolaConfiguration(SolaConfiguration solaConfiguration) {
    this.solaConfiguration = solaConfiguration;
  }

  public void setLayers(String[] layers) {
    this.layers = layers;
  }

  public void startPreview() {
    isLivePreview = true;

    previouslyActiveSystems.forEach(activeSystem -> {
      ecsSystemContainer.get(activeSystem.getClass()).setActive(true);
    });
    previouslyActiveSystems.clear();
  }

  public void stopPreview() {
    isLivePreview = false;
    ecsSystemContainer.activeSystemsIterator().forEachRemaining(activeSystem -> {
      previouslyActiveSystems.add(activeSystem);
      activeSystem.setActive(false);
    });
  }

  public void stop() {
    eventHub.emit(GameLoopEvent.STOP);
  }

  @Override
  protected SolaConfiguration getConfiguration() {
    return solaConfiguration;
  }

  @Override
  protected void onInit() {
    // TODO get assets based on project structure (might need this for all Sola [think VirtualFileSystem of sorts maybe])
    SolaPhysics.use(eventHub, ecsSystemContainer);
    solaGraphics = SolaGraphics.use(ecsSystemContainer, platform.getRenderer(), assetPoolProvider);

    stopPreview();

    platform.getRenderer().createLayers(layers);

    registerOnEntityClick();
    registerEditorCameraControls();
    populateFonts();
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear();

    solaGraphics.render();

    if (!isLivePreview) {
      drawSelectedBorder(renderer);
    }
  }

  private Rectangle getEntityBoundingBox(Entity entity, TransformComponent transformComponent) {
    CircleRendererComponent circleRendererComponent = entity.getComponent(CircleRendererComponent.class);

    Vector2D min = new Vector2D(transformComponent.getX(), transformComponent.getY());
    Vector2D widthHeight = new Vector2D(transformComponent.getScaleX(), transformComponent.getScaleY());

    if (circleRendererComponent != null) {
      float max = Math.max(transformComponent.getScaleX(), transformComponent.getScaleY());
      widthHeight = new Vector2D(max, max);
    }

    return new Rectangle(min, min.add(widthHeight));
  }

  private void registerEditorCameraControls() {
    platform.onKeyPressed(keyEvent -> {
      final float translateAmount = 10;
      final float scaleAmount = 0.1f;
      var editorCameraEntity = ecsSystemContainer.getWorld().getEntityByName(EDITOR_CAMERA_ENTITY_NAME);
      var transformComponent = editorCameraEntity.getComponent(TransformComponent.class);

      if (Key.D.getCode() == keyEvent.keyCode()) {
        transformComponent.setX(transformComponent.getX() + translateAmount);
      }
      if (Key.A.getCode() == keyEvent.keyCode()) {
        transformComponent.setX(transformComponent.getX() - translateAmount);
      }
      if (Key.W.getCode() == keyEvent.keyCode()) {
        transformComponent.setY(transformComponent.getY() - translateAmount);
      }
      if (Key.S.getCode() == keyEvent.keyCode()) {
        transformComponent.setY(transformComponent.getY() + translateAmount);
      }
      if (Key.Z.getCode() == keyEvent.keyCode()) {
        transformComponent.setScaleX(transformComponent.getScaleX() - scaleAmount);
        transformComponent.setScaleY(transformComponent.getScaleY() - scaleAmount);
      }
      if (Key.X.getCode() == keyEvent.keyCode()) {
        transformComponent.setScaleX(transformComponent.getScaleX() + scaleAmount);
        transformComponent.setScaleY(transformComponent.getScaleY() + scaleAmount);
      }
      if (Key.ZERO.getCode() == keyEvent.keyCode()) {
        transformComponent.setScaleX(1);
        transformComponent.setScaleY(1);
      }
    });
  }

  private void registerOnEntityClick() {
    platform.onMousePressed(mouseEvent -> {
      if (isLivePreview) return;

      // TODO this has a bug for selection if it is an entity with a gui panel component when camera transform is changed :(
      // TODO this has a bug for selection if it is an entity with a gui text component

      Vector2D clickPoint = solaGraphics.screenToWorldCoordinate(new Vector2D(mouseEvent.x(), mouseEvent.y()));

      ecsSystemContainer.getWorld().getEntitiesWithComponents(TransformComponent.class)
        .stream().filter(entity -> {
          Rectangle boundingBox = getEntityBoundingBox(entity, entity.getComponent(TransformComponent.class));

          return boundingBox.contains(clickPoint);
        }).findFirst().ifPresentOrElse(entitySelectionModel::select, entitySelectionModel::clearSelection);
    });

    platform.onMouseMoved(mouseEvent -> {
      if (isLivePreview) return;

      if (!entitySelectionModel.isEmpty() && mouseInput.isMouseDragged(MouseButton.PRIMARY)) {
        Entity entity = entitySelectionModel.getSelectedItem();
        TransformComponent transformComponent = entity.getComponent(TransformComponent.class);
        Vector2D worldPoint = solaGraphics.screenToWorldCoordinate(new Vector2D(mouseEvent.x(), mouseEvent.y()));

        transformComponent.setTranslate(worldPoint);
      }
    });
  }

  private void drawSelectedBorder(Renderer renderer) {
    // TODO this does not properly draw around entities with a gui text component
    Entity selectedEntity = entitySelectionModel.getSelectedItem();

    if (selectedEntity != null) {
      if (selectedEntity.getComponent(CameraComponent.class) != null) {
        return;
      }

      TransformComponent cameraTransform = solaGraphics.getCameraTransform();
      TransformComponent originalTransformComponent = selectedEntity.getComponent(TransformComponent.class);

      if (originalTransformComponent != null) {
        TransformComponent scaledTransformComponent = GraphicsUtils.getTransformForAppliedCamera(
          originalTransformComponent, cameraTransform
        );
        Rectangle rectangle = getEntityBoundingBox(selectedEntity, scaledTransformComponent);
        var layers = renderer.getLayers();
        int layerCount = renderer.getLayers().size();

        if (layerCount > 0) {
          renderer.drawToLayer(layers.get(layerCount - 1).getName(), r -> {
            renderSelectedEntityBox(renderer, scaledTransformComponent, rectangle);
          });
        } else {
          renderSelectedEntityBox(renderer, scaledTransformComponent, rectangle);
        }
      }
    }
  }

  private void renderSelectedEntityBox(Renderer renderer, TransformComponent scaledTransformComponent, Rectangle rectangle) {
    float size = 3;
    float doubleSize = 2 * size;
    renderer.drawRect(
      scaledTransformComponent.getX() - size, scaledTransformComponent.getY() - size,
      rectangle.getWidth() + doubleSize, rectangle.getHeight() + doubleSize,
      Color.ORANGE
    );
  }

  private void populateFonts() {
    // TODO need a way to reload fonts when project changes or new ones are added
    AssetPool<Font> fontAssetPool = assetPoolProvider.getAssetPool(Font.class);

    FolderUtils folderUtils = new FolderUtils(solaEditorContext);
    File fontFolder = folderUtils.getOrCreateFolder("assets/fonts");
    File[] fontFiles = fontFolder.listFiles();

    if (fontFiles != null) {
      for (File fontFile : fontFiles) {
        if (fontFile.getName().endsWith(".json")) {
          fontAssetPool.addAssetId(fontFile.getName(), fontFile.getAbsolutePath());
        }
      }
    }
  }
}