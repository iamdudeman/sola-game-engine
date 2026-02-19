package technology.sola.engine.editor;

import technology.sola.engine.editor.scene.ComponentEditorModule;
import technology.sola.engine.editor.scene.common.CameraComponentEditorModule;
import technology.sola.engine.editor.scene.common.CircleRendererComponentEditorModule;
import technology.sola.engine.editor.scene.common.TransformComponentEditorModule;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record SolaEditorCustomization(
  List<ComponentEditorModule<?>> componentEditorModules
) {
  /**
   * Creates a new SolaEditorCustomization instance with default customization.
   */
  public SolaEditorCustomization() {
    this(new Builder().build().componentEditorModules());
  }

  public static class Builder {
    private final Set<ComponentEditorModule<?>> componentEditorModules = new HashSet<>();
    private boolean excludeDefaultComponentModules = false;

    public Builder withDefaultComponentModulesExcluded() {
      this.excludeDefaultComponentModules = true;
      return this;
    }

    public Builder withComponentEditorModule(ComponentEditorModule<?> componentEditorModule) {
      this.componentEditorModules.add(componentEditorModule);
      return this;
    }

    public SolaEditorCustomization build() {
      if (!excludeDefaultComponentModules) {
        this.componentEditorModules.addAll(List.of(
          new TransformComponentEditorModule(),
          new CircleRendererComponentEditorModule(),
          new CameraComponentEditorModule()
        ));
      }

      return new SolaEditorCustomization(this.componentEditorModules.stream().toList());
    }
  }
}
