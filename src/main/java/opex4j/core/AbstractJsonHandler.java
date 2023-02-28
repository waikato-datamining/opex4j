/*
 * AbstractJsonHandler.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package opex4j.core;

import com.google.gson.GsonBuilder;

import java.io.Serializable;

/**
 * Ancestor for objects that manage JSON representations of themselves.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public abstract class AbstractJsonHandler
  implements Serializable, JsonHandler {

  /**
   * Returns the object a JSON string (pretty-printed).
   *
   * @return		the object a JSON string
   */
  public String toString() {
    return toString(true);
  }

  /**
   * Turns the object into JSON.
   *
   * @param prettyPrint	whether to output the JSON in pretty printed style or space efficient
   * @return		the generated JSON
   */
  public String toString(boolean prettyPrint) {
    if (prettyPrint)
      return new GsonBuilder().setPrettyPrinting().create().toJson(toJson());
    else
      return new GsonBuilder().create().toJson(toJson());
  }
}
