/*
 * JsonHandler.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package opex4j.core;

import com.google.gson.JsonObject;

/**
 * Interface for JSON-capable objects.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public interface JsonHandler {

  /**
   * Turns the object into JSON.
   *
   * @return		the generated JSON
   */
  public JsonObject toJson();
}
