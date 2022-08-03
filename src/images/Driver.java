package images;

/**
 * This class implements all the methods for ConcreteImageModel and generate the images
 * using the defined filter.
 */
public class Driver {

  /**
   * The main function implements all the methods.
   * @param args the input.
   */
  public static void main(String[] args) {

    ConcreteImageModel model = new ConcreteImageModel();

    model.loadImage("res/Unequalized_Hawkes_Bay_NZ.png");
    model.grayscaleContractEnhancement();
    model.saveImage("res/Equalized_Hawkes_Bay_NZ.png");


  }
}
