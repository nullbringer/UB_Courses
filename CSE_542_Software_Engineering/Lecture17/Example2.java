import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * This, our second example, is really our first GUI class.
 * 
 * @author Matthew Hertz
 */
public class Example2 {
  /** 
   * Start working with the GUI system.
   * 
   * @param args Command-line arguments which we will ignore.
   */
  public static void main(String[] args) {
    JFrame frame = new JFrame();    // Create a window in which to display things
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Program now quits when the frame is closed
    JButton button = new JButton(); // Create a button.  I like buttons.
    button.setText("Click me!");    // I think the button should say something.
    frame.add(button); // Place the button on our window.
    frame.setSize(300,300);         // Set the size of the window.
    frame.setVisible(true);         // Make the window visible.
    // Does this program now end?  What is happening? 
  }
}