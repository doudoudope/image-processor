package script;

import images.ImageModel;
import java.util.Scanner;

/**
 * Represents a Controller for Image model: handle user input files by executing them
 * using the model; save outcomes to the user in some form.
 */
public class ImageTextController implements ImageController {
  private ImageModel model;
  private ImageTextView view;
  private Readable input;

  /**
   * Execute a single file given an Image Model. When the execution
   * is over, a new image will be saved.
   *
   * @param model the model to use
   * @param input the source of input to use
   * @param view the view to show results
   */
  public ImageTextController(ImageModel model, Readable input, ImageTextView view) {
    if (input == null) {
      throw new IllegalArgumentException("Invalid argument passed to controller");
    }
    this.model = model;
    this.view = view;
    this.input = input;
  }

  @Override
  public void go() {
    try (Scanner in = new Scanner(input)) {
      while (in.hasNext()) {
        String input = in.nextLine();
        view.showMessage(input);
        String[] splitInput = input.split(" ");
        try {
          switch (splitInput[0]) {
            case "load":
              model.loadImage("res/" + splitInput[1]);
              continue;
            case "save":
              model.saveImage("res/" + splitInput[1]);
              continue;
            case "blur":
              model.applyBlur();
              continue;
            case "sharpen":
              model.applySharpen();
              continue;
            case "grayscale":
              model.applyGrayscale();
              continue;
            case "sepia":
              model.applySepia();
              continue;
            case "dither":
              model.applyDither();
              continue;
            case "mosaic":
              int seeds = Integer.parseInt(splitInput[1]);
              model.applyMosaic(seeds);
              continue;
            default:
              throw new IllegalArgumentException("Invalid Command");
          }
        } catch (IllegalArgumentException e) {
          view.showMessage("Error: " + e.getMessage()
                  + ". Command: '" + splitInput[0] + "'");
          break;
        }
      }
    }
  }
}
