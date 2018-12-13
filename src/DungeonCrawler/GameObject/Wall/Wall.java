package DungeonCrawler.GameObject.Wall;

import DungeonCrawler.GameObject.GameObject;
import DungeonCrawler.Utilities.Image;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Wall extends GameObject {
    //A hash map that maps texture names to the corresponding image file.
    private static final HashMap<String, BufferedImage> textureImageList;
    //A hash map that maps texture names to the corresponding pixel data.
    private static final HashMap<String, byte[]> textureDataList;

    private BufferedImage texture;
    private byte[] texturePixelData;
    private Color textureColor;
    protected Line2D.Double line;
    protected double length;
    protected double angle;
    protected boolean seeThrough;
    protected boolean characterCollidable;
    protected boolean projectileCollidable;
    private final static double height = 20;

    //Load all of the textures.
    static {
        //Instantiate the file and data hash maps.
        textureImageList = new HashMap<>();
        textureDataList = new HashMap<>();

        //Load the image files and pixel data for each texture.
        textureImageList.put("Brick", Image.load("resources/Textures/Brick.png"));
        textureDataList.put("Brick", Image.getABGRColorData(textureImageList.get("Brick")));
        textureImageList.put("Dirt 1", Image.load("resources/Textures/Dirt 1.png"));
        textureDataList.put("Dirt 1", Image.getABGRColorData(textureImageList.get("Dirt 1")));
        textureImageList.put("Dirt 2", Image.load("resources/Textures/Dirt 2.png"));
        textureDataList.put("Dirt 2", Image.getABGRColorData(textureImageList.get("Dirt 2")));
        textureImageList.put("Fence", Image.load("resources/Textures/Fence.png"));
        textureDataList.put("Fence", Image.getABGRColorData(textureImageList.get("Fence")));

        textureImageList.put("Brick Damage 0", Image.load("resources/Textures/Cracked Brick/Brick Damage 0.png"));
        textureDataList.put("Brick Damage 0", Image.getABGRColorData(textureImageList.get("Brick Damage 0")));
        textureImageList.put("Brick Damage 1", Image.load("resources/Textures/Cracked Brick/Brick Damage 1.png"));
        textureDataList.put("Brick Damage 1", Image.getABGRColorData(textureImageList.get("Brick Damage 1")));
        textureImageList.put("Brick Damage 2", Image.load("resources/Textures/Cracked Brick/Brick Damage 2.png"));
        textureDataList.put("Brick Damage 2", Image.getABGRColorData(textureImageList.get("Brick Damage 2")));
        textureImageList.put("Brick Damage 3", Image.load("resources/Textures/Cracked Brick/Brick Damage 3.png"));
        textureDataList.put("Brick Damage 3", Image.getABGRColorData(textureImageList.get("Brick Damage 3")));

        textureImageList.put("Hole Damage 0", Image.load("resources/Textures/Blocked Hole/Hole Damage 0.png"));
        textureDataList.put("Hole Damage 0", Image.getABGRColorData(textureImageList.get("Hole Damage 0")));
        textureImageList.put("Hole Damage 1", Image.load("resources/Textures/Blocked Hole/Hole Damage 1.png"));
        textureDataList.put("Hole Damage 1", Image.getABGRColorData(textureImageList.get("Hole Damage 1")));
        textureImageList.put("Hole Damage 2", Image.load("resources/Textures/Blocked Hole/Hole Damage 2.png"));
        textureDataList.put("Hole Damage 2", Image.getABGRColorData(textureImageList.get("Hole Damage 2")));
        textureImageList.put("Hole Damage 3", Image.load("resources/Textures/Blocked Hole/Hole Damage 3.png"));
        textureDataList.put("Hole Damage 3", Image.getABGRColorData(textureImageList.get("Hole Damage 3")));

        textureImageList.put("Brick Stairs", Image.load("resources/Textures/Stairs/Brick Stairs.png"));
        textureDataList.put("Brick Stairs", Image.getABGRColorData(textureImageList.get("Brick Stairs")));
        textureImageList.put("Dirt Stairs", Image.load("resources/Textures/Stairs/Dirt Stairs.png"));
        textureDataList.put("Dirt Stairs", Image.getABGRColorData(textureImageList.get("Dirt Stairs")));

        textureImageList.put("Locked Wood Door", Image.load("resources/Textures/Door/Locked Wood Door.png"));
        textureDataList.put("Locked Wood Door", Image.getABGRColorData(textureImageList.get("Locked Wood Door")));
        textureImageList.put("Unlocked Wood Door", Image.load("resources/Textures/Door/Unlocked Wood Door.png"));
        textureDataList.put("Unlocked Wood Door", Image.getABGRColorData(textureImageList.get("Unlocked Wood Door")));
        textureImageList.put("Locked Fence Door", Image.load("resources/Textures/Door/Locked Fence Door.png"));
        textureDataList.put("Locked Fence Door", Image.getABGRColorData(textureImageList.get("Locked Fence Door")));
        textureImageList.put("Unlocked Fence Door", Image.load("resources/Textures/Door/Unlocked Fence Door.png"));
        textureDataList.put("Unlocked Fence Door", Image.getABGRColorData(textureImageList.get("Unlocked Fence Door")));
    }

    //Get the image file for the given texture.
    public static BufferedImage getTextureImage(String textureName) {
        return textureImageList.get(textureName);
    }
    //Get the pixel data for the given texture.
    public static byte[] getTextureData(String textureName) {
        return textureDataList.get(textureName);
    }

    //Constructor that takes two points.
    public Wall(Point2D.Double point1, Point2D.Double point2, boolean seeThrough, boolean characterCollidable, boolean projectileCollidable, String textureName, Color textureColor) {
        //Set the texture of the wall.
        this.texture = Wall.getTextureImage(textureName);
        this.texturePixelData = Wall.getTextureData(textureName);
        //Set the color of the texture.
        this.textureColor = textureColor;
        //Set the intractability of the wall.
        this.seeThrough = seeThrough;
        this.characterCollidable = characterCollidable;
        this.projectileCollidable = projectileCollidable;

        line = new Line2D.Double(point1, point2);
        //Distance formula.
        length = Math.sqrt(Math.pow(line.x1 - line.x2, 2) + Math.pow(line.y1 - line.y2, 2));
        //Modified equation for spherical coordinates.
        angle = Math.toDegrees(Math.atan2(line.x2-line.x1, line.y2-line.y1));
    }

    public BufferedImage getTexture() {
        return texture;
    }
    protected void setTexture(String textureName) {
        this.texture = Wall.getTextureImage(textureName);
        this.texturePixelData = Wall.getTextureData(textureName);
    }
    public byte[] getTexturePixelData() {
        return texturePixelData;
    }
    public Color getTextureColor() {
        return textureColor;
    }
    public Line2D.Double getLine() {
        return new Line2D.Double(line.getP1(), line.getP2());
    }
    public Point2D.Double getPoint1() {
        return new Point2D.Double(line.getX1(), line.getY1());
    }
    public Point2D.Double getPoint2() {
        return new Point2D.Double(line.getX2(), line.getY2());
    }
    public double getWidth() {
        return length;
    }
    public double getHeight() { return height; }
    public double getAngle() {
        return angle;
    }
    public boolean isSeeThrough() {
        return seeThrough;
    }
    public boolean isCharacterCollidable() {
        return characterCollidable;
    }
    public boolean isProjectileCollidable() {
        return projectileCollidable;
    }
}