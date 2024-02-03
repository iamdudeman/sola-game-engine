package technology.sola.engine.physics.component;

import technology.sola.ecs.Component;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.blend.BlendFunction;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

// todo clean up this file!

public class ParticleEmitterComponent implements Component {
  private List<Particle> particleList = new ArrayList<>();
  private Random random = new Random();


  private Color particleColor = Color.WHITE;
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

  public void emitIfAble(float delta) {
    timeSinceLastEmission += delta;

    if (timeSinceLastEmission > particleEmissionDelay) {
      Vector2D minVel = getParticleMinVelocity();
      Vector2D maxVel = getParticleMaxVelocity();

      for (int i = 0; i < getParticlesPerEmit(); i++) {
        float xVel = getRandomFloat(minVel.x(), maxVel.x());
        float yVel = getRandomFloat(minVel.y(), maxVel.y());
        float size = getRandomFloat(getParticleMinSize(), getParticleMaxSize());
        float life = getRandomFloat(getParticleMinLife(), getParticleMaxLife());

        Particle particle = new Particle(
          getParticleColor(), size, life, new Vector2D(0, 0), new Vector2D(xVel, yVel)
        );

        particleList.add(particle);
      }

      timeSinceLastEmission = 0;
    }
  }

  public Iterator<Particle> emittedParticleIterator() {
    return particleList.iterator();
  }

  public ParticleEmitterComponent setParticleEmissionDelay(float particleEmissionDelay) {
    this.particleEmissionDelay = particleEmissionDelay;

    return this;
  }

  public int getParticlesPerEmit() {
    return particlesPerEmit;
  }

  public ParticleEmitterComponent setParticlesPerEmit(int particlesPerEmit) {
    this.particlesPerEmit = particlesPerEmit;

    return this;
  }

  public Color getParticleColor() {
    return particleColor;
  }

  public ParticleEmitterComponent setParticleColor(Color particleColor) {
    this.particleColor = particleColor;

    return this;
  }

  public float getParticleMinLife() {
    return particleMinLife;
  }

  public float getParticleMaxLife() {
    return particleMaxLife;
  }

  public ParticleEmitterComponent setParticleLifeBounds(float particleMinLife, float particleMaxLife) {
    this.particleMinLife = particleMinLife;
    this.particleMaxLife = particleMaxLife;

    return this;
  }

  public ParticleEmitterComponent setParticleLife(float life) {
    return setParticleLifeBounds(life, life);
  }

  public Vector2D getParticleMinVelocity() {
    return particleMinVelocity;
  }

  public Vector2D getParticleMaxVelocity() {
    return particleMaxVelocity;
  }

  public ParticleEmitterComponent setParticleVelocityBounds(Vector2D particleMinVelocity, Vector2D particleMaxVelocity) {
    this.particleMinVelocity = particleMinVelocity;
    this.particleMaxVelocity = particleMaxVelocity;

    return this;
  }

  public ParticleEmitterComponent setParticleVelocity(Vector2D velocity) {
    setParticleVelocityBounds(velocity, velocity);

    return this;
  }

  public float getParticleMinSize() {
    return particleMinSize;
  }

  public float getParticleMaxSize() {
    return particleMaxSize;
  }

  public ParticleEmitterComponent setParticleSizeBounds(float particleMinSize, float particleMaxSize) {
    this.particleMinSize = particleMinSize;
    this.particleMaxSize = particleMaxSize;

    return this;
  }

  public ParticleEmitterComponent setParticleSize(float size) {
    return setParticleSizeBounds(size, size);
  }

  private float getRandomFloat(float min, float max) {
    if (Float.compare(min, max) == 0) {
      return min;
    }

    return random.nextFloat(min, max);
  }

  public static class Particle {
    private final Color baseColor;
    private final float size;
    private final float inverseMaxLifespan;
    private final Vector2D velocity;
    private Vector2D position;
    private float remainingLifespan;

    public Particle(Color baseColor, float size, float maxLifespan, Vector2D position, Vector2D velocity) {
      this.baseColor = baseColor;
      this.size = size;
      this.position = position;
      this.velocity = velocity;

      remainingLifespan = maxLifespan;
      inverseMaxLifespan = 1 / maxLifespan;
    }

    public void update(float delta) {
      position = position.add(velocity.scalar(delta));
      remainingLifespan -= delta;
    }

    public Color getColorForRendering() {
      int alpha = Math.max((int) ((255 * remainingLifespan * inverseMaxLifespan) + 0.5f), 0);

      return baseColor.updateAlpha(alpha);
    }

    public float getRemainingLifespan() {
      return remainingLifespan;
    }

    public Vector2D getPosition() {
      return position;
    }

    public float getSize() {
      return size;
    }
  }
}
