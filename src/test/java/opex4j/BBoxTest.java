/*
 * BBoxTest.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package opex4j;

import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests the {@link BBox} class.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class BBoxTest {

  @Test
  public void testInstantiation() {
    BBox bbox;

    bbox = new BBox(1, 1, 2, 2);
    assertEquals(1, bbox.getLeft(), "left");
    assertEquals(1, bbox.getTop(), "top");
    assertEquals(2, bbox.getRight(), "right");
    assertEquals(2, bbox.getBottom(), "bottom");
    assertThrows(IllegalArgumentException.class, () -> new BBox(2, 1, 2, 2));
    assertThrows(IllegalArgumentException.class, () -> new BBox(3, 1, 2, 2));
    assertThrows(IllegalArgumentException.class, () -> new BBox(1, 2, 2, 2));
    assertThrows(IllegalArgumentException.class, () -> new BBox(1, 3, 2, 2));
  }

  @Test
  public void testPolygon() {
    BBox bbox;
    Polygon poly;
    List<Point> points;

    bbox = new BBox(1, 1, 2, 2);
    poly = bbox.toPolygon();
    assertEquals(4, poly.size(), "number of points");

    points = new ArrayList<>();
    points.add(new Point(1, 1));
    points.add(new Point(2, 1));
    points.add(new Point(2, 2));
    points.add(new Point(1, 2));
    assertEquals(points, poly.getPoints(), "polygon points");
  }

  @Test
  public void testRectangle() {
    BBox bbox;
    Rectangle rect;

    bbox = new BBox(1, 1, 2, 2);
    rect = bbox.toRectangle();
    assertEquals(1, rect.x, "x");
    assertEquals(1, rect.y, "y");
    assertEquals(2, rect.width, "width");
    assertEquals(2, rect.height, "height");
  }
}
