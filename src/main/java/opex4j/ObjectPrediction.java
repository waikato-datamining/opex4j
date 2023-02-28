/*
 * ObjectPrediction.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package opex4j;

import com.google.gson.JsonObject;
import opex4j.core.AbstractJsonHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a single prediction.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class ObjectPrediction
  extends AbstractJsonHandler {

  /** the (optional) score. */
  protected Double m_Score;

  /** the label. */
  protected String m_Label;

  /** the bounding box. */
  protected BBox m_BBox;

  /** the polygon. */
  protected Polygon m_Polygon;

  /** the meta-data. */
  protected Map<String,String> m_Meta;

  /**
   * Initializes the prediction.
   *
   * @param label	the label
   * @param bbox	the bounding box
   */
  public ObjectPrediction(String label, BBox bbox) {
    this(label, bbox, bbox.toPolygon(), null);
  }

  /**
   * Initializes the prediction.
   *
   * @param label	the label
   * @param polygon	the polygon
   */
  public ObjectPrediction(String label, Polygon polygon) {
    this(label, polygon.toBBox(), polygon, null);
  }

  /**
   * Initializes the prediction.
   *
   * @param label	the label
   * @param bbox	the bounding box
   */
  public ObjectPrediction(String label, BBox bbox, Polygon polygon, Map<String,String> meta) {
    this(label, null, bbox, polygon, meta);
  }

  /**
   * Initializes the prediction.
   *
   * @param label	the label
   * @param score 	the score, can be null
   * @param bbox	the bounding box
   * @param polygon 	the polygon
   * @param meta 	the meta-data, can be null
   */
  public ObjectPrediction(String label, Double score, BBox bbox, Polygon polygon, Map<String,String> meta) {
    m_Label   = label;
    m_Score   = score;
    m_BBox    = bbox;
    m_Polygon = polygon;
    m_Meta    = (meta == null ? new HashMap<>() : new HashMap<>(meta));
  }

  /**
   * Returns the label.
   *
   * @return		the label
   */
  public String getLabel() {
    return m_Label;
  }

  /**
   * Returns the score, if any.
   *
   * @return		the score, can be null
   */
  public Double getScore() {
    return m_Score;
  }

  /**
   * Returns the bounding box.
   *
   * @return		the bbox
   */
  public BBox getBBox() {
    return m_BBox;
  }

  /**
   * Returns the polygon.
   *
   * @return		the polygon
   */
  public Polygon getPolygon() {
    return m_Polygon;
  }

  /**
   * Returns the meta-data.
   *
   * @return		the meta-data
   */
  public Map<String, String> getMeta() {
    return m_Meta;
  }

  /**
   * Turns the object into JSON.
   *
   * @return		the generated JSON
   */
  @Override
  public JsonObject toJson() {
    JsonObject	result;
    JsonObject	meta;

    result = new JsonObject();

    if (m_Score != null)
      result.addProperty("score", m_Score);
    result.addProperty("label", m_Label);
    result.add("bbox", m_BBox.toJson());
    result.add("polygon", m_Polygon.toJson());
    if (m_Meta.size() > 0) {
      meta = new JsonObject();
      for (String key: m_Meta.keySet())
        meta.addProperty(key, m_Meta.get(key));
      result.add("meta", meta);
    }

    return result;
  }

  /**
   * Generates an object prediction from JSON.
   *
   * @param obj		the JSON object to use
   * @return		the generated object prediction
   */
  public static ObjectPrediction newInstance(JsonObject obj) {
    Double		score;
    String		label;
    BBox		bbox;
    Polygon		polygon;
    Map<String,String>	meta;
    JsonObject		jmeta;

    score   = (obj.has("score")) ? obj.get("score").getAsDouble() : null;
    label   = obj.get("label").getAsString();
    bbox    = BBox.newInstance(obj.get("bbox").getAsJsonObject());
    polygon = Polygon.newInstance(obj.get("polygon").getAsJsonObject());
    meta    = null;
    if (obj.has("meta")) {
      jmeta = obj.get("meta").getAsJsonObject();
      meta  = new HashMap<>();
      for (String key: jmeta.keySet())
        meta.put(key, jmeta.get(key).getAsString());
    }

    return new ObjectPrediction(label, score, bbox, polygon, meta);
  }
}
