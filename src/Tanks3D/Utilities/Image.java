package Tanks3D.Utilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class Image {
    //This class is non-instantiable
    private Image() {
    }

    public static BufferedImage load(String imageFile) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(imageFile));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return image;
    }

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

    public static int tintPixel(Color pixelColor, Color tintColor) {
        //Calculate the luminance. These values are pre-determined.
        double lum = (pixelColor.getRed() * 0.2126 + pixelColor.getGreen() * 0.7152 + pixelColor.getBlue() * 0.0722) / 255;

        //Calculate the new tinted color of the pixel and return it.
        return new Color((int) (tintColor.getRed() * lum), (int) (tintColor.getGreen() * lum), (int) (tintColor.getBlue() * lum), pixelColor.getAlpha()).getRGB();
    }

    public static void tintImage(BufferedImage image, Color tintColor) {
        //Loop through the image and change the tint of each color.
        for (int i = 0; i < image.getWidth(); i++)
            for (int j = 0; j < image.getHeight(); j++)
                //Get the color of the pixel, tint it, and write the new color to the pixel.
                image.setRGB(i, j, tintPixel(new Color(image.getRGB(i, j), true), tintColor));
    }
}
