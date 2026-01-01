package technology.sola.engine.utils;

import org.jspecify.annotations.NullMarked;

import java.util.Random;

/**
 * Wrapper around {@link Random} that exposes some additional convenience methods. This wrapper will ensure that random
 * number generation will work across different {@link technology.sola.engine.core.SolaPlatform}s.
 */
@NullMarked
public class SolaRandom {
  private static final Random RANDOM = new Random();

  /**
   * Sets the seed for random number generation.
   *
   * @param seed the seed
   */
  public static void setSeed(long seed) {
    RANDOM.setSeed(seed);
    PerlinNoise.setSeed((int) seed % 255);
  }

  /**
   * @return the next pseudorandom, uniformly distributed boolean value
   */
  public static boolean nextBoolean() {
    return RANDOM.nextBoolean();
  }

  /**
   * Returns a pseudorandom, uniformly distributed int value between 0 (inclusive) and the specified value (exclusive)
   *
   * @param bound the exclusive boundary value
   * @return the random int value
   */
  public static int nextInt(int bound) {
    return RANDOM.nextInt(bound);
  }

  /**
   * Returns a pseudorandom, uniformly distributed int value between min (inclusive) and max value (inclusive)
   *
   * @param min the inclusive min value
   * @param max the inclusive max value
   * @return the random int value
   */
  public static int nextInt(int min, int max) {
    if (min == max) {
      return min;
    }

    return RANDOM.nextInt(max - min + 1) + min;
  }

  /**
   * @return a pseudorandom, uniformly distributed float value between 0.0 inclusive and 1 exclusive.
   */
  public static float nextFloat() {
    return RANDOM.nextFloat();
  }

  /**
   * Returns a pseudorandom, uniformly distributed float value between min (inclusive) and max value (inclusive)
   *
   * @param min the inclusive min value
   * @param max the inclusive max value
   * @return the random float value
   */
  public static float nextFloat(float min, float max) {
    if (Float.compare(min, max) == 0) {
      return min;
    }

    return min + RANDOM.nextFloat() * (max - min);
  }

  /**
   * Generates a Perlin noise value.
   *
   * @param x the x "time"
   * @param y the y "time"
   * @return the noise value
   */
  public static float noise(float x, float y) {
    return (float) PerlinNoise.noise(x, y);
  }

  private SolaRandom() {
  }
}
