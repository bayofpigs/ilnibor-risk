import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Insets;


public class MainMenuPanel extends JPanel {
	private static final long serialVersionUID = 3614630489279583940L;
	private final int BUTTON_WIDTH = 250;
	private final int BUTTON_HEIGHT = 90;
	private final Dimension BGSIZE = new Dimension(900, 700);
	private Image bg;
	private ImageIcon startButtonImg;
	private ImageIcon exitButtonImg;
	private String bgDir;
	private String startDir;
	private String exitDir;
	private JButton startButton;
	private JButton exitButton;

	public MainMenuPanel() {
		setPreferredSize(BGSIZE);
		setMinimumSize(BGSIZE);
		setMaximumSize(BGSIZE);
		setLayout(null);
		
		bgDir = "resources/bgimage.png";
		startDir = "resources/startbutton.png";
		exitDir = "resources/exitbutton.png";
		bg = new ImageIcon(bgDir).getImage();
		startButtonImg = new ImageIcon(startDir);
		exitButtonImg = new ImageIcon(exitDir);
		startButton = new JButton(startButtonImg);
		exitButton = new JButton(exitButtonImg);
		Insets insets = this.getInsets();
		startButton.setBounds(insets.left + 150, insets.top + 500, 
				BUTTON_WIDTH, BUTTON_HEIGHT);
		exitButton.setBounds(insets.left + 150 + BUTTON_WIDTH + 75, insets.top + 500,
				BUTTON_WIDTH, BUTTON_HEIGHT);
		
		add(startButton);
		add(exitButton);
	}
	
	public JButton getStartButton() {
		return startButton;
	}
	
	public JButton getExitButton() {
		return exitButton;
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(bg, 0, 0, null);
	}
}
