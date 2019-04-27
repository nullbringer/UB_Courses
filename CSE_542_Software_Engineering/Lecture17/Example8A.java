import java.awt.Event;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.*;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * This example shows how we can use some of the built-in dialog boxes.
 *
 * @author Matthew Hertz
 */
@SuppressWarnings({ "serial" })
public class Example8A extends JFrame implements ActionListener {
	/** Item used to access help. */
	private JMenuItem openItem;

	/** Item used to exit this program. */
	private JMenuItem exitItem;

	/** Our little message bar. */
	private JLabel label;

	/** Create an instance of the class: a window containing a menu. */
	public Example8A() {
		// Create the menu bar and set it on the window.
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// Create the File menu and add it to the menu bar.
		JMenu fileMenu = new JMenu();
		fileMenu.setText("File"); // What the item reads
		menuBar.add(fileMenu);

		// Create the open item and add it to the file menu.
		openItem = new JMenuItem();
		fileMenu.add(openItem);
		openItem.setText("Open");
		openItem.addActionListener(this);
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));

		// Create the exit item and add it to the File menu
		exitItem = new JMenuItem("Exit");
		fileMenu.add(exitItem);
		exitItem.addActionListener(this); // Use the Example7 class to handle these events.
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.ALT_MASK));

		// Create and display the label.
		label = new JLabel("You ain't seen nothin' yet.");
		add(label);
	}

	/**
	 * Run the example program that uses a menu.
	 *
	 * @param args
	 *            Command-line arguments which we will ignore.
	 */
	public static void main(String[] args) {
		Example8A window = new Example8A();

		// Finish setting up this window.
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("Dialog Example");
		window.setSize(700, 500);
		window.setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == exitItem) {
			// Kill this window -- it is no longer needed.
			dispose();
		} else if (arg0.getSource() == openItem) {
			// Create the JFileChooser to select a window to use.
			JFileChooser opener = new JFileChooser();
			opener.setAcceptAllFileFilterUsed(true);
			// What does this result mean? What happens if we cancel?
			int result = opener.showOpenDialog(null);
			// Change the label to show the file that was selected
			label.setText("You selected: " + opener.getSelectedFile() + " " + result);
		}
	}
}
