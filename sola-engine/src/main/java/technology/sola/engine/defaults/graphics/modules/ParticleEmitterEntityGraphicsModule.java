package technology.sola.engine.defaults.graphics.modules;

import technology.sola.ecs.World;
import technology.sola.ecs.view.View;
import technology.sola.ecs.view.View2Entry;
import technology.sola.engine.core.component.TransformComponent;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.physics.component.ParticleEmitterComponent;
import technology.sola.math.linear.Matrix3D;

public class ParticleEmitterEntityGraphicsModule extends SolaEntityGraphicsModule<View2Entry<ParticleEmitterComponent, TransformComponent>> {
  @Override
  public View<View2Entry<ParticleEmitterComponent, TransformComponent>> getViewToRender(World world) {
    return world.createView().of(ParticleEmitterComponent.class, TransformComponent.class);
  }

  @Override
  public void renderMethod(Renderer renderer, View2Entry<ParticleEmitterComponent, TransformComponent> viewEntry, Matrix3D cameraScaleTransform, Matrix3D cameraTranslationTransform, TransformComponent cameraModifiedEntityTransform) {
    var particleIter = viewEntry.c1().emittedParticleIterator();

    while (particleIter.hasNext()) {
      var particle = particleIter.next();
      var position = cameraModifiedEntityTransform.getTranslate().add(cameraTranslationTransform.multiply(particle.getPosition()));
      var size = cameraScaleTransform.multiply(particle.getSize(), particle.getSize());

      renderer.fillCircle(
        position.x(),
        position.y(),
        size.x() * 0.5f,
        particle.getColorForRendering()
      );
    }
  }

  @Override
  public void renderMethod(Renderer renderer, View2Entry<ParticleEmitterComponent, TransformComponent> viewEntry, TransformComponent cameraModifiedEntityTransform) {
    // Not used since camera translate and scale transforms are needed
  }
}
