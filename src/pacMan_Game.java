//Imports
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Random;

public class pacMan_Game extends JPanel {

    //J Panel
    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int borderWidth = columnCount * tileSize;
    private int borderHeight = rowCount * tileSize;


    //Images
    private Image wallimage;
    private Image blueGhost;
    private Image redGhost;
    private Image scaredGhost;
    private Image orangeGhost;
    private Image pinkGhost;

    public pacMan_Game() {
        setPreferredSize(new Dimension(borderWidth, borderHeight));
        setBackground(Color.black);

    }

}
