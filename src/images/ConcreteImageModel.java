package images;

import java.awt.image.BufferedImage;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import javafx.util.Pair;

/**
 * This class represents a ConcreteImageModel. It defines all the operations mandated by
 * the ImageModel interface.
 */
public class ConcreteImageModel implements ImageModel {
  private static int[][][] originalImage;
  private Stack<int[][][]> imageStack;
  private int[][][] undoTemp;

  /**
   * Constructs a ConcreteImageModel object.
   */
  public ConcreteImageModel() {
    imageStack = new Stack<>();
    originalImage = null;
  }

  /**
   * Load an image into the image model.
   *
   * @param filename the name of the file containing the image.
   * @throws IllegalArgumentException if the filename is invalid or if something
   *                                  goes wrong loading the image
   */
  @Override
  public void loadImage(String filename) throws IllegalArgumentException {
    if (filename == null || "".equals(filename)) {
      throw new IllegalArgumentException("Invalid filename provided for reading the image file");
    }
    originalImage = ImageUtilities.readImage(filename);
    imageStack.push(originalImage);
  }

  /**
   * Save the data in the image model to a file.
   *
   * @param filename the name of the file to save to
   * @throws IllegalArgumentException if the filename is invalid or if something
   *                                  goes wrong saving the file
   */
  @Override
  public void saveImage(String filename) throws IllegalArgumentException {
    if (imageStack.isEmpty()) {
      throw new IllegalStateException("No image has been loaded for processing");
    }

    if (filename == null || "".equals(filename)) {
      throw new IllegalArgumentException("Invalid filename provided");
    }

    ImageUtilities.writeImage(imageStack.peek(), filename);
  }

  /**
   * Apply the blur filter to the data in the image model.
   */
  @Override
  public void applyBlur() {
    if (imageStack.isEmpty()) {
      throw new IllegalStateException("No image has been loaded for processing");
    }

    double[][] blurMatrix = {
            {1 / 16d, 1 / 8d, 1 / 16d},
            {1 / 8d, 1 / 4d, 1 / 8d},
            {1 / 16d, 1 / 8d, 1 / 16d}
    };
    int[][][] image = imageStack.peek();
    int[][][] filteredImage = deepCopyImage(image);
    int centerH = blurMatrix.length / 2;
    int centerW = blurMatrix[centerH].length / 2;
    for (int h = 0; h < image.length; h++) {
      for (int w = 0; w < image[h].length; w++) {
        int newR = 0;
        int newG = 0;
        int newB = 0;
        for (int fh = 0; fh < blurMatrix.length; fh++) {
          for (int fw = 0; fw < blurMatrix[fh].length; fw++) {
            int shiftH = fh - centerH + h;
            int shiftW = fw - centerW + w;
            if (shiftH < 0 || shiftH >= image.length || shiftW < 0 || shiftW >= image[h].length) {
              continue;
            }
            newR += (int) (image[shiftH][shiftW][0] * blurMatrix[fh][fw]);
            newG += (int) (image[shiftH][shiftW][1] * blurMatrix[fh][fw]);
            newB += (int) (image[shiftH][shiftW][2] * blurMatrix[fh][fw]);
          }
        }
        filteredImage[h][w][0] = newR;
        filteredImage[h][w][1] = newG;
        filteredImage[h][w][2] = newB;
      }
    }

    imageStack.push(filteredImage);
  }

