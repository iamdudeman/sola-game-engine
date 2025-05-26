package technology.sola.engine.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.BlendMode;
import technology.sola.engine.graphics.renderer.SoftwareRenderer;
import technology.sola.logging.JavaSolaLoggerFactory;
import technology.sola.logging.SolaLogLevel;
import technology.sola.logging.SolaLogger;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 3, warmups = 1)
@Warmup(iterations = 1)
@Measurement(iterations = 5)
public class SoftwareRendererBenchmark {
  @Setup(Level.Trial)
  public void setup() {
    SolaLogger.configure(SolaLogLevel.OFF, new JavaSolaLoggerFactory());
  }

  @Benchmark
  public void circleDraw(DrawShapeState state, Blackhole blackhole) {
    SoftwareRenderer softwareRenderer = new SoftwareRenderer(1200, 1200);

    for (int i = 0; i < state.iterations; i++) {
      softwareRenderer.drawCircle(5, 5, state.size, state.color);
    }

    blackhole.consume(softwareRenderer);
  }

  @Benchmark
  public void circleFill(DrawShapeState state, Blackhole blackhole) {
    SoftwareRenderer softwareRenderer = new SoftwareRenderer(1200, 1200);

    for (int i = 0; i < state.iterations; i++) {
      softwareRenderer.fillCircle(5, 5, state.size, state.color);
    }

    blackhole.consume(softwareRenderer);
  }

  @Benchmark
  public void rectDraw(DrawShapeState state, Blackhole blackhole) {
    SoftwareRenderer softwareRenderer = new SoftwareRenderer(1200, 1200);

    for (int i = 0; i < state.iterations; i++) {
      softwareRenderer.drawRect(5, 5, state.size, state.size, state.color);
    }

    blackhole.consume(softwareRenderer);
  }

  @Benchmark
  public void rectFill(DrawShapeState state, Blackhole blackhole) {
    SoftwareRenderer softwareRenderer = new SoftwareRenderer(1200, 1200);

    for (int i = 0; i < state.iterations; i++) {
      softwareRenderer.fillRect(5, 5, state.size, state.size, state.color);
    }

    blackhole.consume(softwareRenderer);
  }

  @Benchmark
  public void blendModeNoBlending(BlendModeState state, Blackhole blackhole) {
    SoftwareRenderer softwareRenderer = new SoftwareRenderer(1200, 1200);

    softwareRenderer.setBlendFunction(BlendMode.NO_BLENDING);

    for (int i = 0; i < state.iterations; i++) {
      softwareRenderer.fillRect(5, 5, 50, 50, state.color);
    }

    blackhole.consume(softwareRenderer);
  }

  @Benchmark
  public void blendModeNormal(BlendModeState state, Blackhole blackhole) {
    SoftwareRenderer softwareRenderer = new SoftwareRenderer(1200, 1200);

    softwareRenderer.setBlendFunction(BlendMode.NORMAL);

    for (int i = 0; i < state.iterations; i++) {
      softwareRenderer.fillRect(5, 5, 50, 50, state.color);
    }

    blackhole.consume(softwareRenderer);
  }

  @State(Scope.Thread)
  public static class DrawShapeState {
    public final int iterations = 1000;
    public final Color color = Color.BLACK;

    @Param({ "10", "50", "250" })
    public int size;
  }

  @State(Scope.Thread)
  public static class BlendModeState {
    public final int iterations = 5000;
    public final Color color = new Color(220, 240, 240, 240);
  }
}
