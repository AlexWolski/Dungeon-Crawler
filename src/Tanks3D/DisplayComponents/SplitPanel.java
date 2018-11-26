package Tanks3D.DisplayComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

//Controls the painting of the buffers to the display.
public class SplitPanel extends JPanel {
    private final Dimension panelSize;
    //The three buffers used to store each player's screen and the minimap.
    private final BufferedImage screen1Buffer;
    private final BufferedImage screen2Buffer;
    private final BufferedImage minimapBuffer;
    //The distance between the two screens.
    private final static int screenGap = 4;

    public SplitPanel(Dimension size) {
        this.panelSize = size;
        //The size of each player's screen and the minimap is pre-determined.
        screen1Buffer = new BufferedImage(panelSize.width/2 - screenGap/2, panelSize.height, BufferedImage.TYPE_INT_RGB);
        screen2Buffer = new BufferedImage(panelSize.width/2 - screenGap/2, panelSize.height, BufferedImage.TYPE_INT_RGB);
        minimapBuffer = new BufferedImage(panelSize.height/3, panelSize.height/3, BufferedImage.TYPE_INT_RGB);
    }

    //Return the buffers for each player's screen and the minimap.
    public BufferedImage getScreen1Buffer() { return screen1Buffer; }
    public BufferedImage getScreen2Buffer() { return screen2Buffer; }
    public BufferedImage getMinimapBuffer() { return minimapBuffer; }

    //Paint the three buffers to the display.
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D graphic = (Graphics2D) g;

        //The location of each player's screen and the minimap is pre-determined.
        graphic.drawImage(screen1Buffer, 0, 0, null);
        graphic.drawImage(screen2Buffer, panelSize.width/2 + screenGap, 0, null);

        graphic.drawImage(minimapBuffer, panelSize.width/2 - minimapBuffer.getWidth()/2, 0, null);
    }
}
