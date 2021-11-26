package technology.sola.engine.editor;

import java.io.File;

public class FolderUtils {
  private final SolaEditorContext solaEditorContext;

  public FolderUtils(SolaEditorContext solaEditorContext) {
    this.solaEditorContext = solaEditorContext;
  }

  public File getOrCreateFolder(String path) {
    File projectFolder = solaEditorContext.projectFilePropertyProperty().getValue().getParentFile();
    File childDirectory = new File(projectFolder, path);

    childDirectory.mkdirs();

    return childDirectory;
  }

  public void createDirectories(String ...paths) {
    for (String path : paths) {
      getOrCreateFolder(path);
    }
  }
}
