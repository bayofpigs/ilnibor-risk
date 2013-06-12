import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JDialog;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OccupySlider extends JDialog {
	JPanel centerPanel;
	JLabel useLabel;
	JSlider unitSlider = new JSlider();
	JLabel unitCounter;
	Dimension size;
	JButton confirm = new JButton();;
	Country toOccupy;
	
	public OccupySlider(int minValue, int maxValue, Country target) {
		toOccupy = target;
		this.setLayout(new BorderLayout());
		centerPanel = new JPanel();
		this.setResizable(false);
		size = new Dimension(300, 200);
		this.setSize(size);
		this.setLocationRelativeTo(null);
		
		confirm.setText("Confirm");
		useLabel = new JLabel("Number Of Troops");
		unitSlider.setMinimum(minValue);
		unitSlider.setMaximum(maxValue);
		unitSlider.setValue(maxValue);
		unitSlider.setMajorTickSpacing(1);
		unitSlider.setMinorTickSpacing(1);
		unitSlider.setPaintTicks(true);
		unitSlider.setPaintLabels(true);
		unitCounter = new JLabel("" + unitSlider.getValue());
		
		centerPanel.add(useLabel);
		centerPanel.add(unitSlider);
		centerPanel.add(unitCounter);
		this.getRootPane().setDefaultButton(confirm);
		confirm.requestFocus();
		setUpListeners();
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(confirm, BorderLayout.SOUTH);
		this.setVisible(true);
	}
	
	public Country getTarget() {
		return toOccupy;
	}
	
	public JSlider getSlider() {
		return unitSlider;
	}
	
	public int getSliderValue() {
		return unitSlider.getValue();
	}
	
	public void setUpListeners() {
		unitSlider.addChangeListener(
			new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent arg0) {
					// TODO Auto-generated method stub
					unitCounter.setText("" + ((JSlider)arg0.getSource()).getValue());
				}
				
			}
		);
		
		confirm.addActionListener(
			new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					exit();
				}
			}
		);
	}
	
	public void exit() {
		this.dispose();
	}
	
}
