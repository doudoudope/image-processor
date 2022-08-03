import images.ConcreteImageModel;
import images.ImageModel;
import imageview.ImageGuiView;
import imageview.ImageGuiViewImp;
import script.ImageController;
import script.ImageJframeController;

/**
 * Driver class for gui-based image process program.
 */
public class Main {
  /**
   * The starting point for this example.
   *
   * @param args Not used
   */
  public static void main(String[] args) {
    // Create the model
    ImageModel model = new ConcreteImageModel();
    // Create the view
    ImageGuiView view = new ImageGuiViewImp("Best Image Processor");
    // Create the controller with the model
    ImageController controller = new ImageJframeController(model, view);
    controller.go();
  }
}
