import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Our sixth example class. Let's look at colors and more...
 * 
 * @author Matthew Hertz
 */
@SuppressWarnings("unused")
public class Example6A implements ActionListener {
  /** The button to make things red. */
  private JButton redButton;

  /** The button to make things green. */
  private JButton greenButton;

  /** The button to make things green. */
  private JButton blueButton;

  /** The text for our screen. */
  private JLabel label;

  /**
   * Create a new window containing the button.
   */
  public Example6A() {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    redButton = new JButton("Red");
    redButton.addActionListener(this);

    greenButton = new JButton("Green");
    greenButton.addActionListener(this);

    blueButton = new JButton("Cyan");
    blueButton.addActionListener(this);

    label = new JLabel("I hold text");

    Dimension d = new Dimension(30, 30);
    redButton.setMinimumSize(d); // Give this button an actual size.
    frame.add(BorderLayout.SOUTH, redButton);
    frame.add(BorderLayout.EAST, greenButton);
    frame.add(BorderLayout.WEST, blueButton);
    frame.add(BorderLayout.CENTER, label);
    frame.setTitle("GUI sounds dirty"); // I think so, at least.

    frame.pack();
    frame.setVisible(true);
  }

  /**
   * If I do this correctly, I can joke that Example6 has 3 butt-ons (sorry, the line ran long &amp; I knew I had
   * another joke).
   * 
   * @param args Command-line arguments which we will ignore.
   */
  public static void main(String[] args) {
    Example6A example = new Example6A();
  }

  /** FOR TESTING PURPOSES ONLY */
  protected JButton getBlueButton() {
    return blueButton;
  }

  /** FOR TESTING PURPOSES ONLY */
  protected JButton getGreenButton() {
    return greenButton;
  }

  /** FOR TESTING PURPOSES ONLY */
  protected Color getLabelColor() {
    return label.getForeground();
  }

  /** FOR TESTING PURPOSES ONLY */
  protected JButton getRedButton() {
    return redButton;
  }

  /**
   * Change the label's color to reflect the clicked button.
   * How strong is this method's cohesion?
   * 
   * @param ae Action event that has just been triggered.
   */
  public void actionPerformed(ActionEvent ae) {
    // If this event was triggered by the button
    if (ae.getSource() == redButton) {
      label.setForeground(Color.RED);
    } else if (ae.getSource() == blueButton) {
      label.setForeground(Color.BLUE);
    } else {
      label.setForeground(Color.GREEN);
    }
  }
}