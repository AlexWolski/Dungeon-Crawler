package Tanks3D.DisplayComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

//Controls the painting of the buffers to the display.
public class DisplayPanel extends JPanel {
    private final Dimension panelSize;
    //The buffer used to store the screen and the minimap.
    private final BufferedImage screenBuffer;
    //The graphics2D object used to draw to the screen.
    private Graphics2D graphic;

    public DisplayPanel(Dimension size) {
        this.panelSize = size;

        //Initialize the buffer for the screen.
        screenBuffer = new BufferedImage(panelSize.width, panelSize.height, BufferedImage.TYPE_INT_RGB);
    }

    //Return the buffers for each playerController's screen and the minimap.
    public BufferedImage getScreenBuffer() { return screenBuffer; }

    @Override
    public void paintComponent(Graphics g) {
        graphic = (Graphics2D) g;

        graphic.drawImage(screenBuffer, 0, 0, panelSize.width, panelSize.height, null);
    }
}