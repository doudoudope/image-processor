import static org.junit.Assert.assertEquals;

import images.ConcreteImageModel;
import images.ImageModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import org.junit.Test;
import script.ImageController;
import script.ImageTextController;
import script.ImageTextView;
import script.ImageTextViewImp;

/**
 * Test cases for the ImageModel controller, using mocks for readable and
 * appendable.
 */
public class ImageModelControllerTest {
  /** Test with a failing appendable. */
  @Test(expected = IllegalStateException.class)
  public void testFailingAppendable() {
    ImageModel m = new ConcreteImageModel();
    StringReader input = new StringReader("load");
    Appendable imageLog = new FailingAppendable();
    ImageTextView v = new ImageTextViewImp(imageLog);
    ImageController c = new ImageTextController(m, input, v);
    c.go();
  }


  @Test
  public void testValidFile() {
    StringBuilder modelLog = new StringBuilder();
    ImageModel m = new MockImageModel(modelLog);

    StringBuilder viewLog = new StringBuilder();
    ImageTextView v = new ImageTextViewImp(viewLog);

    String filename = "input.txt";
    File inputFile = new File("res/" + filename);

    try {
      Readable reader = new FileReader(inputFile);
      ImageController c = new ImageTextController(m, reader, v);
      c.go();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    String expectedModel = "Load image\n"
            + "Apply blur\n"
            + "Apply blur\n"
            + "Apply blur\n"
            + "Save image\n"
            + "Load image\n"
            + "Apply sharpen\n"
            + "Save image\n"
            + "Load image\n"
            + "Apply grayscale\n"
            + "Save image\n";
    assertEquals(expectedModel, modelLog.toString());
    String expectedView = "load flower.png\n"
            + "blur\n"
            + "blur\n"
            + "blur\n"
            + "save flower-blurred-3.png\n"
            + "load flower.png\n"
            + "sharpen\n"
            + "save flower-sharpen.png\n"
            + "load flower.png\n"
            + "grayscale\n"
            + "save flower-grayscale.png\n";
    assertEquals(expectedView, viewLog.toString());
  }

  /**
   * Load testNoneExistFilter.txt
   * The correct filter name should be "blur" but miss type as "blue"
   */
  @Test
  public void testNoneExistFilter() {
    StringBuilder modelLog = new StringBuilder();
    ImageModel m = new MockImageModel(modelLog);

    StringBuilder viewLog = new StringBuilder();
    ImageTextView v = new ImageTextViewImp(viewLog);

    String filename = "testNoneExistFilter.txt";
    File inputFile = new File("res/" + filename);

    try {
      Readable reader = new FileReader(inputFile);
      ImageController c = new ImageTextController(m, reader, v);
      c.go();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    String expectedModel = "Load image\n";
    assertEquals(expectedModel, modelLog.toString());
    String expectedView = "load flower.png\n"
            + "blue\n"
            + "Error: Invalid Command. Command: 'blue'\n";
    assertEquals(expectedView, viewLog.toString());
  }

  /**
   * Load testApplyFilterBeforeLoad.txt
   * Apply "blur" before loading image first
   */
  @Test
  public void testApplyFilterBeforeLoad() {
    StringBuilder modelLog = new StringBuilder();
    ImageModel m = new MockImageModel(modelLog);

    StringBuilder viewLog = new StringBuilder();
    ImageTextView v = new ImageTextViewImp(viewLog);

    String filename = "testApplyFilterBeforeLoad.txt";
    File inputFile = new File("res/" + filename);

    try {
      Readable reader = new FileReader(inputFile);
      ImageController c = new ImageTextController(m, reader, v);
      c.go();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    String expectedModel = "";
    assertEquals(expectedModel, modelLog.toString());
    String expectedView = "Blur\n"
            + "Error: Invalid Command. Command: 'Blur'\n";
    assertEquals(expectedView, viewLog.toString());
  }

  /**
   * Load testLoadWithoutFilename.txt
   * Load without filename
   */
  @Test
  public void testLoadWithoutFilename() {
    StringBuilder modelLog = new StringBuilder();
    ImageModel m = new MockImageModel(modelLog);

    StringBuilder viewLog = new StringBuilder();
    ImageTextView v = new ImageTextViewImp(viewLog);

    String filename = "testLoadWithoutFilename.txt";
    File inputFile = new File("res/" + filename);

    try {
      Readable reader = new FileReader(inputFile);
      ImageController c = new ImageTextController(m, reader, v);
      c.go();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException iae) {
      iae.printStackTrace();
    }
    String expectedModel = "";
    assertEquals(expectedModel, modelLog.toString());
    String expectedView = "load\n"
            + "Error: 1. Command: 'load'\n";
    assertEquals(expectedView, viewLog.toString());
  }

  /**
   * Load testLoadWithoutFilename.txt
   * Load without filename
   */
  @Test
  public void testInvalidMosaic() {
    StringBuilder modelLog = new StringBuilder();
    ImageModel m = new MockImageModel(modelLog);

    StringBuilder viewLog = new StringBuilder();
    ImageTextView v = new ImageTextViewImp(viewLog);


    String filename = "testInvalidMosaic.txt";
    File inputFile = new File("res/" + filename);

    try {
      Readable reader = new FileReader(inputFile);
      ImageController c = new ImageTextController(m, reader, v);
      c.go();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    String expectedModel = "Load image\n";
    assertEquals(expectedModel, modelLog.toString());
    String expectedView = "load flower.png\n"
           + "mosaic c\n"
           + "Error: For input string: \"c\". Command: 'mosaic'\n";
    assertEquals(expectedView, viewLog.toString());
  }

}

