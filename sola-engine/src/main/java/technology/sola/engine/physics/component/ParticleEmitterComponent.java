package technology.sola.engine.physics.component;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.RenderMode;
import technology.sola.math.linear.Vector2D;

import java.io.Serial;

// todo consider an array of colors maybe? (think confetti)
// todo consider different methods for deciding which next color to use if it is an array
// todo consider adding some sort of blend mode instead of always fading out alpha?

// TODO note, blend mode would need to be implemented in Renderer as well (might actually be the same as "RenderMode")

public class ParticleEmitterComponent implements Component {
  @Serial
  private static final long serialVersionUID = -8273651094891084287L;
  private Color particleColor = Color.WHITE;
  private RenderMode renderMode = RenderMode.ALPHA;
  private float particleMinLife = 1f;
  private float particleMaxLife = 2f;
  private Vector2D particleMinVelocity = new Vector2D(-50, -100);
  private Vector2D particleMaxVelocity = new Vector2D(50, -0.1f);
  private float particleMinSize = 8f;
  private float particleMaxSize = 8f;
  private int particlesPerEmit = 1;
  private float particleEmissionDelay;

  private float timeSinceLastEmission;

  public ParticleEmitterComponent() {
    this(0.1f);
  }

  public ParticleEmitterComponent(float particleEmissionDelay) {
    this.particleEmissionDelay = particleEmissionDelay;
  }

  public void addTimeSinceLastEmission(float delta) {
    timeSinceLastEmission += delta;
  }

  public boolean isAbleToEmit() {
    return timeSinceLastEmission > particleEmissionDelay;
  }

  public void emit() {
    timeSinceLastEmission = 0;
  }


  public void setParticleEmissionDelay(float particleEmissionDelay) {
    this.particleEmissionDelay = particleEmissionDelay;
  }

  public int getParticlesPerEmit() {
    return particlesPerEmit;
  }

  public void setParticlesPerEmit(int particlesPerEmit) {
    this.particlesPerEmit = particlesPerEmit;
  }

  public Color getParticleColor() {
    return particleColor;
  }

  public void setParticleColor(Color particleColor) {
    this.particleColor = particleColor;
  }

  public RenderMode getRenderMode() {
    return renderMode;
  }

  public void setRenderMode(RenderMode renderMode) {
    this.renderMode = renderMode;
  }

  public float getParticleMinLife() {
    return particleMinLife;
  }

  public float getParticleMaxLife() {
    return particleMaxLife;
  }

  public void setParticleLifeBounds(float particleMinLife, float particleMaxLife) {
    this.particleMinLife = particleMinLife;
    this.particleMaxLife = particleMaxLife;
  }

  public void setParticleLife(float life) {
    setParticleLifeBounds(life, life);
  }

  public Vector2D getParticleMinVelocity() {
    return particleMinVelocity;
  }

  public Vector2D getParticleMaxVelocity() {
    return particleMaxVelocity;
  }

  public void setParticleVelocityBounds(Vector2D particleMinVelocity, Vector2D particleMaxVelocity) {
    this.particleMinVelocity = particleMinVelocity;
    this.particleMaxVelocity = particleMaxVelocity;
  }

  public void setParticleVelocity(Vector2D velocity) {
    setParticleVelocityBounds(velocity, velocity);
  }

  public float getParticleMinSize() {
    return particleMinSize;
  }

  public float getParticleMaxSize() {
    return particleMaxSize;
  }

  public void setParticleSizeBounds(float particleMinSize, float particleMaxSize) {
    this.particleMinSize = particleMinSize;
    this.particleMaxSize = particleMaxSize;
  }

  public void setParticleSize(float size) {
    setParticleSizeBounds(size, size);
  }
}
