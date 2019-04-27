
import java.awt.event.ActionEvent;

import javax.swing.JButton;

/**
 * Class we will use to test that our code in the Example6 class works.
 * 
 * @author Matthew Hertz
 */
public class Example6ATest {
	private static Example6A instance;

	public static void main(String[] args) {
		instance = new Example6A();
		clickBlueButton();
	}

	public static void clickRedButton() {
		JButton ridingHood = instance.getRedButton();
		ActionEvent ae = new ActionEvent(ridingHood, 0, null);
		instance.actionPerformed(ae);
		System.out.println(instance.getLabelColor());
	}
	
	public static void clickGreenButton() {
		ActionEvent ae = new ActionEvent(null, 0, null);
		instance.actionPerformed(ae);
		System.out.println(instance.getLabelColor());
	}

	public static void clickBlueButton() {
		JButton littleBoy = instance.getBlueButton();
		littleBoy.doClick();
		System.out.println(instance.getLabelColor());
	}
}