  /**
   * Apply the sharpen filter to the data in the image model.
   */
  @Override
  public void applySharpen() {
    if (imageStack.isEmpty()) {
      throw new IllegalStateException("No image has been loaded for processing");
    }

    double[][] sharpenMatrix = {
            {-1 / 8d, -1 / 8d, -1 / 8d, -1 / 8d, -1 / 8d},
            {-1 / 8d, 1 / 4d, 1 / 4d, 1 / 4d, -1 / 8d},
            {-1 / 8d, 1 / 4d, 1d, 1 / 4d, -1 / 8d},
            {-1 / 8d, 1 / 4d, 1 / 4d, 1 / 4d, -1 / 8d},
            {-1 / 8d, -1 / 8d, -1 / 8d, -1 / 8d, -1 / 8d}
    };
    int[][][] image = imageStack.peek();
    int[][][] filteredImage = deepCopyImage(image);
    int centerH = sharpenMatrix.length / 2;
    int centerW = sharpenMatrix[centerH].length / 2;
    for (int h = 0; h < image.length; h++) {
      for (int w = 0; w < image[h].length; w++) {
        int newR = 0;
        int newG = 0;
        int newB = 0;
        for (int fh = 0; fh < sharpenMatrix.length; fh++) {
          for (int fw = 0; fw < sharpenMatrix[fh].length; fw++) {
            int shiftH = fh - centerH + h;
            int shiftW = fw - centerW + w;
            if (shiftH < 0 || shiftH >= image.length || shiftW < 0 || shiftW >= image[h].length) {
              continue;
            }
            newR += (int) (image[shiftH][shiftW][0] * sharpenMatrix[fh][fw]);
            newG += (int) (image[shiftH][shiftW][1] * sharpenMatrix[fh][fw]);
            newB += (int) (image[shiftH][shiftW][2] * sharpenMatrix[fh][fw]);
          }
        }
        filteredImage[h][w][0] = newR;
        filteredImage[h][w][1] = newG;
        filteredImage[h][w][2] = newB;
      }
    }

    imageStack.push(filteredImage);
  }

  /**
   * Apply the grayscale color transformation to the data in the image model.
   */
  @Override
  public void applyGrayscale() {
    if (imageStack.isEmpty()) {
      throw new IllegalStateException("No image has been loaded for processing");
    }

    int[][][] image = deepCopyImage(imageStack.peek());
    grayscaleImage(image);

    imageStack.push(image);
  }

  /**
   * Apply the sepia color transformation to the data in the image model.
   */
  @Override
  public void applySepia() {
    if (imageStack.isEmpty()) {
      throw new IllegalStateException("No image has been loaded for processing");
    }

    int[][][] image = deepCopyImage(imageStack.peek());
    for (int h = 0; h < image.length; h++) {
      for (int w = 0; w < image[h].length; w++) {
        int newR = (int) (0.393d * image[h][w][0]
                + 0.769d * image[h][w][1]
                + 0.189d * image[h][w][2]);

        int newG = (int) (0.349d * image[h][w][0]
                + 0.686d * image[h][w][1]
                + 0.168d * image[h][w][2]);

        int newB = (int) (0.272d * image[h][w][0]
                + 0.534d * image[h][w][1]
                + 0.131d * image[h][w][2]);

        image[h][w][0] = newR > 255 ? 255 : newR;
        image[h][w][1] = newG > 255 ? 255 : newG;
        image[h][w][2] = newB > 255 ? 255 : newB;
      }
    }

    imageStack.add(image);
  }

