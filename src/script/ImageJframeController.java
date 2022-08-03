package script;

import images.ImageModel;
import imageview.ImageGuiView;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Implementation of image frame controller.
 */
public class ImageJframeController implements Features, ImageController {
  private ImageModel model;
  private ImageGuiView view;

  /**
   * Constructor.
   *
   * @param model the image model.
   * @param view the image gui view.
   */
  public ImageJframeController(ImageModel model, ImageGuiView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void loadImage(String file) {
    try {
      model.loadImage(file);
      view.showMessage("Load image from: " + file);
    } catch (IllegalArgumentException iae) {
      view.showMessage("Error: " + iae.getMessage());
    }
  }

  @Override
  public void saveImage(String file) {
    try {
      model.saveImage(file);
      view.showMessage("Save image to: " + file);
    } catch (IllegalArgumentException iae) {
      view.showMessage("Error: " + iae.getMessage());
    }
  }

  @Override
  public void batchProcess(String file) {
    Readable reader = null;
    try {
      reader = new FileReader(file);
    } catch (FileNotFoundException e) {
      view.showMessage("Error: " + e.getMessage());
    }

    try (Scanner in = new Scanner(reader)) {
      while (in.hasNext()) {
        String input = in.nextLine();
        view.showMessage(input);
        String[] splitInput = input.split(" ");
        String secondInput = "";
        String command = splitInput[0];

        for (int i = 0; i < splitInput.length; i++) {
          if (i > 0) {
            secondInput += " " + splitInput[i];
          }
        }

        try {
          switch (command) {
            case "load":
              loadImage(secondInput.trim());
              continue;
            case "save":
              saveImage(secondInput.trim());
              continue;
            case "blur":
              blur();
              continue;
            case "sharpen":
              sharpen();
              continue;
            case "grayscale":
              grayscale();
              continue;
            case "sepia":
              sepia();
              continue;
            case "dither":
              dither();
              continue;
            case "mosaic":
              int seeds = Integer.parseInt(secondInput.trim());
              mosaic(seeds);
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

  @Override
  public void blur() {
    try {
      view.showMessage("Apply Blur");
      model.applyBlur();
    } catch (IllegalStateException e) {
      view.showMessage(e.getMessage());
    }
  }

  @Override
  public void sharpen() {
    try {
      view.showMessage("Apply Sharpen");
      model.applySharpen();
    } catch (IllegalStateException e) {
      view.showMessage(e.getMessage());
    }
  }

  @Override
  public void grayscale() {
    try {
      view.showMessage("Apply Grayscale");
      model.applyGrayscale();
    } catch (IllegalStateException e) {
      view.showMessage(e.getMessage());
    }
  }

  @Override
  public void sepia() {
    try {
      view.showMessage("Apply Sepia");
      model.applySepia();
    } catch (IllegalStateException e) {
      view.showMessage(e.getMessage());
    }
  }

  @Override
  public void dither() {
    try {
      view.showMessage("Apply Dither");
      model.applyDither();
    } catch (IllegalStateException e) {
      view.showMessage(e.getMessage());
    }
  }

  @Override
  public void mosaic(int seeds) {
    try {
      view.showMessage("Applying Mosaic (" + seeds + ")");
      model.applyMosaic(seeds);
    } catch (IllegalStateException e) {
      view.showMessage(e.getMessage());
    } finally {
      view.showMessage("Mosaic Applied");
    }
  }

  @Override
  public void edgeDetection() {
    try {
      view.showMessage("Apply edgeDetection");
      model.edgeDetection();
    } catch (IllegalStateException e) {
      view.showMessage(e.getMessage());
    }
  }

  @Override
  public void grayscaleContractEnhancement() {
    try {
      view.showMessage("Apply grayscaleContractEnhancement");
      model.grayscaleContractEnhancement();
    } catch (IllegalStateException e) {
      view.showMessage(e.getMessage());
    }
  }

  @Override
  public BufferedImage getCurrentImage() {
    BufferedImage image = null;
    try {
      image = model.getCurrentImage();
    } catch (IllegalArgumentException e) {
      view.showMessage(e.getMessage());
    }
    return image;
  }

  @Override
  public BufferedImage getOriginImage() {
    BufferedImage image = null;
    try {
      image = model.getOriginalImage();
    } catch (IllegalArgumentException e) {
      view.showMessage(e.getMessage());
    }
    return image;
  }

  @Override
  public boolean undo() {
    try {
      model.undoOneStep();
      view.showMessage("Undo one step");
      return true;
    } catch (IllegalArgumentException e) {
      view.showMessage(e.getMessage());
      return false;
    }
  }

  @Override
  public boolean redo() {
    try {
      model.redoOneStep();
      view.showMessage("Redo one step");
      return true;
    } catch (IllegalArgumentException e) {
      view.showMessage(e.getMessage());
      return false;
    }
  }

  @Override
  public boolean reset() {
    try {
      model.resetToOrigin();
      view.showMessage("Reset to origin");
      return true;
    } catch (IllegalArgumentException e) {
      view.showMessage(e.getMessage());
      return false;
    }
  }

  @Override
  public String getUserManual() {
    return loadTextResource("UserManual.txt");
  }

  @Override
  public String getAboutMe() {
    return loadTextResource("AboutMe.txt");
  }

  @Override
  public void go() {
    view.addFeatures(this);
  }
  
  private String loadTextResource(String fileName) {
    StringBuilder contentBuilder = new StringBuilder();
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
         InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
         BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
         Stream<String> lines = bufferedReader.lines();
         ) {
      lines.forEach(s -> contentBuilder.append(s).append("\n"));
    } catch (IOException ioException) {
      view.showMessage(ioException.getMessage());
    }
    return contentBuilder.toString();
  }
}
