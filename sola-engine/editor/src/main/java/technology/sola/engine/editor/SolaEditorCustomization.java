package technology.sola.engine.editor;

import org.jspecify.annotations.NullMarked;
import technology.sola.ecs.Component;
import technology.sola.engine.assets.scene.SceneAssetLoaderConfigurator;
import technology.sola.engine.editor.scene.ComponentEditorModule;
import technology.sola.engine.editor.scene.common.CameraComponentEditorModule;
import technology.sola.engine.editor.scene.common.CircleRendererComponentEditorModule;
import technology.sola.engine.editor.scene.common.TransformComponentEditorModule;
import technology.sola.json.mapper.JsonMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NullMarked
public record SolaEditorCustomization(
  List<ComponentEditorModule<?>> componentEditorModules,
  List<JsonMapper<? extends Component>> componentJsonMappers
) {
  /**
   * Creates a new SolaEditorCustomization instance with default customization.
   */
  public SolaEditorCustomization() {
    this(new Builder().build().componentEditorModules(), SceneAssetLoaderConfigurator.getDefaultJsonMappers());
  }

  public static class Builder {
    private final Set<ComponentEditorModule<?>> componentEditorModules = new HashSet<>();
    private final Set<JsonMapper<? extends Component>> componentJsonMappers = new HashSet<>();
    private boolean excludeDefaultComponents = false;

    public Builder withDefaultComponentsExcluded() {
      this.excludeDefaultComponents = true;
      return this;
    }

    public Builder withComponentJsonMapper(JsonMapper<? extends Component> componentJsonMapper) {
      this.componentJsonMappers.add(componentJsonMapper);

      return this;
    }

    public Builder withComponentEditorModule(ComponentEditorModule<?> componentEditorModule) {
      this.componentEditorModules.add(componentEditorModule);
      return this;
    }

    public SolaEditorCustomization build() {
      if (!excludeDefaultComponents) {
        this.componentEditorModules.addAll(List.of(
          new TransformComponentEditorModule(),
          new CircleRendererComponentEditorModule(),
          new CameraComponentEditorModule()
        ));
        this.componentJsonMappers.addAll(SceneAssetLoaderConfigurator.getDefaultJsonMappers());
      }

      return new SolaEditorCustomization(
        this.componentEditorModules.stream().toList(),
        this.componentJsonMappers.stream().toList()
      );
    }
  }
}
