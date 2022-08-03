package script;

import java.awt.image.BufferedImage;

/**
 * The interface for features.
 */
public interface Features {

  /**
   * Load an image from file.
   *
   * @param file the file to be loaded.
   */
  void loadImage(String file);

  /**
   * Save an image from file.
   *
   * @param file the file to be saved.
   */
  void saveImage(String file);

  /**
   * Process batch file.
   *
   * @param file the batch file to be processed.
   */
  void batchProcess(String file);

  /**
   * Apply blur filter.
   */
  void blur();

  /**
   * Apply sharpen filter.
   */
  void sharpen();

  /**
   * Apply grayscale filter.
   */
  void grayscale();

  /**
   * Apply sepia filter.
   */
  void sepia();

  /**
   * Apply dither filter.
   */
  void dither();

  /**
   * Apply mosaic filter.
   *
   * @param seeds the seeds of mosaic.
   */
  void mosaic(int seeds);

  /**
   * Apply edgeDetection.
   */
  void edgeDetection();

  /**
   * Apply grayscaleContractEnhancement.
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
  BufferedImage getOriginImage();

  /**
   * Undo previous step.
   *
   * @return true or false.
   */
  boolean undo();

  /**
   * Redo previous step.
   *
   * @return true or false.
   */
  boolean redo();

  /**
   * Reset image.
   *
   * @return true or false.
   */
  boolean reset();

  /**
   * Get user manual.
   *
   * @return string of user manual.
   */
  String getUserManual();

  /**
   * Get about me.
   *
   * @return string of about me.
   */
  String getAboutMe();
}
