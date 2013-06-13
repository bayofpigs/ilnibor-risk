import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.awt.Color;

public class NumberOfPlayersFrame extends JDialog{
	ArrayList<JLabel> nameLabels;
	JComboBox<Integer> combo;
	JPanel topPanel;
	String[] possibleNames;
	
	public NumberOfPlayersFrame() {
		topPanel = new JPanel();
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		nameLabels = new ArrayList<JLabel>();
		combo = new JComboBox<Integer>();
		
	}
}
