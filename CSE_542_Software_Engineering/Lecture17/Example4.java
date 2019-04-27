import java.awt.BorderLayout;

import javax.swing.JButton;

/**
 * This example shows: a) this is still Java and normal Java rules apply; and b) how this whole ActionListener thing
 * works.
 * 
 * @author Matthew Hertz
 */
@SuppressWarnings("unused")
public class Example4 extends Example3 {
  /**
   * Create an instance of this class. This does all the work of Example3, but adds another button.
   */
  public Example4() {
    super();
    JButton anotherButton = new JButton("Another button?"); // Create another button with specified text.
    frame.add(BorderLayout.CENTER, anotherButton); // Add the button to the window.
    // What happens when we press anotherButton?
  }

  /**
   * Create the window with 2 buttons; but do they work?
   * 
   * @param args Command-line arguments which we will ignore.
   */
  public static void main(String[] args) {
    Example4 example = new Example4();
  }
}
