import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * The "board" of the risk game. Is called from the mainmenu
 * upon user click of the StartButton
 * @author Michael Zhang
 * @author Akhil Velagapudi
 *
 * @see GuiFrame
 * @see MainMenuPanel
 * @see Engine
 * @see Country
 * 
 */
public class GameBoardPanel extends JPanel{
	private String mapImgDir; // String directory of where the map image is located
	private String countryMapDir; //String directory of where the country map image is located
	private String phaseCompleteDir;
	private ImageIcon phaseCompleteImage;
	private JButton phaseComplete;
	private Image mapImg; // Stores the map image
	private BufferedImage countryMap; //Stores map with corresponding country images
	private Engine game; // The engine of the game
	private final Dimension BGSIZE; // The size of the map
	private ColorTurnIndicator turnIndicator;
	private int previousState;
	private Color previousColor;
	private JLabel reinIndicator;
	
	public GameBoardPanel(Engine en) {
		previousState = 0;
		previousColor = Color.gray;
		
		// Set the size of the gameboard
		BGSIZE = new Dimension(1179, 700);
		
		// initialize the engine
		game = en;
		
		// set the size of the gameboard
		setPreferredSize(BGSIZE);
		setMaximumSize(BGSIZE);
		setMinimumSize(BGSIZE);
		setSize(BGSIZE);
		
		// set the layout of the gameboard to null layout
		setLayout(null);
		
		// Insert the map image into the mapImg variable
		// The map is drawn onto the gameboard in the paintcomponent() method
		mapImgDir = "resources/map.jpg";
		mapImg = new ImageIcon(mapImgDir).getImage();
		
		// Creates the map object that contains the corresponding countries associated
		// with x, y coordinates on map.jpg
		countryMapDir = "resources/map.png";
		try {
			countryMap = ImageIO.read(new File(countryMapDir));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for (Country a: game.countries)
			add(a);
		setupCurrentPlayer();
		
		//  Click Coordinate finder, for usage with country buttons
		this.addMouseListener(
			new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					int index = 1000;
					if (e.getX() < 1160)
						index = new Color((((BufferedImage) countryMap).getRGB(e.getX() - 14, e.getY()))).getBlue();
					processColor(index, e.getPoint());
				}
			}
		);
	}
	
	// Processes the color clicked and uses information to update countries
	public void processColor(int blueIndex, Point x)
	{
		if (blueIndex >= 0 && blueIndex < game.countries.size())
			try {
				processClick(game.countries.get(blueIndex));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
	// Set ups the turn indicator
	public void setupCurrentPlayer() {
		phaseCompleteDir = "resources/turndone.png";
		phaseCompleteImage = new ImageIcon(phaseCompleteDir);
		phaseComplete = new JButton(phaseCompleteImage);
		Insets insets = this.getInsets();
		phaseComplete.setBounds(insets.left + 46, insets.top + 443, 
								phaseCompleteImage.getIconWidth(), 
								phaseCompleteImage.getIconHeight());
		phaseComplete.addMouseListener(
			new MouseAdapter() {public void mouseClicked(MouseEvent e) {endClick();}}
		);
		
		turnIndicator = new ColorTurnIndicator();
		Dimension indDim = turnIndicator.getDim();
		turnIndicator.setBounds(insets.left + 46, insets.top + 530,
								indDim.width, indDim.height);
		
		reinIndicator = new JLabel();
		
		turnIndicator.setText(game.gameState);
		previousState = game.gameState;
		turnIndicator.changeColor(game.turn.armyColor);
		
		reinIndicator.setText("<html><font color = \"white\" size = \"5\">Reinforcements: " 
														+ game.turn.reinforcements + "</font></head>");
		reinIndicator.setBounds(insets.left + 46, insets.top + 590, indDim.width, indDim.height);
		
		previousColor = game.turn.armyColor;
		add(phaseComplete);
		add(turnIndicator);
		add(reinIndicator);
	}
	
	// Updates the indicator
	public void updateIndicator() {
		if (game.gameState != previousState) {
			turnIndicator.setText(game.gameState);
		}
		previousState = game.gameState;
		if (!game.turn.armyColor.equals(previousColor)) {
			turnIndicator.changeColor(game.turn.armyColor);
		}
		previousColor = game.turn.armyColor;
		
		reinIndicator.setText("<html><font color = \"white\" size = \"5\">Reinforcements: " 
				+ game.turn.reinforcements + "</font></head>");
	}
	public void processClick(Country c) throws InterruptedException {
		// sends information on the country clicked to the Engine to be processed
		game.readClick(c);
		updateIndicator();
	}
	
	public void endClick(){
		game.endClick();
		updateIndicator();
	}
	
	/**
	 * Override of the default JPanel paintComponent method to 
	 * paint the map onto the screen
	 */
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, BGSIZE.width, BGSIZE.height);
		g.drawImage(mapImg, 15, 0, null);
	}
}
