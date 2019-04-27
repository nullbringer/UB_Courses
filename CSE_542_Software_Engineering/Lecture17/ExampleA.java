import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.*;

/**
 * Another example of how the MVC scheme is useful. This is based on (and uses)
 * code from Sun's Swing tutorial. The original can be found here: <a href=
 * "http://java.sun.com/docs/books/tutorial/uiswing/events/examples/ListSelectionDemo.java">http://java.sun.com/docs/books/tutorial/uiswing/events/examples/ListSelectionDemo.java</a>
 */
@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
public class ExampleA extends JFrame implements ListSelectionListener {
	/** List that will hold English translations. */
	private JList list;

	/** Table that holds translations in many other languages. */
	private JTable table;

	/** Create a new window that translates simple text. */
	public ExampleA() {
		String[] listData = { "one", "two", "three", "four", "five", "six", "seven" };
		String[] columnNames = { "French", "Spanish", "Italian" };
		String[][] tableData = { { "un", "uno", "uno" }, { "deux", "dos", "due" }, { "trois", "tres", "tre" },
				{ "quatre", "cuatro", "quattro" }, { "cinq", "cinco", "cinque" }, { "six", "seis", "sei" },
				{ "sept", "siete", "sette" } };
		list = new JList(listData);
		ListSelectionModel listSelectionModel = list.getSelectionModel();
		listSelectionModel.addListSelectionListener(this);
		table = new JTable(tableData, columnNames);
		table.setSelectionModel(listSelectionModel);
		table.setFont(list.getFont());
		table.setRowHeight(table.getFontMetrics(table.getFont()).getHeight() +2);
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		getContentPane().add(BorderLayout.WEST, list);
		getContentPane().add(BorderLayout.CENTER, table);
	}

	/** If the selection has changed on either the list or table, process it. */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			if (!list.isSelectionEmpty()) {
				System.err.print(list.getSelectedValue());
				if (table.getSelectedColumn() == -1) {
					for (int i = 0; i < 3; i++)
						System.err.print(" " + table.getValueAt(table.getSelectedRow(), i));
				} else {
					System.err.print(" " + table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()));
				}
			}
			System.err.println();
		}
	}

	/**
	 * Runs another example program using the full MVC scheme.
	 *
	 * @param args
	 *            Command-line arguments which we will ignore.
	 */
	public static void main(String[] args) {

		ExampleA window = new ExampleA();
		// Finish setting up this window.
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("Full Example");
		window.setSize(800,500);
		window.setVisible(true);
	}
}
