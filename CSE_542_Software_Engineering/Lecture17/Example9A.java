import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * With this example, we look at the full MVC (model-view-controller) Java GUI
 * system. We have now seen at least a little bit of each of Swing's different
 * areas.
 *
 * @author Matthew Hertz
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class Example9A extends JFrame implements ListSelectionListener, ActionListener {
	/** List of names that I will use. */
	private JList myList;

	/** Create a new window containing our list. */
	public Example9A() {
		// Create the description for our list
		JLabel description = new JLabel("Who should be called on next: ");
		add(BorderLayout.NORTH, description);
		myList = new JList();
		ListModel model = new Example9AListModel();
		myList.setModel(model);
		myList.setVisibleRowCount(1);
		myList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		myList.addListSelectionListener(this);
		add(BorderLayout.CENTER, myList);

		JList myList2 = new JList();
		ListModel model2 = new Example9AListModel();
		myList2.setModel(model2);
		myList2.setVisibleRowCount(1);
		myList2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// Will this duplicate myList?
		add(BorderLayout.EAST, myList2);

		// Finally, create the button component.
		JButton button = new JButton("Add a student");
		button.addActionListener(this);
		add(BorderLayout.SOUTH, button);
	}

	/**
	 * Run the example program that uses the full MVC scheme.
	 *
	 * @param args
	 *            Command-line arguments which we will ignore.
	 */
	public static void main(String[] args) {
		Example9A window = new Example9A();

		// Finish setting up this window.
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("Full Example");
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
		// You selected the button, lets ask a question and go from there!
		String value = JOptionPane.showInputDialog(null, "Student to be added:");
		if (value != null) {
			((Example9AListModel) myList.getModel()).addData(value);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.
	 * ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent arg0) {
		// Get the selected value
		String selection = ((String) myList.getSelectedValue());
		// Ask them what they think
		JOptionPane.showMessageDialog(null, selection + ", what do you think?");
	}
}
