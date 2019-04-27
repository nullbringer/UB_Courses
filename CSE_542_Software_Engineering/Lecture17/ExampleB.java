import java.awt.Font;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * We now see how to make an area in which we can edit text.
 *
 * @author Matthew Hertz
 */
@SuppressWarnings({ "serial" })
public class ExampleB extends JFrame {
	/**
	 * Create a new window in which to write the great American novel.
	 */
	public ExampleB() {
		JTextArea textBox = new JTextArea(1, 50);
		textBox.setLineWrap(true);
		textBox.setWrapStyleWord(true);
		textBox.setText("It was a dark and stormy night...");
		add(textBox);
	}

	/**
	 * Run the example program that starts the day
	 *
	 * @param args
	 *            Command-line arguments which we will ignore.
	 */
	public static void main(String[] args) {

		ExampleB window = new ExampleB();

		// Finish setting up this window.
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("Full Example");
		window.pack();
		window.setVisible(true);
	}

}
