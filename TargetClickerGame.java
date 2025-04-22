import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class TargetClickerGame extends JFrame {
    public TargetClickerGame() {
        setTitle("Target Clicker");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new GamePanel());
        setVisible(true);
    }

    public static void main(String[] args) {
        new TargetClickerGame();
    }
}

class GamePanel extends JPanel implements ActionListener, MouseListener {
    int targetX = 30; 
    int targetY = 30;
    int targetRadius = 30;
    int score = 0;
    int timeLeft = 30;
    Timer moveTimer;
    Timer gameTimer;
    Random rand = new Random();

    public GamePanel() {
        setBackground(Color.WHITE);
        addMouseListener(this);

        moveTimer = new Timer(1000, this);
        gameTimer = new Timer(1000, e -> {
            timeLeft--;
            if (timeLeft <= 0) {
                moveTimer.stop();
                gameTimer.stop();
                repaint();
            }
        });

        moveTimer.start();
        gameTimer.start();


        SwingUtilities.invokeLater(() -> {
            Timer delayTimer = new Timer(100, e -> {
                moveTarget();
                ((Timer) e.getSource()).stop();
            });
            delayTimer.setRepeats(false);
            delayTimer.start();
        });
    }

    private void moveTarget() {
        int w = getWidth();
        int h = getHeight();

        if (w <= 2 * targetRadius || h <= 2 * targetRadius) return;

        targetX = rand.nextInt(w - 2 * targetRadius) + targetRadius;
        targetY = rand.nextInt(h - 2 * targetRadius) + targetRadius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (timeLeft > 0) {
            g.setColor(Color.RED);
            g.fillOval(targetX - targetRadius, targetY - targetRadius, targetRadius * 2, targetRadius * 2);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString("Score: " + score, 20, 30);
            g.drawString("Time: " + timeLeft, 20, 60);
        } else {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Over!", getWidth() / 2 - 100, getHeight() / 2 - 20);
            g.drawString("Final Score: " + score, getWidth() / 2 - 110, getHeight() / 2 + 30);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveTarget();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (timeLeft > 0) {
            int dx = e.getX() - targetX;
            int dy = e.getY() - targetY;
            if (dx * dx + dy * dy <= targetRadius * targetRadius) {
                score++;
                moveTarget();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}