/*
 * ObjectPredictionsTest.java
 * Copyright (C) 2023 University of Waikato, Hamilton, New Zealand
 */

package opex4j;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test the {@link ObjectPredictions} class.
 *
 * @author fracpete (fracpete at waikato dot ac dot nz)
 */
public class ObjectPredictionsTest {

  @Test
  public void testInstantiation() {
    assertThrows(IllegalArgumentException.class, () -> new ObjectPredictions(null, null, null));
    assertThrows(IllegalArgumentException.class, () -> new ObjectPredictions(null, "", null));
  }

  @Test
  public void testFromStream() throws Exception {
    InputStream stream;

    stream = getClass().getClassLoader().getResourceAsStream("opex4j/simple.json");
    if (stream != null) {
      ObjectPredictions preds = ObjectPredictions.newInstance(stream);
      stream.close();
      assertEquals(2, preds.getObjects().size(), "number of predictions");
    }
  }

  @Test
  public void testFromReader() throws Exception {
    InputStream stream;
    InputStreamReader reader;

    stream = getClass().getClassLoader().getResourceAsStream("opex4j/simple.json");
    if (stream != null) {
      reader = new InputStreamReader(stream);
      ObjectPredictions preds = ObjectPredictions.newInstance(reader);
      reader.close();
      stream.close();
      assertEquals(2, preds.getObjects().size(), "number of predictions");
    }
  }

  @Test
  public void testFromString() throws Exception {
    InputStream stream;
    StringBuilder json;
    int c;

    stream = getClass().getClassLoader().getResourceAsStream("opex4j/simple.json");
    if (stream != null) {
      json = new StringBuilder();
      while ((c = stream.read()) != -1)
        json.append((char) c);
      ObjectPredictions preds = ObjectPredictions.newInstance(json.toString());
      assertEquals(2, preds.getObjects().size(), "number of predictions");
    }
  }

  @Test
  public void testFromFile() throws Exception {
    ObjectPredictions preds = ObjectPredictions.newInstance(new File("src/test/resources/opex4j/simple.json"));
    assertEquals(2, preds.getObjects().size(), "number of predictions");
  }
}
