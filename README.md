# opex4j
Java library for the [OPEX JSON format](https://github.com/WaikatoLink2020/objdet-predictions-exchange-format).

## JSON

Here is an [example](src/test/resources/opex4j/simple.json):

```json
{
  "timestamp": "yyyyMMdd_HHmmss.SSSSSS",
  "id": "str",
  "objects": [
    {
      "score": 1.0,
      "label": "person",
      "bbox": {
        "top": 100,
        "left": 100,
        "bottom": 150,
        "right": 120
      },
      "polygon": {
        "points": [
          [100, 100],
          [150, 100],
          [150, 120],
          [100, 120]
        ]
      }
    },
    {
      "score": 0.95,
      "label": "house",
      "bbox": {
        "top": 100,
        "left": 100,
        "bottom": 200,
        "right": 200
      },
      "polygon": {
        "points": [
          [100, 100],
          [200, 100],
          [200, 200],
          [100, 200]
        ]
      },
      "meta": {
        "price": "500k"
      }
    }
  ],
  "meta": {
    "key1": "value1",
    "key2": "value2"
  }
}
```


## Reading/Writing

To read predictions:

```java
import opex4j.ObjectPredictions;
import java.io.InputStream;
import java.io.File;
import java.io.Reader;

public class Examples {

  public static void main(String[] args) throws Exception {
    // from string
    ObjectPredictions preds = ObjectPredictions.newInstance("{...}");

    // from file
    ObjectPredictions preds = ObjectPredictions.newInstance(new File("predictions.json"));

    // from reader
    Reader reader = ...;
    ObjectPredictions preds = ObjectPredictions.newInstance(reader);
    reader.close();
 
    // from stream
    InputStream stream = ...;
    ObjectPredictions preds = ObjectPredictions.newInstance(stream);
    stream.close();
  }
}
```

To write predictions:

```java
import opex4j.ObjectPredictions;
import java.io.File;
import java.io.OutputStream;
import java.io.Writer;

public class Examples {

  public static void main(String[] args) throws Exception {
    ObjectPredictions preds = ...;

    // to string (not pretty printed)
    String s = preds.toString(false);

    // to file (pretty printed, implicitly)
    ObjectPredictions.write(new File("predictions.json"));

    // to writer (pretty printed, explicitly)
    Writer writer = ...;
    ObjectPredictions.write(writer, true);
    writer.close();
 
    // to stream (not pretty printed)
    OutputStream stream = ...;
    preds.write(stream, false);
    stream.close();
  }
}
```


## Maven

Use the following dependency in your `pom.xml`:

```xml
    <dependency>
      <groupId>nz.ac.waikato.cms.adams</groupId>
      <artifactId>opex4j</artifactId>
      <version>0.0.1</version>
    </dependency>
```
