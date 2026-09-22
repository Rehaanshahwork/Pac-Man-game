import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        //Define variables of the maze
        int rowCount = 21;
        int columnCount = 19;
        int tileSize = 32;
        int borderWidth = columnCount * tileSize;
        int borderHeight = rowCount * tileSize;

        //Create the frame for the maze
        JFrame frame = new JFrame("Pac Man");
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(borderWidth, borderHeight);

        //Create game
        pacMan_Game game = new pacMan_Game();
        frame.add(game);
        frame.pack();
        frame.setVisible(true);


    }
}
