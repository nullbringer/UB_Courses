import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This is the third class which will be presented in class. This time, we add
 * an ActionListener so that the program actually does something. It is still
 * boring, however.
 * 
 * @author Matthew Hertz
 */
@SuppressWarnings("unused")
public class Example3 implements ActionListener {
	/**
	 * Variable counting the number of times an object to which we are listening
	 * is clicked.
	 */
	private int clicks;

	/**
	 * Window that gives good GUI. Yeah, I said it.
	 * 
	 * Since we only use this field in one method we could make it a local
	 * variable, but would the code then work?
	 */
	protected JFrame frame;

	/**
	 * Create a new window containing the button.
	 */
	public Example3() {
		frame = new JFrame(); // Create a window in which to display things

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Quit when frame closed

		JButton button = new JButton(); // Create a JButton

		button.setText("Push the button!"); // Hey, it worked for Stanley Milgram...

		button.addActionListener(this); // Shhh. We're spying.
		button.addActionListener(this); // Shhh. We're spying.
		
		frame.add(BorderLayout.NORTH, button); // Where does this go?
                
		frame.setSize(300, 300); // Set the size of the window.
		
		frame.setVisible(true); // Make the window visible.
	}

	/**
	 * Create the window with the button. Once the window is created we no
	 * longer need to run anything here...
	 * 
	 * @param args Command-line arguments which we will ignore.
	 */
	public static void main(String[] args) {
		Example3 example = new Example3();
	}

	/**
	 * This method is declared by ActionListener. It is called whenever we click
	 * on the JButton in the JFrame.
	 * 
	 * @param arg0
	 *            Object recording the action that caused this method to be
	 *            called.
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (clicks == 0) {
			JOptionPane.showMessageDialog(null, "This is (kind of) your first event!");
			System.out.println("Why did you have to click me?");
		}
		// Increase the number of times we've been clicked.
		clicks += 1;
		// Get the button that was pushed
		JButton theButton = (JButton) arg0.getSource();
		// Update the text to reflect this fact.
		theButton.setText("I've been clicked " + clicks + " times");
	}
}