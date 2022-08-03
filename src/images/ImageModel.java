package images;

import java.awt.image.BufferedImage;

/**
 * This interface contains all operations that all ImageModel should support.
 */
public interface ImageModel {

  /**
   * Load an image into the image model.
   *
   * @param filename the name of the file containing the image.
   * @throws IllegalArgumentException if the filename is invalid or if something
   *                                  goes wrong loading the image
   */
  void loadImage(String filename) throws IllegalArgumentException;

  /**
   * Save the data in the image model to a file.
   *
   * @param filename the name of the file to save to
   * @throws IllegalArgumentException if the filename is invalid or if something
   *                                  goes wrong saving the file
   */
  void saveImage(String filename) throws IllegalArgumentException;

  /**
   * Apply the blur filter to the data in the image model.
   */
  void applyBlur();

  /**
   * Apply the sharpen filter to the data in the image model.
   */
  void applySharpen();

  /**
   * Apply the grayscale color transformation to the data in the image model.
   */
  void applyGrayscale();

  /**
   * Apply the sepia color transformation to the data in the image model.
   */
  void applySepia();

  /**
   * Apply the dithering effect to the data in the image model.
   */
  void applyDither();

  /**
   * Apply the mosaic effect to the data in the image model.
   *
   * @param seeds the number of seeds to use in the mosaic
   * @throws IllegalArgumentException if the number of seeds is not positive
   */
  void applyMosaic(int seeds) throws IllegalArgumentException;

  /**
   * Produces a grayscale image where edges (areas of high contrast) are highlighted. .
   */
  void edgeDetection();

  /**
   * Increase the contrast of an image..
   */
  void grayscaleContractEnhancement();

  /**
   * Get current image.
   *
   * @return the current image.
   */

  BufferedImage getCurrentImage();

  /**
   * Get previous image.
   *
   * @return the original image.
   */
  BufferedImage getOriginalImage();

  /**
   * Undo previous step.
   */
  void undoOneStep();

  /**
   * Redo previous step.
   */
  void redoOneStep();

  /**
   * Reset to original image.
   */
  void resetToOrigin();
}
