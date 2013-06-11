import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.BorderLayout;

public class OccupySlider extends JFrame {
	JPanel centerPanel;
	JLabel useLabel;
	JSlider unitSlider = new JSlider();
	JLabel unitCounter;
	
	public OccupySlider(int minValue, int maxValue) {
		centerPanel = new JPanel(new BorderLayout());
		useLabel = new JLabel("Number Of Troops");
		unitSlider.setMinimum(minValue);
		unitSlider.setMaximum(maxValue);
		unitSlider.setValue(maxValue);
		unitSlider.setMajorTickSpacing(1);
		unitSlider.setMinorTickSpacing(1);
		unitSlider.setPaintTicks(true);
		unitSlider.setPaintLabels(true);
		
	}
	
	public JSlider getSlider() {
		return unitSlider;
	}
	
	public static void main(String[] args) {
		
	}
}
