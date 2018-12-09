package Tanks3D.DisplayComponents;

import Tanks3D.GameData;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

//Draws both players' screens and the minimap to the display.
public class DisplayWindow extends JFrame {
    private final GameData gameData;
    private final DisplayPanel panel;

    public DisplayWindow(GameData gameData, String s, Dimension panelSize, int titleBarHeight) {
        super(s);

        this.gameData = gameData;

        //Set the settings for the JFrame.
        setLayout(new BorderLayout());
        setSize(new Dimension(panelSize.width, panelSize.height - titleBarHeight));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Instantiate the Panel object and add it to the JFrame.
        panel = new DisplayPanel(panelSize);
        panel.setPreferredSize(panelSize);
        add(panel);

        //Resize the JFrame and make it visible.
        pack();
        setVisible(true);
    }

    //Return the JPanel
    public JPanel getPanel() { return panel; }
    //Return the buffers for the playerController's screen and the minimap.
    public BufferedImage getScreenBuffer() { return panel.getScreenBuffer(); }
    public BufferedImage getMinimapBuffer() { return panel.getMinimapBuffer(); }

    //Draw the two screens and the minimap to buffers, then paint them on the display.
    public void draw() {
        gameData.playerController.draw();
        gameData.minimap.draw();

        //Paint all three buffers on the display. 'paintImmediately' is used to prevent the buffers being modified before painting is complete.
        panel.paintImmediately(panel.getBounds());
    }
}