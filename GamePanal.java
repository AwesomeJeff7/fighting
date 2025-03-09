import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener {
    private Fighter player1;
    private Fighter player2;
    private Timer timer;
    private boolean gameOver = false;
    
    // Key states
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean aPressed = false;
    private boolean dPressed = false;
    
    public GamePanel() {
        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.WHITE);
        
        // Create fighters
        player1 = new Fighter("Player 1", 100, 250, true);
        player2 = new Fighter("Player 2", 600, 250, false);
        
        // Set up game timer
        timer = new Timer(16, this); // ~60 FPS
        timer.start();
        
        // Set up key listeners
        setFocusable(true);
        setupKeyBindings();
    }
    
    private void setupKeyBindings() {
        // Player 1 controls (A, D, F, G)
        bindKey("A", "pressed", evt -> aPressed = true);
        bindKey("A", "released", evt -> aPressed = false);
        bindKey("D", "pressed", evt -> dPressed = true);
        bindKey("D", "released", evt -> dPressed = false);
        bindKey("F", "pressed", evt -> player1.attack(player2));
        bindKey("G", "pressed", evt -> player1.block());
        bindKey("G", "released", evt -> player1.stopBlocking());
        
        // Player 2 controls (Left, Right, K, L)
        bindKey("LEFT", "pressed", evt -> leftPressed = true);
        bindKey("LEFT", "released", evt -> leftPressed = false);
        bindKey("RIGHT", "pressed", evt -> rightPressed = true);
        bindKey("RIGHT", "released", evt -> rightPressed = false);
        bindKey("K", "pressed", evt -> player2.attack(player1));
        bindKey("L", "pressed", evt -> player2.block());
        bindKey("L", "released", evt -> player2.stopBlocking());
    }
    
    private void bindKey(String key, String event, ActionListener action) {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
        
        im.put(KeyStroke.getKeyStroke(key), key + event);
        am.put(key + event, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.actionPerformed(e);
            }
        });
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            // Handle movement
            if (aPressed) player1.move(-5);
            if (dPressed) player1.move(5);
            if (leftPressed) player2.move(-5);
            if (rightPressed) player2.move(5);
            
            // Check for game over
            if (player1.getHealth() <= 0 || player2.getHealth() <= 0) {
                gameOver = true;
            }
            
            repaint();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw fighters
        drawFighter(g, player1);
        drawFighter(g, player2);
        
        // Draw health bars
        drawHealthBar(g, player1, 50, 30);
        drawHealthBar(g, player2, getWidth() - 250, 30);
        
        // Draw game over message
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            String winner = (player1.getHealth() <= 0) ? player2.getName() : player1.getName();
            g.drawString(winner + " Wins!", getWidth()/2 - 100, getHeight()/2);
        }
    }
    
    private void drawFighter(Graphics g, Fighter fighter) {
        g.setColor(fighter.isBlocking() ? Color.BLUE : Color.BLACK);
        g.fillRect(fighter.getX(), fighter.getY(), fighter.getWidth(), fighter.getHeight());
    }
    
    private void drawHealthBar(Graphics g, Fighter fighter, int x, int y) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 200, 20);
        g.setColor(Color.GREEN);
        g.fillRect(x, y, fighter.getHealth() * 2, 20);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, 200, 20);
        g.drawString(fighter.getName() + ": " + fighter.getHealth(), x, y - 5);
    }
}
