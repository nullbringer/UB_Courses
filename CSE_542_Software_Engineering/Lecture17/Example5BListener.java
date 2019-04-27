import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * This class does not create a JFrame (or anything else) but simply listens for
 * action events.
 * 
 * @author Matthew Hertz
 */
public class Example5BListener implements ActionListener {
	/** Counts the number of times the action has been performed. */
	private int clicks;
	
	/** What if we assign this in the constructor? */
	private JButton theButton;

	/** Create a new instance of this class that has not yet recorded a click. */
	public Example5BListener(JButton b) {
		theButton = b;
		clicks = 0;
	}

	/**
	 * When an event occurs, update the buttons text to reflect this fact.
	 */
	public void actionPerformed(ActionEvent arg0) {
		// Increase the number of times this button has been clicked.
		clicks += 1;
		// Get the button that was clicked.
		//JButton btn = (JButton) arg0.getSource();
		// Update the text to reflect this fact.
		theButton.setText("I've been clicked " + clicks + " times");
	}
}
