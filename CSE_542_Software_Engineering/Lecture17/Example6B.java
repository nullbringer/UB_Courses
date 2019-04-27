import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Our sixth example class redux
 *
 * @author Matthew Hertz
 */
public class Example6B extends JFrame {

	/** The text for our screen. */
	private JLabel label;

	/**
	 * Create a new window containing the button.
	 */
	public Example6B() {
		super("GUI sounds dirty");
		JButton redButton = new JButton("Red");
		ButtonListen bl = new ButtonListen();
		redButton.addActionListener(bl);

		JButton greenButton = new JButton("Green");
		greenButton.addActionListener(bl);

		JButton blueButton = new JButton("Blue");
		blueButton.addActionListener(bl);

		label = new JLabel("I hold text");
		Dimension d = new Dimension(30, 30);
		redButton.setMinimumSize(d);
		add(BorderLayout.SOUTH, redButton);
		add(BorderLayout.EAST, greenButton);
		add(BorderLayout.WEST, blueButton);
		add(BorderLayout.CENTER, label);

		pack();
		setVisible(true);
	}

	/**
	 * Create the window with the buttons.
	 *
	 * @param args
	 *            Command-line arguments which we will ignore.
	 */
	public static void main(String[] args) {
		Example6C example = new Example6C();
		// Set the example's close operation, size, and then make it visible
		example.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		example.pack();
		example.setVisible(true);
	}

	public class ButtonListen implements ActionListener {
		
		/**
		 * This method is required by ActionListener. It will be called whenever an
		 * object to which we are listening generates an event.
		 *
		 * What is the cohesion and coupling this creates?
		 * 
		 * @param ae
		 *            Action event that has just been triggered.
		 */
		public void actionPerformed(ActionEvent ae) {
			JLabel theSource = (JLabel)ae.getSource();
			// If this event was triggered by the button
		    if (theSource.getText() == "RED") {
		      label.setForeground(Color.RED);
		    } else if (theSource.getText() == "BLUE") {
		      label.setForeground(Color.BLUE);
		    } else {
		      label.setForeground(Color.GREEN);
		    }
		}
	}
}
