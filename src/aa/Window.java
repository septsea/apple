package aa;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * The Window class. It is introduced to decrease the repetition code.
 */
public class Window extends JFrame {
    /**
     * If {@code private static final long serialVersionUID} is not defined, a piece
     * of warning information will be shown. It is something tiny.
     */
    private static final long serialVersionUID = 1L;
    /**
     * The container to display text.
     */
    public JTextArea jta;

    /**
     * Initializes the window.
     * 
     * @param title         the title of the window.
     * @param width         the width of the window.
     * @param height        the height of the window.
     * @param invertedColor whether to invert the window background color. If this
     *                      is set {@code false}, the background color will be
     *                      white.
     */
    public Window(final String title, final int width, final int height, final boolean invertedColor) {
        this.setTitle(title);
        final Dimension size = new Dimension(width, height);
        this.setSize(size);
        this.setMaximumSize(size);
        this.setMinimumSize(size);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jta = new JTextArea();
        this.jta.setEditable(false);
        this.jta.setFocusable(false);
        this.jta.setFont(AALib.FiraCode(12));
        if (invertedColor) {
            this.jta.setBackground(Color.BLACK);
            this.jta.setForeground(Color.WHITE);
        } else {
            this.jta.setBackground(Color.WHITE);
            this.jta.setForeground(Color.BLACK);
        }
        this.getContentPane().add(this.jta);
    }

    /**
     * Displays the ASCII art player window.
     * 
     * @param op the core part of the player.
     */
    public void display(final Operation op) {
        this.setVisible(true);
        op.commence();
    }

}
