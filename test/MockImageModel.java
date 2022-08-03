
import images.ImageModel;
import java.awt.image.BufferedImage;

/**
 * Mock models for the ConcreteImageModel.
 */
public class MockImageModel implements ImageModel {
  private StringBuilder log;

  /**
   * Constructor for the mock image model.
   *
   * @param log the log to use
   */
  public MockImageModel(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void loadImage(String filename) throws IllegalArgumentException {
    log.append("Load image").append(System.lineSeparator());

  }

  @Override
  public void saveImage(String filename) throws IllegalArgumentException {
    log.append("Save image").append(System.lineSeparator());
  }

  @Override
  public void applyBlur() {
    log.append("Apply blur").append(System.lineSeparator());
  }

  @Override
  public void applySharpen() {
    log.append("Apply sharpen").append(System.lineSeparator());

  }

  @Override
  public void applyGrayscale() {
    log.append("Apply grayscale").append(System.lineSeparator());
  }

  @Override
  public void applySepia() {
    log.append("Apply sepia").append(System.lineSeparator());

  }

  @Override
  public void applyDither() {
    log.append("Apply dither").append(System.lineSeparator());
  }

  @Override
  public void applyMosaic(int seeds) throws IllegalArgumentException {
    log.append("Apply mosaic").append(System.lineSeparator());
  }

  @Override
  public void edgeDetection() {

  }

  @Override
  public void grayscaleContractEnhancement() {

  }

  @Override
  public BufferedImage getCurrentImage() {
    return null;
  }

  @Override
  public BufferedImage getOriginalImage() {
    return null;
  }

  @Override
  public void undoOneStep() {

  }

  @Override
  public void redoOneStep() {

  }

  @Override
  public void resetToOrigin() {

  }
}
