import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 * This object holds and can be used to manipulate all of the data inside a list.  This is a 
 * far better example than what I did before.
 *  
 * @author Matthew Hertz
 */
@SuppressWarnings({"serial","rawtypes"})
public class Example9BListModel extends AbstractListModel {
  private ArrayList<String> data;
  
  /** Create a new object that holds data for a list.  This will initially be empty. */
  public Example9BListModel() {
    data = new ArrayList<String>();
  }
  
  /**
   * Add another piece of data to the end of this list.
   * 
   * @param item Piece of data to be added to the list.
   */
  public void addData(String item) {
    data.add(item);
    fireIntervalAdded(item, getSize() - 1, getSize());
  }

  /* (non-Javadoc)
   * @see javax.swing.ListModel#getElementAt(int)
   */
  public String getElementAt(int arg0) {
    return data.get(arg0);
  }
  
  public void removeData(int i) {
    String victim = data.remove(i);
    fireIntervalRemoved(victim, i, i+1);
  }

  /* (non-Javadoc)
   * @see javax.swing.ListModel#getSize()
   */
  public int getSize() {
    return data.size();
  }
}



