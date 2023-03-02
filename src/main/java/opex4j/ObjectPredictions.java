/*
 * ObjectPredictions.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package opex4j;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import opex4j.core.AbstractJsonHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages one or more {@link ObjectPrediction}.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class ObjectPredictions
  extends AbstractJsonHandler {

  /** the format for the timestamp (Python: %Y%m%d_%H%M%S.%f). */
  public final static String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss.SSSSSS";

  /** the formatter to use. */
  public final static DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);

  /** the (alternative) format for the timestamp (Python: %Y-%m-%d %H:%M:%S.%f). */
  public final static String TIMESTAMP_FORMAT_ALT = "yyyy-MM-dd HH:mm:ss.SSSSSS";

  /** the (alternative) formatter to use. */
  public final static DateTimeFormatter TIMESTAMP_FORMATTER_ALT = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT_ALT);

  /** the timestamp. */
  protected LocalDateTime m_Timestamp;

  /** the ID. */
  protected String m_ID;

  /** the objects. */
  protected List<ObjectPrediction> m_Objects;

  /** meta-data. */
  protected Map<String,String> m_Meta;

  /**
   * Initializes the predictions.
   *
   * @param timestamp	the timestamp, can be null
   * @param id		the ID
   * @param objects	the predictions, can be null
   */
  public ObjectPredictions(LocalDateTime timestamp, String id, List<ObjectPrediction> objects) {
    this(timestamp, id, objects, null);
  }

  /**
   * Initializes the predictions.
   *
   * @param timestamp	the timestamp, can be null
   * @param id		the ID
   * @param objects	the predictions, can be null
   * @param meta 	the meta-data, can be null
   */
  public ObjectPredictions(LocalDateTime timestamp, String id, List<ObjectPrediction> objects, Map<String,String> meta) {
    if ((id == null) || id.isEmpty())
      throw new IllegalArgumentException("ID cannot be null or empty!");
    m_Timestamp = timestamp;
    m_ID        = id;
    m_Objects   = (objects == null) ? new ArrayList<>() : new ArrayList<>(objects);
    m_Meta      = (meta == null) ? new HashMap<>() : new HashMap<>(meta);
  }

  /**
   * Returns the timestamp.
   *
   * @return		the timestamp, can be null
   */
  public LocalDateTime getTimestamp() {
    return m_Timestamp;
  }

  /**
   * Returns the timestamp as string.
   *
   * @return		the timestamp, can be null
   */
  public String getTimestampStr() {
    if (m_Timestamp != null)
      return TIMESTAMP_FORMATTER.format(m_Timestamp);
    else
      return null;
  }

  /**
   * Returns the ID.
   *
   * @return		the ID
   */
  public String getID() {
    return m_ID;
  }

  /**
   * Returns the predictions.
   *
   * @return		the predictions
   */
  public List<ObjectPrediction> getObjects() {
    return m_Objects;
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
    JsonArray	objects;
    JsonObject	meta;

    result = new JsonObject();

    if (m_Timestamp != null)
      result.addProperty("timestamp", TIMESTAMP_FORMATTER.format(m_Timestamp));

    result.addProperty("id", m_ID);

    objects = new JsonArray();
    for (ObjectPrediction obj: m_Objects)
      objects.add(obj.toJson());
    result.add("objects", objects);

    if (m_Meta.size() > 0) {
      meta = new JsonObject();
      for (String key: m_Meta.keySet())
	meta.addProperty(key, m_Meta.get(key));
      result.add("meta", meta);
    }

    return result;
  }

  /**
   * Writes the predictions to the specified file.
   * Uses pretty printing.
   *
   * @param file		the file to write to
   * @throws IOException	if writing fails
   */
  public void write(File file) throws IOException {
    write(file, true);
  }

  /**
   * Writes the predictions to the specified file.
   *
   * @param file		the file to write to
   * @param prettyPrint		whether to use pretty printing or not
   * @throws IOException	if writing fails
   */
  public void write(File file, boolean prettyPrint) throws IOException {
    FileWriter		fwriter;
    BufferedWriter	bwriter;

    fwriter = null;
    bwriter = null;
    try {
      fwriter = new FileWriter(file);
      bwriter = new BufferedWriter(fwriter);
      bwriter.write(toString(prettyPrint));
      bwriter.flush();
    }
    finally {
      if (bwriter != null) {
	try {
	  bwriter.close();
	}
	catch (Exception e) {
	  // ignored
	}
      }
      if (fwriter != null) {
	try {
	  fwriter.close();
	}
	catch (Exception e) {
	  // ignored
	}
      }
    }
  }

  /**
   * Writes the predictions to the supplied writer.
   * Uses pretty printing.
   * Caller must close the writer.
   *
   * @param writer		the writer to write to
   * @throws IOException	if writing fails
   */
  public void write(Writer writer) throws IOException {
    write(writer, true);
  }

  /**
   * Writes the predictions to the supplied writer.
   * Caller must close the writer.
   *
   * @param writer		the writer to write to
   * @param prettyPrint		whether to use pretty printing or not
   * @throws IOException	if writing fails
   */
  public void write(Writer writer, boolean prettyPrint) throws IOException {
    writer.write(toString(prettyPrint));
  }

  /**
   * Writes the predictions to the supplied stream.
   * Uses pretty printing.
   * Caller must close the stream.
   *
   * @param stream		the stream to write to
   * @throws IOException	if writing fails
   */
  public void write(OutputStream stream) throws IOException {
    write(stream, true);
  }

  /**
   * Writes the predictions to the supplied stream.
   * Caller must close the stream.
   *
   * @param stream		the stream to write to
   * @param prettyPrint		whether to use pretty printing or not
   * @throws IOException	if writing fails
   */
  public void write(OutputStream stream, boolean prettyPrint) throws IOException {
    stream.write(toString(prettyPrint).getBytes());
  }

  /**
   * Writes the predictions to the supplied StringBuilder instance.
   * Uses pretty printing.
   * Caller must close the stream.
   *
   * @param builder		the builder to write to
   */
  public void write(StringBuilder builder) {
    write(builder, true);
  }

  /**
   * Writes the predictions to the supplied StringBuilder instance.
   * Caller must close the stream.
   *
   * @param builder		the builder to write to
   * @param prettyPrint		whether to use pretty printing or not
   */
  public void write(StringBuilder builder, boolean prettyPrint) {
    builder.append(toString(prettyPrint));
  }

  /**
   * Generates object predictions from JSON.
   *
   * @param obj		the JSON to use
   * @return		the object predictions
   */
  public static ObjectPredictions newInstance(JsonObject obj) {
    LocalDateTime		timestamp;
    String			timestampStr;
    String			id;
    List<ObjectPrediction>	objects;
    JsonArray			jobjects;
    Map<String,String>		meta;
    JsonObject			jmeta;
    int				i;

    timestamp = null;
    if (obj.has("timestamp")) {
      timestampStr = obj.get("timestamp").getAsString();
      try {
	timestamp = LocalDateTime.parse(timestampStr, TIMESTAMP_FORMATTER);
      }
      catch (Exception e) {
	try {
	  timestamp = LocalDateTime.parse(timestampStr, TIMESTAMP_FORMATTER_ALT);
	}
	catch (Exception e2) {
	  System.err.println("Failed to parse timestamp: " + obj);
	}
      }
    }

    id = obj.get("id").getAsString();

    jobjects = obj.get("objects").getAsJsonArray();
    objects = new ArrayList<>();
    for (i = 0; i < jobjects.size(); i++)
      objects.add(ObjectPrediction.newInstance(jobjects.get(i).getAsJsonObject()));

    meta = null;
    if (obj.has("meta")) {
      jmeta = obj.get("meta").getAsJsonObject();
      meta  = new HashMap<>();
      for (String key: jmeta.keySet())
	meta.put(key, jmeta.get(key).getAsString());
    }

    return new ObjectPredictions(timestamp, id, objects, meta);
  }

  /**
   * Loads the objects predictions from the specified JSON file.
   *
   * @param file	the file to load
   * @return		the object predictions
   * @throws Exception	if reading/parsing fails
   */
  public static ObjectPredictions newInstance(File file) throws Exception {
    FileReader		freader;
    BufferedReader	breader;

    freader = null;
    breader = null;
    try {
      freader = new FileReader(file);
      breader = new BufferedReader(freader);
      return ObjectPredictions.newInstance((JsonObject) JsonParser.parseReader(breader));
    }
    finally {
      if (breader != null) {
	try {
	  breader.close();
	}
	catch (Exception e) {
	  // ignored
	}
      }
      if (freader != null) {
	try {
	  freader.close();
	}
	catch (Exception e) {
	  // ignored
	}
      }
    }
  }

  /**
   * Loads the objects predictions from the supplied reader.
   *
   * @param reader	the reader to use
   * @return		the object predictions
   * @throws Exception	if reading/parsing fails
   */
  public static ObjectPredictions newInstance(Reader reader) throws Exception {
    return ObjectPredictions.newInstance((JsonObject) JsonParser.parseReader(reader));
  }

  /**
   * Loads the objects predictions from the supplied stream.
   *
   * @param stream	the stream to use
   * @return		the object predictions
   * @throws Exception	if reading/parsing fails
   */
  public static ObjectPredictions newInstance(InputStream stream) throws Exception {
    return ObjectPredictions.newInstance((JsonObject) JsonParser.parseReader(new InputStreamReader(stream)));
  }

  /**
   * Loads the objects predictions from the JSON string.
   *
   * @param s		the string to parse
   * @return		the object predictions
   * @throws Exception	if reading/parsing fails
   */
  public static ObjectPredictions newInstance(String s) throws Exception {
    return ObjectPredictions.newInstance((JsonObject) JsonParser.parseReader(new StringReader(s)));
  }
}
