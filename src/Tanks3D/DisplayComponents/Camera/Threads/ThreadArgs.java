package Tanks3D.DisplayComponents.Camera.Threads;

import Tanks3D.DisplayComponents.Camera.ObjectSlice;
import Tanks3D.GameData;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class ThreadArgs {
    //A struct that contains the necessary data about the game.
    private final GameData gameData;
    //An buffer that get written to, then displayed on the screen.
    private final BufferedImage canvas;
    //An array containing the wall slices that need to be drawn.
    private final ObjectSlice[] wallBuffer;
    //An array full of booleans indicating whether a pixel has been written to or not.
    private final Boolean[][] pixelTable;
    //
    private Point2D.Double position;
    //
    private double angle;

    public ThreadArgs(GameData gameData, BufferedImage canvas, ObjectSlice[] wallBuffer, Boolean[][] pixelTable, Point2D.Double position, double angle) {
        this.gameData = gameData;
        this.canvas = canvas;
        this.wallBuffer = wallBuffer;
        this.pixelTable = pixelTable;
        this.position = position;
        this.angle = angle;
    }
}
