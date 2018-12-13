package Tanks3D.DisplayComponents.Camera;

import Tanks3D.GameObject.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

//A 'struct' to contain the data needed to draw a slice of a wall or entity.
public class ObjectSlice {
    //The wall that this slice came from.
    public GameObject object;
    //The distance from the camera to the intersection between the ray and the wall.
    public double distToCamera;
    //The height of the object on the screen
    public double zPos;
    //The image that a slice will be taken out of.
    public BufferedImage image;
    //The pixel data of the image.
    public byte[] imagePixelData;
    //The color to tint the image.
    public Color imageColor;
    //How far along the wall the ray intersected. Between 0 and 1.
    public double intersectRatio;

    public ObjectSlice (GameObject wall, double distToCamera, double zPos, BufferedImage image, byte[] imagePixelData, Color imageColor, double intersectRatio) {
        this.object = wall;
        this.distToCamera = distToCamera;
        this.zPos = zPos;
        this.image = image;
        this.imagePixelData = imagePixelData;
        this.imageColor = imageColor;
        this.intersectRatio = intersectRatio;
    }
}