# Image Program

This is the starting readme for Image Program.  

- Name: YE ZHANG

- Description: Image processing program using Java


## About/Overview
---

Many applications uses color images. In this program, you can change the appearance of images by applying different filters on them. You can do this by running this program where you could specify that you want to load an image, apply various effects to it, and save the result. 

## List of Features
---

* loadImage. Load an image into the image model.
* saveImage. Save the data in the image model to a file.
* applyBlur filter. Apply the blur filter to the data in the image.
* applySharpen filter. Apply the sharpen filter to the data in the image.
* applyGrayscale filter. Apply the grayscale color transformation to the data in the image.
* applySepia filter. Apply the sepia color transformation to the data in the image.
* applyDither filter. Apply the dithering effect to the data in the image.
* applyMosaic filter. Apply the mosaic effect to the data in the image.
* edgeDetection. Edge detection produces a grayscale image where edges (areas of high contrast) are highlighted.
* grayscaleContractEnhancement. GrayscaleContractEnhancement increase the contrast of an image.

## Limitations
---
* This program can only deal with the above eight image filters. 
* If you want to apply a mosaic filter to the image, you need to provide the number you want the image to be "broken down," namely seeds. However, if the number (seeds) you provide is too big, for example, 100000, it may take a very long time to run.

## How to Run
---
### For Gui(Jar)
* Using shell/cmd and navigate to res/ directory. 
* Run 'java -jar ImageProcessor.jar'


### For Script Run:
* Put the original images in the res/ directory.
* Create a .txt script file in res/ directory.
* Provide the .txt script file to the program. When running your main method from your IDE, you will need to change the "Run Configuration" by adding the file into the program arguments so that if you type "input.txt" there, save changes and run your program, then "input.txt" is available as args[0] in your public static void main(String[] args) method.
* You can find the filtered images in the res/ directory.

## How to Use the Program
---
* Please type the script in lower cases.
* Please keep in mind the order of your script. You need to load an image before adding filter to it. 
* Be sure to name each file appropriately so you can easily determine what each image file is.
* If you want to apply mosaic filter to the image, be sure to add "seeds" to it. Seeds should be an integer.
* Please type a space between "load" and "filename", "save" and "filename", "mosaic" and "seeds".
* Be sure to type the filename and the filter name correctly.

## Examples
---
load flower.png

blur

blur

save flower-blurred-2.png

sepia

save flower-blurred-sepia.png

load flower.png

grayscale

save flower-grayscale.png

load flower.png

blur

mosaic 8000

save flower-mosaic-8000.png

...

## Citations
---
Professional Internet Site

[1] The Java™ Tutorials":The switch Statement.[Online], Avaliable: https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html. [Accessed Mar.30, 2022].

I used the switch statement in script.ImageTextController. I took the format of the switch statements but changed "break" to "continue" to make the program work as I want.


