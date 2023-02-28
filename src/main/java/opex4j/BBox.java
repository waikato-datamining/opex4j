/*
 * BBox.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package opex4j;

import com.google.gson.JsonObject;
import opex4j.core.AbstractJsonHandler;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single bounding box.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class BBox
  extends AbstractJsonHandler {

  /** the top coordinate. */
  protected int m_Top;

  /** the left coordinate. */
  protected int m_Left;

  /** the bottom coordinate. */
  protected int m_Bottom;

  /** the right coordinate. */
  protected int m_Right;

  /**
   * Initializes the bounding box.
   *
   * @param left	the left coordinate
   * @param top		the top coordinate
   * @param right	the right coordinate
   * @param bottom	the bottom coordinate
   */
  public BBox(int left, int top, int right, int bottom) {
    if (top >= bottom)
      throw new IllegalArgumentException("Violation of top < bottom: top=" + top + " and bottom=" + bottom);
    if (left >= right)
      throw new IllegalArgumentException("Violation of left < right: left=" + left + " and right=" + right);
    m_Top    = top;
    m_Left   = left;
    m_Bottom = bottom;
    m_Right  = right;
  }

  /**
   * Returns the left coordinate.
   *
   * @return		the left
   */
  public int getLeft() {
    return m_Left;
  }

  /**
   * Returns the top coordinate.
   *
   * @return		the top
   */
  public int getTop() {
    return m_Top;
  }

  /**
   * Returns the right coordinate.
   *
   * @return		the right
   */
  public int getRight() {
    return m_Right;
  }

  /**
   * Returns the bottom coordinate.
   *
   * @return		the bottom
   */
  public int getBottom() {
    return m_Bottom;
  }

  /**
   * Turns the object into JSON.
   *
   * @return		the generated JSON
   */
  @Override
  public JsonObject toJson() {
    JsonObject	result;

    result = new JsonObject();
    result.addProperty("top", m_Top);
    result.addProperty("left", m_Left);
    result.addProperty("bottom", m_Bottom);
    result.addProperty("right", m_Right);

    return result;
  }

  /**
   * Generates an AWT rectangle.
   *
   * @return		the generated rectangle
   */
  public Rectangle toRectangle() {
    return new Rectangle(getLeft(), getTop(), getRight() - getLeft() + 1, getBottom() - getTop() + 1);
  }

  /**
   * Returns itself as a polygon.
   *
   * @return		the polygon
   */
  public Polygon toPolygon() {
    List<Point> 	points;

    points = new ArrayList<>();
    points.add(new Point(getLeft(), getTop()));
    points.add(new Point(getRight(), getTop()));
    points.add(new Point(getRight(), getBottom()));
    points.add(new Point(getLeft(), getBottom()));

    return new Polygon(points);
  }

  /**
   * Instantiates a BBox from JSON.
   *
   * @param obj		the JSON to use
   * @return		the generated BBox instance
   */
  public static BBox newInstance(JsonObject obj) {
    return new BBox(
      obj.get("left").getAsInt(),
      obj.get("top").getAsInt(),
      obj.get("right").getAsInt(),
      obj.get("bottom").getAsInt());
  }

  /**
   * Instantiates a BBox from a rectangle.
   *
   * @param rect	the rectangle to use
   * @return		the generated BBox instance
   */
  public static BBox newInstance(Rectangle rect) {
    return new BBox(rect.x, rect.y, rect.x + rect.width - 1, rect.y + rect.height - 1);
  }

  /**
   * Instantiates a BBox from x/y coordinates, width and height.
   *
   * @param x		the x coordinate
   * @param y		the y coordinate
   * @param width	the width of the box
   * @param height 	the height of the box
   * @return		the generated BBox instance
   */
  public static BBox newInstance(int x, int y, int width, int height) {
    return new BBox(x, y, x + width - 1, y + height - 1);
  }
}
