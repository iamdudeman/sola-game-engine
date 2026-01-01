package technology.sola.engine.physics.component.particle.movement;

/**
 * ParticleNoise hold details on how much noise in a {@link technology.sola.engine.physics.component.particle.Particle}s
 * movement there is.
 *
 * @param xStrength the strength of the noise effect on the x-axis (positive value or zero)
 * @param yStrength the strength of the noise effect on the y-axis (positive value or zero)
 * @param frequency the frequency of direction change (higher values mean more direction changes [0.5f is a decent default])
 */
public record ParticleNoise(
  float xStrength,
  float yStrength,
  float frequency
) {
}
