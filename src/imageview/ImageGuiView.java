package imageview;

import java.awt.event.ActionListener;
import script.Features;

/**
 * The interface for our view class.
 */
public interface ImageGuiView {

  /**
   * Add features.
   *
   * @param features features to be added.
   */
  void addFeatures(Features features);

  /**
   * Show message.
   * @param message the message to be showed.
   */
  void showMessage(String message);
}
