import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

/**
 * This example shows how we can create and add menu items to our programs and also how to work with multiple windows.
 * 
 * @author Matthew Hertz
 */
public class Example7 extends JFrame implements ActionListener {
  /** Item used to exit this program. */
  private JMenuItem exitItem;

  /** Number of instances of this class that are active. */
  private static int numActive = 0;

  /** Create an instance of the class: a window containing a menu. */
  public Example7() {
    // Create the menu bar and set it on the window.
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);
 
    // Create the File menu and add it to the menu bar.
    JMenu fileMenu = new JMenu();
    fileMenu.setText("File"); // What the item reads
    fileMenu.setMnemonic(KeyEvent.VK_F); // Shortcut for this menu
    menuBar.add(fileMenu);

    // Create the exit item and add it to the File menu
    exitItem = new JMenuItem("Exit");
    fileMenu.add(exitItem);
    exitItem.setMnemonic(KeyEvent.VK_X); // Use the "X" key to select.
    exitItem.addActionListener(this); // Use the Example7 class to handle these events.
    exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.ALT_MASK));

    // Create the help item and add it to the menu bar.
    JMenuItem helpItem = new JMenuItem();
    helpItem.setText("Help");
    helpItem.addActionListener(this);
    menuBar.add(helpItem);

    // Add to the number of active JFrames
    numActive += 1;
  }

  /**
   * Run the example program that uses a menu.
   * 
   * @param args Command-line arguments which we will ignore.
   */
  public static void main(String[] args) {
    Example7 firstWindow = new Example7();
    Example7 secondWindow = new Example7();
    // Finish setting up the first window.
    firstWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    firstWindow.setTitle("First Window With Menu Example");
    firstWindow.setSize(300, 300);
    firstWindow.setVisible(true);
    // Now set up the second window, but DO NOT set it to exit on close.
    secondWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    secondWindow.setTitle("Second Window With Menu Example");
    secondWindow.setSize(300, 300);
    secondWindow.setVisible(true);
  }

  /*
   * (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent arg0) {
    if (arg0.getSource() == exitItem) {
      // Double-check what is going on.
      System.out.println("Hi Mom! " + numActive);
      // Other windows are still active, so decrement the active count
      numActive -= 1;
      // And tell the GUI to remove the current JFrame
      dispose();

    } else {
      JOptionPane.showMessageDialog(null, "Not implemented yet!");
    }
  }
}
