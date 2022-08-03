package script;

import java.io.IOException;
import java.util.Scanner;

/**
 * The implementation of image text view.
 */
public class ImageTextViewImp implements ImageTextView {
  private Appendable out;

  /**
   * Constructor for the view.
   *
   * @param out the output to use
   * @throws IllegalArgumentException for invalid parameter values.
   */
  public ImageTextViewImp(Appendable out) throws IllegalArgumentException {
    if (out == null) {
      throw new IllegalArgumentException("Invalid parameter passed to view.");
    }

    this.out = out;
  }

  @Override
  public void showMessage(String msg) {
    try {
      out.append(msg);
      out.append(System.lineSeparator());
    } catch (IOException ex) {
      throw new IllegalStateException("Error occurred writing to view");
    }
  }
}
