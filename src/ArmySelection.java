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

public class ArmySelection extends JDialog{
	private static final long serialVersionUID = 2278594614920750336L;
	private Dimension dialogSize;
	private ArrayList<JLabel> colorLabels;
	private ArrayList<JTextField> nameFields;
	private final Color[] COLORS = new Color[]{Color.MAGENTA, Color.BLUE, Color.GRAY, Color.RED, Color.GREEN};
	private ArrayList<Color> availColors = new ArrayList<Color>();
	private JComboBox<Integer> combo;
	private JPanel namePanel;
	private JPanel comboPanel;
	private JPanel okayCancelPanel;
	private JButton okay;
	private JButton cancel;
	private boolean userClickOK;
	private int currentPlayerNum;
	public MessageLog log;
	
	public ArmySelection(GuiFrame parentFrame) {
		super(parentFrame);
		log = parentFrame.messages;
		this.setModal(true);
		dialogSize = new Dimension(200, 350);
		this.setTitle("How many players?");
		this.setSize(dialogSize);
		this.setResizable(false);
		userClickOK = false;
		this.setLayout(new BorderLayout());
		comboPanel = new JPanel();
		namePanel = new JPanel();
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
		okayCancelPanel = new JPanel();
		
		nameFields = new ArrayList<JTextField>();
		colorLabels = new ArrayList<JLabel>();
		
		Integer[] comboOptions = new Integer[] {2, 3, 4, 5};
		combo = new JComboBox<Integer>(comboOptions);
		combo.setSelectedItem(new Integer(3));
		comboPanel.add(combo);
		
		setNameLayout((Integer)combo.getSelectedItem());
		
		okay = new JButton("OK");
		cancel = new JButton("Cancel");
		okayCancelPanel.add(okay);
		okayCancelPanel.add(cancel);
		
		this.add(comboPanel, BorderLayout.NORTH);
		this.add(namePanel, BorderLayout.CENTER);
		this.add(okayCancelPanel, BorderLayout.SOUTH);
		
		setupListeners();
	}
	
	public JPanel createAndGetNewPanel() {
		Dimension labelSize = new Dimension(20, 20);
		JPanel newPanel = new JPanel();
		
		JLabel newLabel = new JLabel();
		newLabel.setSize(labelSize);
		int index = (int)(Math.random() * availColors.size());
		newLabel.setForeground(availColors.remove(index));
		newLabel.setText("Player " + currentPlayerNum + "'s Name:");
		colorLabels.add(newLabel);
		
		JTextField newTextField = new JTextField(10);
		
		newPanel.add(newLabel);
		newPanel.add(newTextField);
		nameFields.add(newTextField);
		
		return newPanel;
	}
	
	public void setupListeners() {
		combo.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setNameLayout((Integer)combo.getSelectedItem());
				}
			}
		);
		
		okay.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					userClickOK = true;
					exit();
				}
			}
		);
		
		cancel.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					userClickOK = false;
					exit();
				}
			}
		);
	}
	
	public void setNameLayout(int numSelected) {
		availColors.clear();
		availColors = new ArrayList<Color>(Arrays.asList(COLORS));
		colorLabels.clear();
		nameFields.clear();
		namePanel.removeAll();
		for (currentPlayerNum = 1; currentPlayerNum <= numSelected; currentPlayerNum++) {
			namePanel.add(createAndGetNewPanel());
		}
		
		namePanel.revalidate();
		namePanel.repaint();
	}
	
	public ArrayList<Army> getArmyList() {
		ArrayList<Integer> riskValues = new ArrayList<Integer>();
		for (int i = 2; i < 40; i += 2)
			riskValues.add(i);
		ArrayList<String> randomNames = new ArrayList<String>();
		Scanner nameScanner = null;
		
		try {
			nameScanner = new Scanner(new File("resources/PlayerNames.txt"));
			int numNames = Integer.parseInt(nameScanner.nextLine());
			for (int i = 0; i < numNames; i++) {
				randomNames.add(nameScanner.nextLine());
			}
			
			nameScanner.close();
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//System.out.println(randomNames);
		
		ArrayList<Army> armies = new ArrayList<Army>();
		for (int i = 0; i < colorLabels.size(); i++) {
			String nextName = nameFields.get(i).getText();
			if (nextName.trim().equals("")) {
				int nextDigit = (int)(Math.random() * randomNames.size());
				nextName = randomNames.remove(nextDigit);
			}
			
			armies.add(new Army(colorLabels.get(i).getForeground(), nextName, log, colorLabels.size(), riskValues));
		}
		log.write("Players:", "NO_LINEMARKER", Color.WHITE);
		for(Army army : armies)
			if (army.armyName.equals(armies.get(armies.size() - 1).armyName))
				log.write(army.armyName, army.armyColor);
			else
				log.write(army.armyName,"NO_LINEMARKER", army.armyColor);
		return armies;
	}
	
	public void exit() {
		this.dispose();
	}
	
	public boolean getAccepted() {
		return userClickOK;
	}	
}