package Tanks3D.Utilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

public final class Image {
    //This class is non-instantiable
    private Image() {
    }

    //Load an image file from a directory.
    public static BufferedImage load(String imageFile) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(imageFile));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return image;
    }

    //Return a deep copy of the passed image.
    public static BufferedImage copy(BufferedImage image) {
        return new BufferedImage(image.getColorModel(), image.copyData(null), image.isAlphaPremultiplied(), null);
    }

    //Rotate the image and draw it at the given point. There are many ways rotate an image, but this was the fastest.
    public static void drawRotated(Graphics2D graphic, BufferedImage image, double angle, int x, int y, int width, int height) {
        //Create a new transform.
        AffineTransform transform = new AffineTransform();
        //Translate the transform to where the image will be drawn.
        transform.translate(x, y);
        //Rotate the transform.
        transform.rotate(Math.toRadians(angle), width/2.0, height/2.0);
        //Draw the tank.
        graphic.setTransform(transform);
        graphic.drawImage(image, 0, 0, width, height, null);
    }

    //Change the color of the pixel using its brightness.
    public static int tintABGRPixel(int pixelColor, Color tintColor) {
        //Calculate the luminance. The decimal values are pre-determined.
        int x = pixelColor>>16 & 0xff, y = pixelColor>>8 & 0xff, z = pixelColor & 0xff;
        int top = 2126*x + 7252*y + 722*z;
        int tempB = (int)((tintColor.getBlue() * top * 1766117501L) >> 52);
        int tempG = (int)((tintColor.getGreen() * top * 1766117501L) >> 52);
        int tempR = (int)((tintColor.getRed() * top * 1766117501L) >> 52);

        //Calculate the new tinted color of the pixel and return it.
        return ((pixelColor>>24 & 0xff) << 24) | tempB & 0xff | (tempG & 0xff) << 8 | (tempR & 0xff) << 16;
    }

    //Tint every pixel of an image.
    public static void tintImage(BufferedImage image, Color tintColor) {
        //Get the raw, live pixel data of the image.
        byte[] imagePixelData = getABGRColorData(image);

        //Loop through the image and change the tint of each color.
        for (int i = 0; i < image.getWidth(); i++)
            for (int j = 0; j < image.getHeight(); j++) {
                //Get the original color of the image.
                int originalColor = getABGRPixel(imagePixelData, image.getWidth(), i, j);
                //Tint the color and update the pixel.
                setABGRPixel(imagePixelData, image.getWidth(), i, j, tintABGRPixel(originalColor, tintColor));
            }
    }

    //Combine 4 bytes into one int.
    private static int byteToInt(byte byte1, byte byte2, byte byte3, byte byte4) {
        return (byte1 << 24) | (byte2 & 0xff) << 16 | (byte3 & 0xff) << 8 | (byte4 & 0xff);
    }

    //A faster alternative BufferedImage's setRGB method for images of type TYPE_INT_RGB or TYPE_4BYTE_ABGR.
    public static void setRGBPixel(int[] imagePixelData, int imageWidth, int x, int y, int pixelColor) {
        imagePixelData[y*imageWidth + x] = pixelColor;
    }
    public static void setABGRPixel(byte[] imagePixelData, int imageWidth, int x, int y, int pixelColor) {
        imagePixelData[(y*imageWidth+x)*4] = (byte)(pixelColor >>> 24);
        imagePixelData[(y*imageWidth+x)*4+1] = (byte)(pixelColor);
        imagePixelData[(y*imageWidth+x)*4+2] = (byte)(pixelColor >>> 8);
        imagePixelData[(y*imageWidth+x)*4+3] = (byte)(pixelColor >>> 16);
    }

    //A faster alternative BufferedImage's setRGB method for images of type TYPE_4BYTE_ABGR.
    public static int getABGRPixel(byte [] imagePixelData, int imageWidth, int x, int y) {
        return byteToInt(imagePixelData[(y*imageWidth+x)*4], imagePixelData[(y*imageWidth+x)*4+3], imagePixelData[(y*imageWidth+x)*4+2], imagePixelData[(y*imageWidth+x)*4+1]);
    }

    //Return the raw data of an image of type TYPE_INT_RGB. Each pixel is stored in 3 bytes: Red, Green, and Blue.
    public static int[] getRGBColorData(BufferedImage image) {
        if(image.getType() == BufferedImage.TYPE_INT_RGB)
            return ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

        return null;
    }

    //Return the raw data of an image of type TYPE_4BYTE_ABGR. Each pixel is stored in 4 bytes: Alpha, Blue, Green, and Red.
    public static byte[] getABGRColorData(BufferedImage image) {
        if(image.getType() == BufferedImage.TYPE_4BYTE_ABGR)
            return ((DataBufferByte)image.getRaster().getDataBuffer()).getData();

        return null;
    }
}