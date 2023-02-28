/*
 * Polygon.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package opex4j;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import opex4j.core.AbstractJsonHandler;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single polygon.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class Polygon
  extends AbstractJsonHandler {

  /** the points making up the polygon. */
  protected List<Point> m_Points;

  /**
   * Initializes the polygon.
   */
  public Polygon(List<Point> points) {
    if (points == null)
      throw new IllegalArgumentException("Points cannot be null!");
    if (points.size() < 3)
      throw new IllegalArgumentException("At least three points required, provided: " + points.size());
    m_Points = new ArrayList<>(points);
  }

  /**
   * Returns the points.
   *
   * @return		the points
   */
  public List<Point> getPoints() {
    return m_Points;
  }

  /**
   * Returns the number of points.
   *
   * @return		the number of points
   */
  public int size() {
    return m_Points.size();
  }

  /**
   * Turns the object into JSON.
   *
   * @return		the generated JSON
   */
  @Override
  public JsonObject toJson() {
    JsonObject	result;
    JsonArray 	points;
    JsonArray	point;

    result = new JsonObject();
    points = new JsonArray();
    result.add("points", points);
    for (Point p: m_Points) {
      point = new JsonArray();
      point.add((int) p.getX());
      point.add((int) p.getY());
      points.add(point);
    }

    return result;
  }

  /**
   * Generates an AWT polygon.
   *
   * @return		the polygon
   */
  public java.awt.Polygon toPolygon() {
    int[]	x;
    int[]	y;
    int		i;

    x = new int[size()];
    y = new int[size()];
    for (i = 0; i < size(); i++) {
      x[i] = (int) m_Points.get(i).getX();
      y[i] = (int) m_Points.get(i).getY();
    }

    return new java.awt.Polygon(x, y, x.length);
  }

  /**
   * Determines the max coordinates and creates a BBox from it.
   *
   * @return		the bounding box
   */
  public BBox toBBox() {
    int		minx;
    int		maxx;
    int		miny;
    int		maxy;

    minx = Integer.MAX_VALUE;
    maxx = 0;
    miny = Integer.MAX_VALUE;
    maxy = 0;

    for (Point p: m_Points) {
      minx = Math.min(minx, (int) p.getX());
      maxx = Math.max(maxx, (int) p.getX());
      miny = Math.min(miny, (int) p.getY());
      maxy = Math.max(maxy, (int) p.getY());
    }

    return new BBox(minx, miny, maxx, maxy);
  }

  /**
   * Creates a new Polygon from the JSON object.
   *
   * @param obj		the JSON object to use
   * @return		the generated instance
   */
  public static Polygon newInstance(JsonObject obj) {
    JsonArray 	jpoints;
    JsonArray 	jpoint;
    int		i;
    List<Point>	points;

    jpoints = obj.getAsJsonArray("points");
    points  = new ArrayList<>();
    for (i = 0; i < jpoints.size(); i++) {
      jpoint = jpoints.get(i).getAsJsonArray();
      points.add(new Point(jpoint.get(0).getAsInt(), jpoint.get(1).getAsInt()));
    }

    return new Polygon(points);
  }

  /**
   * Creates a new Polygon from the AWT polygon.
   *
   * @param polygon	the AWT polygon to use
   * @return		the generated instance
   */
  public static Polygon newInstance(java.awt.Polygon polygon) {
    int		i;
    List<Point>	points;

    points  = new ArrayList<>();
    for (i = 0; i < polygon.npoints; i++)
      points.add(new Point(polygon.xpoints[i], polygon.ypoints[i]));

    return new Polygon(points);
  }
}