  /**
   * Apply the dithering effect to the data in the image model.
   */
  @Override
  public void applyDither() {
    if (imageStack.isEmpty()) {
      throw new IllegalStateException("No image has been loaded for processing");
    }

    int[][][] image = deepCopyImage(imageStack.peek());
    // Greyscale the image
    grayscaleImage(image);

    // new_color = 0 or 255, whichever is closer to old_color
    for (int h = 0; h < image.length - 1; h++) {
      for (int w = 1; w < image[h].length - 1; w++) {
        float oldR = image[h][w][0];
        float oldG = image[h][w][1];
        float oldB = image[h][w][2];

        double newRedDouble = Math.round(image[h][w][0] / 255.0) * 255.0;
        double newGreenDouble = Math.round(image[h][w][1] / 255.0) * 255.0;
        double newBlueDouble = Math.round(image[h][w][2] / 255.0) * 255.0;
        image[h][w][0] = (int) newRedDouble;
        image[h][w][1] = (int) newGreenDouble;
        image[h][w][2] = (int) newBlueDouble;
        int newR = image[h][w][0];
        int newG = image[h][w][1];
        int newB = image[h][w][2];

        // calculate the error
        float errR = oldR - newR;
        float errG = oldG - newG;
        float errB = oldB - newB;

        //  add (7/16 * error) to pixel on the right (r,c+1)
        float r = image[h][w + 1][0];
        float g = image[h][w + 1][1];
        float b = image[h][w + 1][2];
        r = (float) (r + errR * (7 / 16.0));
        g = (float) (g + errG * (7 / 16.0));
        b = (float) (b + errB * (7 / 16.0));
        image[h][w + 1][0] = (int) r;
        image[h][w + 1][1] = (int) g;
        image[h][w + 1][2] = (int) b;

        // add (3/16 * error) to pixel on the next-row-left (r+1,c-1)
        r = image[h + 1][w - 1][0];
        g = image[h + 1][w - 1][1];
        b = image[h + 1][w - 1][2];
        r = (float) (r + errR * (3 / 16.0));
        g = (float) (g + errG * (3 / 16.0));
        b = (float) (b + errB * (3 / 16.0));
        image[h + 1][w - 1][0] = (int) r;
        image[h + 1][w - 1][1] = (int) g;
        image[h + 1][w - 1][2] = (int) b;

        //add (5/16 * error) to pixel below in next row (r+1,c)
        r = image[h + 1][w][0];
        g = image[h + 1][w][1];
        b = image[h + 1][w][2];
        r = (float) (r + errR * (5 / 16.0));
        g = (float) (g + errG * (5 / 16.0));
        b = (float) (b + errB * (5 / 16.0));
        image[h + 1][w][0] = (int) r;
        image[h + 1][w][1] = (int) g;
        image[h + 1][w][2] = (int) b;

        //add (1/16 * error) to pixel on the next-row-right (r+1,c+1)
        r = image[h + 1][w + 1][0];
        g = image[h + 1][w + 1][1];
        b = image[h + 1][w + 1][2];
        r = (float) (r + errR * (1 / 16.0));
        g = (float) (g + errG * (1 / 16.0));
        b = (float) (b + errB * (1 / 16.0));
        image[h + 1][w + 1][0] = (int) r;
        image[h + 1][w + 1][1] = (int) g;
        image[h + 1][w + 1][2] = (int) b;

      }
    }

    imageStack.push(image);
  }

  /**
   * Apply the mosaic effect to the data in the image model.
   *
   * @param seeds the number of seeds to use in the mosaic
   * @throws IllegalArgumentException if the number of seeds is not positive
   */
  @Override
  public void applyMosaic(int seeds) throws IllegalArgumentException {
    if (imageStack.isEmpty()) {
      throw new IllegalStateException("No image has been loaded for processing");
    }
    if (seeds <= 0 || seeds > originalImage.length * originalImage[0].length) {
      throw new IllegalArgumentException(("seeds is not valid"));
    }

    HashMap<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>> seedToMinDistPixelDict =
            new HashMap();

    int[][][] image = imageStack.peek();
    // Sample seed pixels
    while (seedToMinDistPixelDict.size() < seeds) {
      Pair<Integer, Integer> ranPixel = new Pair<>((int) (Math.random() * image.length),
              (int) (Math.random() * image[0].length));
      if (!seedToMinDistPixelDict.containsKey(ranPixel)) {
        seedToMinDistPixelDict.put(ranPixel, new ArrayList<>());
      }
    }

    for (int h = 0; h < image.length; h++) {
      for (int w = 0; w < image[h].length; w++) {
        Pair<Integer, Integer> curPixel = new Pair<>(h, w);
        Pair<Integer, Integer> minDistSeed = null;
        double minDist = distance(new Pair<>(0, 0), new Pair<>(image.length, image[0].length));

        for (Pair<Integer, Integer> seed : seedToMinDistPixelDict.keySet()) {
          double curDist = distance(curPixel, seed);
          if (curDist < minDist) {
            minDist = curDist;
            minDistSeed = seed;
          }
        }

        seedToMinDistPixelDict.get(minDistSeed).add(curPixel);
      }
    }

    int[][][] filteredImage = deepCopyImage(image);
    for (Pair<Integer, Integer> seed : seedToMinDistPixelDict.keySet()) {
      int[] aveColor = averageColor(seedToMinDistPixelDict.get(seed), image);
      for (Pair<Integer, Integer> p : seedToMinDistPixelDict.get(seed)) {
        filteredImage[p.getKey()][p.getValue()] = aveColor;
      }
    }

    imageStack.push(filteredImage);
  }

