package script;

/**
 * Represents a Controller for Image model: handle user input files by executing them
 * using the model; save outcomes to the user in some form.
 */
public interface ImageController {

  /**
   * Execute a single file given an Image Model. When the execution
   * is over, a new image will be saved.
   */
  void go();
}
