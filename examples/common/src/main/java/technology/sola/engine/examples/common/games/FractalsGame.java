package technology.sola.engine.examples.common.games;

import technology.sola.engine.core.Sola;
import technology.sola.engine.core.SolaConfiguration;
import technology.sola.engine.graphics.Color;
import technology.sola.engine.graphics.renderer.Renderer;
import technology.sola.engine.graphics.screen.AspectMode;
import technology.sola.math.geometry.Triangle;
import technology.sola.math.linear.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class FractalsGame extends Sola {
  private List<Triangle> triangles = new ArrayList<>();

  public FractalsGame() {
    super(new SolaConfiguration("Fractals", 800, 800, 30));
  }

  @Override
  protected void onRender(Renderer renderer) {
    renderer.clear(Color.BLACK);

    for (var triangle : triangles) {
      renderer.fillTriangle(
        triangle.p1().x(), triangle.p1().y(),
        triangle.p2().x(), triangle.p2().y(),
        triangle.p3().x(), triangle.p3().y(),
        Color.WHITE
      );
    }
  }

  @Override
  protected void onInit() {
    platform.getViewport().setAspectMode(AspectMode.MAINTAIN);

    var original = scaleTriangle(unitEquilateralTriangle(), 800);

    triangles.add(original);

    triangles = sierpinski();
    triangles = sierpinski();
    triangles = sierpinski();
  }

  private List<Triangle> sierpinski() {
    List<Triangle> newTriangles = new ArrayList<>();

    for (var triangle : triangles) {
      var newTriangle = translateTriangle(
        scaleTriangle(triangle, 0.5f),
        new Vector2D(triangle.p1().x() * 0.5f, triangle.p1().y() * 0.5f)
      );
      var sideLength = newTriangle.p2().y() - newTriangle.p1().y();

      newTriangles.add(
        translateTriangle(newTriangle, new Vector2D(-sideLength * 0.5f + sideLength * 0.5f, 0))
      );
      newTriangles.add(
        translateTriangle(newTriangle, new Vector2D(-sideLength * 0.5f, sideLength))
      );
      newTriangles.add(
        translateTriangle(newTriangle, new Vector2D(-sideLength * 0.5f + sideLength, sideLength))
      );
    }

    return newTriangles;
  }

  private Triangle unitEquilateralTriangle() {
    return new Triangle(
      new Vector2D(0.5f, 0),
      new Vector2D(0, 1),
      new Vector2D(1, 1)
    );
  }

  private Triangle scaleTriangle(Triangle triangle, float scale) {
    return new Triangle(
      triangle.p1().scalar(scale),
      triangle.p2().scalar(scale),
      triangle.p3().scalar(scale)
    );
  }

  private Triangle translateTriangle(Triangle triangle, Vector2D translation) {
    return new Triangle(
      triangle.p1().add(translation),
      triangle.p2().add(translation),
      triangle.p3().add(translation)
    );
  }
}
