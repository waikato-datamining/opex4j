/*
 * PolygonTest.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package opex4j;

import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests the {@link Polygon} class.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class PolygonTest {

  @Test
  public void testInstantiation() {
    Polygon poly;
    List<Point> points;

    points = new ArrayList<>();
    points.add(new Point(1, 1));
    points.add(new Point(2, 1));
    points.add(new Point(2, 2));
    points.add(new Point(1, 2));
    poly = new Polygon(points);
    assertEquals(4, poly.size(), "number of points");
    assertThrows(IllegalArgumentException.class, () -> new BBox(2, 1, 2, 2));
    assertThrows(IllegalArgumentException.class, () -> new BBox(3, 1, 2, 2));
    assertThrows(IllegalArgumentException.class, () -> new BBox(1, 2, 2, 2));
    assertThrows(IllegalArgumentException.class, () -> new BBox(1, 3, 2, 2));
  }

  @Test
  public void testBBox() {
    Polygon poly;
    List<Point> points;
    final List<Point> fpoints;
    BBox bbox;

    points = new ArrayList<>();
    points.add(new Point(1, 1));
    points.add(new Point(2, 1));
    points.add(new Point(2, 2));
    points.add(new Point(1, 2));
    poly = new Polygon(points);

    bbox = poly.toBBox();
    assertEquals(1, bbox.getLeft(), "left");
    assertEquals(1, bbox.getTop(), "top");
    assertEquals(2, bbox.getRight(), "right");
    assertEquals(2, bbox.getBottom(), "bottom");

    assertThrows(IllegalArgumentException.class, () -> new Polygon(null));

    fpoints = new ArrayList<>();
    fpoints.add(new Point(1, 1));
    assertThrows(IllegalArgumentException.class, () -> new Polygon(fpoints));
  }

  @Test
  public void testAWTPolygon() {
    Polygon poly;
    List<Point> points;
    java.awt.Polygon awt;

    points = new ArrayList<>();
    points.add(new Point(1, 1));
    points.add(new Point(2, 1));
    points.add(new Point(2, 2));
    points.add(new Point(1, 2));
    poly = new Polygon(points);
    awt = poly.toPolygon();
    assertEquals(4, awt.npoints, "npoints");
    assertArrayEquals(new int[]{1, 2, 2, 1}, awt.xpoints, "xpoints");
    assertArrayEquals(new int[]{1, 1, 2, 2}, awt.ypoints, "ypoints");
  }
}
