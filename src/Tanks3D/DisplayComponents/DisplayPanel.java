package Tanks3D.DisplayComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

//Controls the painting of the buffers to the display.
public class DisplayPanel extends JPanel {
    private final Dimension panelSize;
    //The two buffers used to store the player's screen and the minimap.
    private final BufferedImage screenBuffer;
    private final BufferedImage minimapBuffer;

    public DisplayPanel(Dimension size) {
        this.panelSize = size;
        //The size of each player's screen and the minimap is pre-determined.
        screenBuffer = new BufferedImage(panelSize.width, panelSize.height, BufferedImage.TYPE_INT_RGB);
        minimapBuffer = new BufferedImage(panelSize.height/3, panelSize.height/3, BufferedImage.TYPE_INT_RGB);
    }

    //Return the buffers for each player's screen and the minimap.
    public BufferedImage getScreenBuffer() { return screenBuffer; }
    public BufferedImage getMinimapBuffer() { return minimapBuffer; }

    //Paint the three buffers to the display.
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D graphic = (Graphics2D) g;

        //Draw the player's screen and the minimap.
        graphic.drawImage(screenBuffer, 0, 0, null);
        graphic.drawImage(minimapBuffer, panelSize.width - minimapBuffer.getWidth(), 0, null);
    }
}
