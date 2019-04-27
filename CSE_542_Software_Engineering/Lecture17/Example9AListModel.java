import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * This object holds and can be used to manipulate all of the data inside a list.
 *
 * @author Matthew Hertz
 */
@SuppressWarnings({"rawtypes"})
public class Example9AListModel implements ListModel {
  private ArrayList<String> data;
  private LinkedList<ListDataListener> listeners;

  /** Create a new object that holds data for a list.  This will initially be empty. */
  public Example9AListModel() {
    data = new ArrayList<String>();
    listeners = new LinkedList<ListDataListener>();
  }

  /**
   * Add another piece of data to the end of this list.
   *
   * @param item Piece of data to be added to the list.
   */
  public void addData(String item) {
    data.add(item);

    ListDataEvent lde = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, data.size() - 1, data.size());
    for (ListDataListener listener : listeners) {
      //listener.contentsChanged(lde);
    }
  }
  
  public String getElementAt(int arg0) {
    return data.get(arg0);
  }

  public int getSize() {
    return data.size();
  }

  public void removeData(String item) {
    data.remove(item);

    ListDataEvent lde = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, data.size() - 1, data.size());
    for (ListDataListener listener : listeners) {
      listener.contentsChanged(lde);
    }
  }

  public void addListDataListener(ListDataListener arg0) {
    listeners.add(arg0);
  }

  public void removeListDataListener(ListDataListener arg0) {
    listeners.remove(arg0);
  }
}
