//Imports
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Random;


public class pacMan_Game extends JPanel implements ActionListener,KeyListener {

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
    Timer gameLoop;

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

        //Testing loadMap and how many of each hash set I have
        loadMap();

        //Game loop
        gameLoop = new Timer(50, this);
        gameLoop.start();

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
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();
        for(int r = 0; r < rowCount; r++){
            for(int c = 0; c < columnCount; c++){

                char rowCharacter = tileMap[r].charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;
                if(rowCharacter == 'X'){ // Wall
                    Block wallBlock = new Block(x, y, tileSize, tileSize, wall);
                    walls.add(wallBlock);

                } else if(rowCharacter == 'b'){ // Blue ghost
                    Block ghostBlock = new Block(x, y, tileSize, tileSize, blueGhost);
                    ghosts.add(ghostBlock);

                } else if(rowCharacter == 'o'){ // Orange ghost
                    Block ghostBlock = new Block(x, y, tileSize, tileSize, orangeGhost);
                    ghosts.add(ghostBlock);

                } else if(rowCharacter == 'r'){ // Red ghost
                    Block ghostBlock = new Block(x, y, tileSize, tileSize, redGhost);
                    ghosts.add(ghostBlock);

                } else if(rowCharacter == 'p'){ // Pink ghost
                    Block ghostBlock = new Block(x, y, tileSize, tileSize, pinkGhost);
                    ghosts.add(ghostBlock);

                } else if(rowCharacter == 'P'){ // Pac-Man
                    pacman = new Block(x, y, tileSize, tileSize, pacmanRight);

                } else if(rowCharacter == ' '){ // Food pellet
                    Block foodBlock = new Block(x+14, y+14, 4, 4, null);
                    foods.add(foodBlock);
                }
            }
        }
    }

    //Methods for drawing the components
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawImage(g);
    }

    //Drawing
    public void drawImage(Graphics g){
        g.drawImage(pacman.image, pacman.x,  pacman.y, pacman.width, pacman.height, null);

        //Drawing the ghosts
        for(Block ghost : ghosts){
            g.drawImage(ghost.image, ghost.x,  ghost.y, ghost.width, ghost.height, null);
        }

        //Drawing the walls
        for(Block wall : walls){
            g.drawImage(wall.image, wall.x,  wall.y, wall.width, wall.height, null);
        }

        for(Block food : foods){
            g.fillRect(food.x, food.y, food.width, food.height);
            g.setColor(Color.white);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        //For this code to execute we need a game loop / timer
    }

    //Movement methods
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }



}
