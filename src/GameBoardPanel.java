import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;

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
	private String phaseCompleteDir;
	private ImageIcon phaseCompleteImage;
	private JButton phaseComplete;
	private Image mapImg; // Stores the map image
	private Engine game; // The engine of the game
	private final Dimension BGSIZE; // The size of the map
	private boolean buttonAppear;
	
	public GameBoardPanel(Engine en) {
		buttonAppear = false;
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
		mapImg = (new ImageIcon(mapImgDir)).getImage();
		
		// setup the labels of troop numbers for each country as prescribed by 
		// the game engine and resources/countries.txt
		setUpButtons(game.countries);
		
		// Setups turn based labels and buttons
		setupCurrentPlayer();
		
		//  Click Coordinate finder, for debug purposes
		this.addMouseListener(
			new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					System.out.println(e.getPoint());
				}
			}
		);
	}
	
	public void setupCurrentPlayer() {
		phaseCompleteDir = "resources/turndone.png";
		phaseCompleteImage = new ImageIcon(phaseCompleteDir);
		phaseComplete = new JButton(phaseCompleteImage);
		Insets insets = this.getInsets();
		phaseComplete.setBounds(insets.left + 46, insets.top + 443, 
								phaseCompleteImage.getIconWidth(), 
								phaseCompleteImage.getIconHeight());
		
		add(phaseComplete);
	}
	/**
	 * TODO:
	 * Having some problems in this method/reading clicks; check it out
	 */
	/**
	 * Places an arraylist of Countries in their appropriate positions 
	 * In general, is called from the constructor with reference to the 
	 * game engine class
	 * @param countries
	**/
	public void setUpButtons(ArrayList<Country> countries) {
		for (Country a: countries) {
			a.addMouseListener(
				// adds a click listener to each country
				new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						Country country = (Country)(e.getSource());
						boolean transparent = ((BufferedImage) country.image).getRGB(e.getX(), e.getY()) == Color.white.getRGB();
						if (!transparent) {
							processClick(country);
						}
					}
				}
			) ;
			add(a);
		}
	}
	
	public void processClick(Country c) {
		// sends information on the country clicked to the Engine to be processed
		game.readClick(c);
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