  @Override
  public void edgeDetection() {
    if (imageStack.isEmpty()) {
      throw new IllegalStateException("No image has been loaded for processing");
    }

    int[][][] image = imageStack.peek();
    int[][][] filteredImage = deepCopyImage(imageStack.peek());

    double[][] kx = { {1, 0, -1}, {2, 0, -2}, {1, 0, -1} };
    double[][] ky = { {1, 2, 1}, {0, 0, 0}, {-1, -2, -1} };
    int[][][] gxImage = applyKernel(kx, image);
    int[][][] gyImage = applyKernel(ky, image);

    int max = 0;
    int min = 255;
    for (int h = 0; h < image.length; h++) {
      for (int w = 0; w < image[h].length; w++) {
        int newRx = gxImage[h][w][0];
        int newGx = gxImage[h][w][1];
        int newBx = gxImage[h][w][2];

        int newRy = gyImage[h][w][0];
        int newGy = gyImage[h][w][1];
        int newBy = gyImage[h][w][2];

        int r = (int) Math.sqrt(newRx * newRx + newRy * newRy);
        int g = (int) Math.sqrt(newGx * newGx + newGy * newGy);
        int b = (int) Math.sqrt(newBx * newBx + newBy * newBy);

        max = Math.max(r, Math.max(g, Math.max(b, max)));
        min = Math.min(r, Math.min(g, Math.min(b, min)));

        filteredImage[h][w] = new int[] {r, g, b};
      }
    }

    for (int h = 0; h < filteredImage.length; h++) {
      for (int w = 0; w < filteredImage[0].length; w++) {
        int r = filteredImage[h][w][0];
        int g = filteredImage[h][w][1];
        int b = filteredImage[h][w][2];

        filteredImage[h][w][0] = ((r - min) * 255) / (max - min);
        filteredImage[h][w][1] = ((g - min) * 255) / (max - min);
        filteredImage[h][w][2] = ((b - min) * 255) / (max - min);
      }
    }

    // Greyscale the image
    grayscaleImage(filteredImage);
    imageStack.add(filteredImage);
  }

  @Override
  public void grayscaleContractEnhancement() {
    if (imageStack.isEmpty()) {
      throw new IllegalStateException("No image has been loaded for processing");
    }

    int[][][] filteredImage = deepCopyImage(imageStack.peek());
    grayscaleImage(filteredImage);

    HashMap<Integer, ArrayList<int[]>> histogramMap = constructHistogramMap(filteredImage);
    int accumulator = 0;
    int totalPixels = filteredImage.length * filteredImage[0].length;
    for (int intensity : histogramMap.keySet()) {
      ArrayList<int[]> pixels = histogramMap.get(intensity);
      accumulator += pixels.size();
      int equalizedIntensity = 255 * accumulator / totalPixels;
      for (int[] p : pixels) {
        filteredImage[p[0]][p[1]] =
                new int[] {equalizedIntensity, equalizedIntensity, equalizedIntensity};
      }
    }

    imageStack.add(filteredImage);
  }

  @Override
  public BufferedImage getCurrentImage() {
    if (imageStack.isEmpty()) {
      throw new IllegalArgumentException("No image has been loaded for processing");
    }

    return ImageUtilities.convertImage(imageStack.peek());
  }

  @Override
  public BufferedImage getOriginalImage() {
    if (imageStack.isEmpty()) {
      throw new IllegalArgumentException("No image has been loaded for processing");
    }

    return ImageUtilities.convertImage(imageStack.firstElement());
  }

  @Override
  public void undoOneStep() {
    if (imageStack.isEmpty()) {
      throw new IllegalStateException("No image has been loaded for processing");
    }

    if (imageStack.size() > 1) {
      undoTemp = imageStack.pop();
    } else {
      throw new IllegalStateException("Can't undo any more");
    }
  }

