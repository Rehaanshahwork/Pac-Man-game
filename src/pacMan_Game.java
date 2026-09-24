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
    private Image wall;
    private Image blueGhost;
    private Image redGhost;
    private Image scaredGhost;
    private Image orangeGhost;
    private Image pinkGhost;
    private Image greenGhost;
    private Image cherry;
    private Image cherry2;
    private Image pacmanDown;
    private Image pacmanUp;
    private Image pacmanRight;
    private Image pacmanLeft;
    private Image powerFood;


    //We will use Hashsets to represent multiple objects, for better performance
    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    //Tile map for the game
    private String [] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };


    public pacMan_Game() {
        setPreferredSize(new Dimension(borderWidth, borderHeight));
        setBackground(Color.black);

        //Loading the images of the game
        wall = new ImageIcon(getClass().getResource("wall.png")).getImage();
        blueGhost = new ImageIcon(getClass().getResource("blueGhost.png")).getImage();
        redGhost = new ImageIcon(getClass().getResource("redGhost.png")).getImage();
        scaredGhost = new ImageIcon(getClass().getResource("scaredGhost.png")).getImage();
        orangeGhost = new ImageIcon(getClass().getResource("orangeGhost.png")).getImage();
        pinkGhost = new ImageIcon(getClass().getResource("pinkGhost.png")).getImage();
        pacmanDown = new ImageIcon(getClass().getResource("pacmanDown.png")).getImage();
        pacmanUp = new ImageIcon(getClass().getResource("pacmanUp.png")).getImage();
        pacmanRight = new ImageIcon(getClass().getResource("pacmanRight.png")).getImage();
        pacmanLeft = new ImageIcon(getClass().getResource("pacmanLeft.png")).getImage();
        powerFood = new ImageIcon(getClass().getResource("powerFood.png")).getImage();

    }

    public class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;

        //X and Y will keep chaning so we need to save the starting x and y
        int startX;
        int startY;

        //class constructor
        public Block(int x, int y, int width, int height, Image image){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.image = image;
            startX = x;
            startY = y;
        }
    }

    //Loading the tile map
    public void loadMap(){
        for(int r = 0; r < rowCount; r++){
            for(int c = 0; c < columnCount; c++){
                String row = tileMap[r];
                char rowCharacter = row.charAt(c);

                //Finding X and Y
                int x = r*tileSize;
                int y = c*tileSize;

              // Logic for adding objects to panel
                if(rowCharacter == 'X'){

                }
            }
        }
    }


}
