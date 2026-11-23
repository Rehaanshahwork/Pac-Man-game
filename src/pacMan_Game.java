//Imports
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Random;

public class pacMan_Game extends JPanel implements ActionListener, KeyListener {

    // =====================================================================
    // 1. GAME CONFIGURATION & CONSTANTS
    // =====================================================================
    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int borderWidth = columnCount * tileSize;
    private int borderHeight = rowCount * tileSize;

    // =====================================================================
    // 2. ASSETS (IMAGES)
    // =====================================================================
    private Image wall, blueGhost, redGhost, scaredGhost, orangeGhost, pinkGhost;
    private Image pacmanDown, pacmanUp, pacmanRight, pacmanLeft, powerFood;


    // =====================================================================
    // 3. GAME OBJECTS & DATA STRUCTURES
    // =====================================================================
    // HashSets are used because they prevent duplicates and allow very fast
    // collision lookups.
    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    // Game Loop & Utility
    Timer gameLoop;
    char[] directions = {'u', 'd', 'l', 'r'}; // Up, Down, Left, Right
    Random rand = new Random();


    // The Map (21 rows, 19 columns)
    private String[] tileMap = {
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

    // =====================================================================
    // 4. CONSTRUCTOR (Setup)
    // =====================================================================
    public pacMan_Game() {
        setPreferredSize(new Dimension(borderWidth, borderHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        loadImages();
        loadMap();

        // Give each ghost a random starting direction
        for (Block ghost : ghosts) {
            char randomDir = directions[rand.nextInt(4)];
            ghost.updateDirection(randomDir);
        }

        // Start the game loop (triggers actionPerformed every 50ms)
        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

    // =====================================================================
    // 5. THE INNER CLASS: BLOCK (Blueprint for Pacman, Ghosts, Walls)
    // =====================================================================
    public class Block {
        int x, y, width, height;
        Image image;

        // Movement properties
        char direction = 'u';
        int velocity_X = 0;
        int velocity_Y = 0;

        // Constructor
        public Block(int x, int y, int width, int height, Image image) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.image = image;
        }

        // Updates the direction and immediately updates the velocity
        void updateDirection(char direction) {
            this.direction = direction;
            updateVelocity();
        }

        // Translates the character direction ('u', 'd', etc.) into x/y speed
        void updateVelocity() {
            if (direction == 'u') {
                this.velocity_X = 0;
                this.velocity_Y = -tileSize / 4;
            } else if (direction == 'd') {
                this.velocity_X = 0;
                this.velocity_Y = tileSize / 4;
            } else if (direction == 'l') {
                this.velocity_X = -tileSize / 4;
                this.velocity_Y = 0;
            } else if (direction == 'r') {
                this.velocity_X = tileSize / 4;
                this.velocity_Y = 0;
            }
        }
    }

    // =====================================================================
    // 6. SETUP METHODS
    // =====================================================================
    private void loadImages() {
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

    // Reads the tileMap string and converts characters into game objects
    public void loadMap() {
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                char rowCharacter = tileMap[r].charAt(c);
                int x = c * tileSize;
                int y = r * tileSize;

                if (rowCharacter == 'X') {
                    walls.add(new Block(x, y, tileSize, tileSize, wall));
                } else if (rowCharacter == 'b') {
                    ghosts.add(new Block(x, y, tileSize, tileSize, blueGhost));
                } else if (rowCharacter == 'o') {
                    ghosts.add(new Block(x, y, tileSize, tileSize, orangeGhost));
                } else if (rowCharacter == 'r') {
                    ghosts.add(new Block(x, y, tileSize, tileSize, redGhost));
                } else if (rowCharacter == 'p') {
                    ghosts.add(new Block(x, y, tileSize, tileSize, pinkGhost));
                } else if (rowCharacter == 'P') {
                    pacman = new Block(x, y, tileSize, tileSize, pacmanRight);
                } else if (rowCharacter == ' ') {
                    // Food is smaller (4x4) and centered inside the 32x32 tile
                    foods.add(new Block(x + 14, y + 14, 4, 4, null));
                }
            }
        }
    }




    // =====================================================================
    // 7. GAMEPLAY & RENDERING
    // =====================================================================
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawImage(g);
    }

    public void drawImage(Graphics g) {
        // Draw Pac-Man
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        // Draw Ghosts
        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        // Draw Walls
        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        // Draw Food (White dots)
        g.setColor(Color.white); // Set color ONCE before the loop for performance
        for (Block food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }
    }



    void move() {
        // --- PAC-MAN MOVEMENT ---
        pacman.x += pacman.velocity_X;
        pacman.y += pacman.velocity_Y;

        // Pac-Man teleportation
        if (pacman.x > borderWidth) {
            pacman.x = 0;
        } else if (pacman.x + pacman.width < 0) {
            pacman.x = borderWidth;
        }

        // Pac-Man Collision Check
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                // If we hit a wall, undo the movement to slide along it
                pacman.x -= pacman.velocity_X;
                pacman.y -= pacman.velocity_Y;
                break;
            }
        }

        // --- GHOST MOVEMENT ---
        for (Block ghost : ghosts) {

            // 1. Force UP if on the tunnel row (Row 9)
            if (ghost.y == tileSize * 9 && (ghost.direction == 'l' || ghost.direction == 'r')) {
                ghost.updateDirection('u');
            }

            // 2. Move the ghost
            ghost.x += ghost.velocity_X;
            ghost.y += ghost.velocity_Y;

            // 3. Ghost teleportation
            if (ghost.x > borderWidth) {
                ghost.x = 0;
            } else if (ghost.x + ghost.width < 0) {
                ghost.x = borderWidth;
            }

            // 4. Ghost Collision Check
            for (Block wall : walls) {
                if (collision(ghost, wall)) {
                    // Undo movement so they don't phase into the block
                    ghost.x -= ghost.velocity_X;
                    ghost.y -= ghost.velocity_Y;

                    // Pick a new random direction
                    char newDirection = directions[rand.nextInt(4)];
                    ghost.updateDirection(newDirection);

                }
            }
        }
    }

    // AABB (Axis-Aligned Bounding Box) Collision Detection
    boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    // =====================================================================
    //INPUT HANDLING
    // =====================================================================
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('u');
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('d');
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('l');
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('r');
        }

        //When pack man changes direction, the image should change accordingly
        if(pacman.direction == 'u') {
            pacman.image = pacmanUp;
        }else if(pacman.direction == 'd') {
            pacman.image = pacmanDown;
        }else if(pacman.direction == 'l') {
            pacman.image = pacmanLeft;
        }else if(pacman.direction == 'r') {
            pacman.image = pacmanRight;
        }

    }
}