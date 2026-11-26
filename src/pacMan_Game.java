import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Random;

public class pacMan_Game extends JPanel implements ActionListener, KeyListener {

    // =====================================================================
    // 1. GAME CONFIGURATION & CONSTANTS
    // =====================================================================
    private final int rowCount = 21;
    private final int columnCount = 19;
    private final int tileSize = 32;
    private final int borderWidth = columnCount * tileSize;
    private final int borderHeight = rowCount * tileSize;

    // =====================================================================
    // 2. ASSETS (IMAGES)
    // =====================================================================
    private Image wall, blueGhost, redGhost, scaredGhost, orangeGhost, pinkGhost;
    private Image pacmanDown, pacmanUp, pacmanRight, pacmanLeft, powerFood;

    // =====================================================================
    // 3. GAME OBJECTS & DATA
    // =====================================================================
    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    Timer gameLoop;
    char[] directions = {'u', 'd', 'l', 'r'};
    Random rand = new Random();

    // Game State
    int playerLives = 3;
    int gameScore = 0;
    boolean gameOver = false;

    private final String[] tileMap = {
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
    // 4. CONSTRUCTOR
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

        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

    // =====================================================================
    // 5. INNER CLASS: BLOCK
    // =====================================================================
    public class Block {
        int x, y, width, height;
        Image image;
        char direction = 'u';
        int velocity_X = 0, velocity_Y = 0;
        int startX, startY;

        public Block(int x, int y, int width, int height, Image image) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.image = image;
            this.startX = x;
            this.startY = y;
        }

        void updateDirection(char direction) {
            this.direction = direction;
            updateVelocity();
        }

        void updateVelocity() {
            if (direction == 'u') { velocity_X = 0; velocity_Y = -tileSize / 4; }
            else if (direction == 'd') { velocity_X = 0; velocity_Y = tileSize / 4; }
            else if (direction == 'l') { velocity_X = -tileSize / 4; velocity_Y = 0; }
            else if (direction == 'r') { velocity_X = tileSize / 4; velocity_Y = 0; }
        }

        void reset() {
            this.x = this.startX;
            this.y = this.startY;
            this.velocity_X = 0;
            this.velocity_Y = 0;
        }

        // Forces the block to the exact center of its grid tile
        void snapToGrid() {
            this.x = Math.round(this.x / (float) tileSize) * tileSize;
            this.y = Math.round(this.y / (float) tileSize) * tileSize;
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

    public void loadMap() {
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                char rowCharacter = tileMap[r].charAt(c);
                int x = c * tileSize;
                int y = r * tileSize;

                if (rowCharacter == 'X') walls.add(new Block(x, y, tileSize, tileSize, wall));
                else if (rowCharacter == 'b') ghosts.add(new Block(x, y, tileSize, tileSize, blueGhost));
                else if (rowCharacter == 'o') ghosts.add(new Block(x, y, tileSize, tileSize, orangeGhost));
                else if (rowCharacter == 'r') ghosts.add(new Block(x, y, tileSize, tileSize, redGhost));
                else if (rowCharacter == 'p') ghosts.add(new Block(x, y, tileSize, tileSize, pinkGhost));
                else if (rowCharacter == 'P') pacman = new Block(x, y, tileSize, tileSize, pacmanRight);
                else if (rowCharacter == ' ') foods.add(new Block(x + 14, y + 14, 4, 4, null));
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
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.white);
        for (Block food : foods) {
            g.fillRect(food.x, food.y, food.width, food.height);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 28));
        if (gameOver) {
            g.drawString("Game Over: " + gameScore, tileSize / 2, tileSize / 2);
        } else {
            g.drawString("Score: " + gameScore, tileSize / 2, tileSize / 2);
        }
    }

    void move() {
        if (gameOver) return; // Stop moving if the game is over

        // --- PAC-MAN MOVEMENT ---
        pacman.x += pacman.velocity_X;
        pacman.y += pacman.velocity_Y;

        // Pac-Man teleportation
        if (pacman.x > borderWidth) pacman.x = 0;
        else if (pacman.x + pacman.width < 0) pacman.x = borderWidth;

        // Pac-Man Collision Check
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocity_X;
                pacman.y -= pacman.velocity_Y;
                break;
            }
        }

        // --- GHOST MOVEMENT ---
        for (Block ghost : ghosts) {
            // Check for death collision
            if (collision(pacman, ghost)) {
                playerLives -= 1;
                if (playerLives <= 0) {
                    gameOver = true;
                    gameLoop.stop();
                } else {
                    resetPositions();
                }
                return; // Stop moving this frame so we don't glitch out
            }

            // Force UP if in the tunnel
            if (ghost.y == tileSize * 9 && (ghost.direction == 'l' || ghost.direction == 'r')) {
                ghost.updateDirection('u');
            }

            // Move Ghost
            ghost.x += ghost.velocity_X;
            ghost.y += ghost.velocity_Y;

            // Ghost Teleportation
            if (ghost.x > borderWidth) ghost.x = 0;
            else if (ghost.x + ghost.width < 0) ghost.x = borderWidth;

            // Ghost Collision Check
            for (Block wall : walls) {
                if (collision(ghost, wall)) {
                    ghost.x -= ghost.velocity_X;
                    ghost.y -= ghost.velocity_Y;

                    // Snap to center of tile so they don't get stuck
                    ghost.snapToGrid();

                    char newDirection = directions[rand.nextInt(4)];
                    ghost.updateDirection(newDirection);
                    break; // Break so they don't double-collide in one frame
                }
            }
        }

        // --- FOOD COLLISION---
        Block foodEaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                foodEaten = food;
                gameScore += 10;
            }
        }
        if (foodEaten != null) {
            foods.remove(foodEaten);
        }
        //If all the food is eaten, we progress to the next level
        if (foods.isEmpty()){
            loadMap();
            resetPositions();
        }
    }

    boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    public void resetPositions() {
        pacman.reset();
        // Pac-Man doesn't move until the player presses a key again
        pacman.direction = 'u';

        for (Block ghost : ghosts) {
            ghost.reset();
            char newDirection = directions[rand.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    // =====================================================================
    // 8. INPUT HANDLING
    // =====================================================================
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        //Reset the game when game is over
         if (gameOver) {
             loadMap();
             resetPositions();
             playerLives = 3;
             gameScore = 0;
             gameOver = false;
             gameLoop.start();
         }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('u');
            pacman.image = pacmanUp;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('d');
            pacman.image = pacmanDown;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('l');
            pacman.image = pacmanLeft;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('r');
            pacman.image = pacmanRight;
        }
    }
}