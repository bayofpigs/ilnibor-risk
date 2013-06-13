import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import java.io.File;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NumberOfPlayersFrame extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2278594614920750336L;
	private Dimension dialogSize;
	private ArrayList<JLabel> colorLabels;
	private ArrayList<JTextField> nameFields;
	private final ArrayList<Color> COLORSLIST = new ArrayList<Color>(
			Arrays.asList(new Color[]{Color.red, Color.blue, Color.green,
			Color.orange, Color.yellow} ));
	private ArrayList<Color> availColors = new ArrayList<Color>();
	private JComboBox<Integer> combo;
	private JPanel namePanel;
	private JPanel comboPanel;
	private JPanel okayCancelPanel;
	private JButton okay;
	private JButton cancel;
	private String[] possibleNames;
	private boolean accepted;
	
	public NumberOfPlayersFrame() {
		dialogSize = new Dimension(200, 300);
		this.setTitle("How many players?");
		this.setSize(dialogSize);
		this.setResizable(false);
		accepted = false;
		this.setLayout(new BorderLayout());
		comboPanel = new JPanel();
		namePanel = new JPanel();
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
		okayCancelPanel = new JPanel();
		
		nameFields = new ArrayList<JTextField>();
		colorLabels = new ArrayList<JLabel>();
		
		Integer[] comboOptions = new Integer[] {1, 2, 3, 4, 5};
		combo = new JComboBox<Integer>(comboOptions);
		combo.setSelectedItem(new Integer(3));
		comboPanel.add(combo);
		
		availColors = COLORSLIST;
		for (int i = 0; i < (int)combo.getSelectedItem(); i++) {
			namePanel.add(createAndGetNewPanel());
		}
		
		okay = new JButton("Okay");
		cancel = new JButton("Cancel");
		okayCancelPanel.add(okay);
		okayCancelPanel.add(cancel);
		
		this.add(comboPanel, BorderLayout.NORTH);
		this.add(namePanel, BorderLayout.CENTER);
		this.add(okayCancelPanel, BorderLayout.SOUTH);
	}
	
	public JPanel createAndGetNewPanel() {
		Dimension labelSize = new Dimension(20, 20);
		JPanel newPanel = new JPanel();
		
		JLabel newLabel = new JLabel();
		newLabel.setSize(labelSize);
		int index = (int)(Math.random() * availColors.size());
		newLabel.setBackground(availColors.remove(index));
		newLabel.setOpaque(true);
		colorLabels.add(newLabel);
		
		JTextField newTextField = new JTextField(10);
		
		newPanel.add(newLabel);
		newPanel.add(newTextField);
		nameFields.add(newTextField);
		
		return newPanel;
	}
	
	public boolean getAccepted() {
		return accepted;
	}
	
	public static void main(String[] args) {
		NumberOfPlayersFrame n = new NumberOfPlayersFrame();
		n.setVisible(true);
	}
}
