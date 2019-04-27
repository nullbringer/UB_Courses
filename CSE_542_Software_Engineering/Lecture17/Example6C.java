import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Our sixth example class re-redux
 *
 * @author Matthew Hertz
 */
@SuppressWarnings("unused")
public class Example6C extends JFrame {

	public class ButtonListen implements ActionListener {
		private Color theColor;

		public ButtonListen(Color c) {
			theColor = c;
		}

		/**
		 * This method is required by ActionListener. It will be called whenever an
		 * object to which we are listening generates an event.
		 *
		 * Is the cohesion and coupling better or worse than
		 * past efforts?
		 * 
		 * @param arg0
		 *            Action event that has just been triggered.
		 */
		public void actionPerformed(ActionEvent arg0) {
			label.setForeground(theColor);
		}
	}

	/** The text for our screen. */
	private JLabel label;

	/**
	 * Create a new window containing the button.
	 */
	public Example6C() {
		super("GUI sounds dirty");
		JButton redButton = new JButton("Red");
		redButton.addActionListener(new ButtonListen(Color.RED));

		JButton greenButton = new JButton("Green");
		greenButton.addActionListener(new ButtonListen(Color.GREEN));

		JButton blueButton = new JButton("Cyan");
		blueButton.addActionListener(new ButtonListen(Color.CYAN));

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
}