  @Override
  public void redoOneStep() {
    if (imageStack.isEmpty()) {
      throw new IllegalStateException("No image has been loaded for processing");
    }

    if (undoTemp != null) {
      imageStack.push(undoTemp);
      undoTemp = null;
    } else {
      throw new IllegalStateException("You can only redo one step");
    }
  }

  @Override
  public void resetToOrigin() {
    if (originalImage == null) {
      throw new IllegalStateException("No image has been loaded for processing");
    }

    undoTemp = imageStack.peek();
    imageStack = new Stack<>();
    imageStack.push(originalImage);
  }

  private double distance(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
    return Math.sqrt((p1.getKey() - p2.getKey()) * (p1.getKey() - p2.getKey())
            + (p1.getValue() - p2.getValue()) * (p1.getValue() - p2.getValue()));
  }

  private int[] averageColor(ArrayList<Pair<Integer, Integer>> pixels, int[][][] image) {
    int aveR = 0;
    int aveG = 0;
    int aveB = 0;
    int size = pixels.size();
    for (Pair<Integer, Integer> p : pixels) {
      aveR += image[p.getKey()][p.getValue()][0];
      aveG += image[p.getKey()][p.getValue()][1];
      aveB += image[p.getKey()][p.getValue()][2];
    }
    return new int[] {aveR / size, aveG / size, aveB / size};
  }

  private int[][][] deepCopyImage(int[][][] image) {
    int[][][] copyImage = new int[image.length][image[0].length][image[0][0].length];
    for (int h = 0; h < image.length; h++) {
      for (int w = 0; w < image[h].length; w++) {
        copyImage[h][w] = Arrays.copyOf(image[h][w], image[0][0].length);
      }
    }
    return copyImage;
  }

  private int[][][] applyKernel(double[][] matrix, int[][][] image) {
    int[][][] filteredImage = deepCopyImage(image);
    int centerH = matrix.length / 2;
    int centerW = matrix[centerH].length / 2;
    for (int h = 0; h < image.length; h++) {
      for (int w = 0; w < image[h].length; w++) {
        int newR = 0;
        int newG = 0;
        int newB = 0;
        for (int fh = 0; fh < matrix.length; fh++) {
          for (int fw = 0; fw < matrix[fh].length; fw++) {
            int shiftH = fh - centerH + h;
            int shiftW = fw - centerW + w;
            if (shiftH < 0 || shiftH >= image.length || shiftW < 0 || shiftW >= image[h].length) {
              continue;
            }
            newR += (int) (image[shiftH][shiftW][0] * matrix[fh][fw]);
            newG += (int) (image[shiftH][shiftW][1] * matrix[fh][fw]);
            newB += (int) (image[shiftH][shiftW][2] * matrix[fh][fw]);
          }
        }
        filteredImage[h][w][0] = newR;
        filteredImage[h][w][1] = newG;
        filteredImage[h][w][2] = newB;
      }
    }

    return filteredImage;
  }

  private void grayscaleImage(int[][][] image) {
    for (int h = 0; h < image.length; h++) {
      for (int w = 0; w < image[h].length; w++) {
        int newVal = getIntensity(image[h][w]);
        image[h][w][0] = newVal;
        image[h][w][1] = newVal;
        image[h][w][2] = newVal;
      }
    }
  }

  // this method produces monochrome luminance
  private int getIntensity(int[] color) {
    if (color.length != 3) {
      throw new IllegalArgumentException();
    }

    int r = color[0];
    int g = color[1];
    int b = color[2];
    int newVal = (int) (0.2126d * r + 0.7152d * g + 0.0722d * b);
    return newVal > 255 ? 255 : newVal;
  }

  private HashMap<Integer, ArrayList<int[]>> constructHistogramMap(int[][][] image) {
    HashMap<Integer, ArrayList<int[]>> histogramMap = new HashMap<>();
    for (int i = 0; i < 256; i++) {
      histogramMap.put(i, new ArrayList<>());
    }

    for (int h = 0; h < image.length; h++) {
      for (int w = 0; w < image[h].length; w++) {
        int intensity = image[h][w][0];
        histogramMap.get(intensity).add(new int[] {h, w});
      }
    }
    return histogramMap;
  }
}
