import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * The game engine for the Risk game
 * @author Akhil Velegapudi
 * @version 1.0
 * @see Game
 * @Code_Reviewer Mike Zhang
 *
 *TODO
 *Separate the gameArmies build variable thing from the constructor or at least add a separate setArmies
 *once debugging is over. It would be better to have the user input the number of players and
 *the army names through the GUI.
 *
 */

public class Engine {
	public ArrayList<Country> countries; // The array of countries; to be read from Countries.txt
	public ArrayList<Continent> continents; // The array of continents, to be read from Continents.txt
	public ArrayList<Army> armies; // The array of Armies to be read from input
	public int gameState;
	public Army turn;
	private Country donor, reciever;
	public static final int PRE_GAME = 0, REINFORCE = 1, RECRUIT = 2, ATTACK_A = 3, 
			ATTACK_B = 4, OCCUPY = 5, FORTIFY_A = 6, FORTIFY_B = 7, FORTIFY_C = 8, END_GAME = 9;
	private ArrayList<Integer> riskValues = new ArrayList<Integer>();
	
	private GuiFrame gameGui;
	private GameBoardPanel gameBoard;
	private BufferedImage countryMap; //Stores map with corresponding country images
	private String countryMapDir; //String directory of where the country map image is located
	private String phaseCompleteDir;
	private ImageIcon phaseCompleteImage;
	private JButton phaseComplete;
	protected ColorTurnIndicator turnIndicator;
	protected int previousState;
	protected Color previousColor;
	protected JLabel reinIndicator;
	
	/*
	 * Text versions:
	 * PRE_GAME, OCCUPY = "OCCUPY"
	 * REINFORCE_A, RECRUIT = "REINFORCE"
	 * ATTACK_A, ATTACK_B = "ATTACK"
	 * FORTIFY = "FORTIFY"
	 * END_GAME = "GAME OVER"
	 */
	public Engine(File mapCountries, File mapNeighbors, File mapContinents, ArrayList<Army> gameArmies, GuiFrame gui) throws FileNotFoundException{
		// Initialize the array variables
		
		countries = new ArrayList<Country>();
		continents = new ArrayList<Continent>();
		
		// Fill the countries array with the contents of Countries.txt file
		Scanner a = new Scanner(mapCountries);
		buildCountries(a);
		
		// Add neighbors to each country
		a = new Scanner(mapNeighbors);
		buildNeighbors(a);
		
		// Fill the continents array with the contents of the Continents.txt file
		a = new Scanner(mapContinents);
		buildContinents(a);
		
		// Initalize the GUI
		gameGui = gui;
		gameBoard = new GameBoardPanel(countries);
		gui.setGameBoardPanelInformation(gameBoard);
		
	}
	
	public void start() {
		donor = countries.get(0);
		reciever = countries.get(0);
		gameState = PRE_GAME;
		
		countryMapDir = "resources/map.png";
		try {
			countryMap = ImageIO.read(new File(countryMapDir));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		previousState = 0;
		previousColor = Color.gray;
		setUpGameBoardListeners();
		setupMainMenuListener();
	}
	
	public void setUpGameBoardListeners() {
		gameBoard.addMouseListener(
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
	
	// Set ups the turn indicator
	public void setupGameCurrentPlayer() {
		phaseCompleteDir = "resources/turndone.png";
		phaseCompleteImage = new ImageIcon(phaseCompleteDir);
		phaseComplete = new JButton(phaseCompleteImage);
		Insets insets = gameBoard.getInsets();
		phaseComplete.setBounds(insets.left + 46, insets.top + 443, 
								phaseCompleteImage.getIconWidth(), 
								phaseCompleteImage.getIconHeight());
		phaseComplete.addMouseListener(
			new MouseAdapter() {
				
				public void mouseReleased(MouseEvent e) {
					endClick();
					updateIndicator();
				}
			}
		);
		
		turnIndicator = new ColorTurnIndicator();
		Dimension indDim = turnIndicator.getDim();
		turnIndicator.setBounds(insets.left + 46, insets.top + 530,
								indDim.width, indDim.height);
		
		reinIndicator = new JLabel();
		
		turnIndicator.setText(gameState);
		previousState = gameState;
		turnIndicator.changeColor(turn.armyColor);
		
		reinIndicator.setText("<html><font color = \"white\" size = \"5\">Reinforcements: " 
														+ turn.reinforcements + "</font></head>");
		reinIndicator.setBounds(insets.left + 46, insets.top + 590, indDim.width, indDim.height);
		
		previousColor = turn.armyColor;
		gameBoard.add(phaseComplete);
		gameBoard.add(turnIndicator);
		gameBoard.add(reinIndicator);
	}

	
	/**
	 * Sets up listener for the start and exit buttons on the mainmenu
	 */
	public void setupMainMenuListener() {
		// The start button from the main menu
		JButton start = gameGui.mainMenu.getStartButton();
		
		// The exit button from the mainmenu
		JButton exit = gameGui.mainMenu.getExitButton();
		start.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					initiateGame();
				}
			}		
		);
		
		exit.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					// On user click exit, the application exits.
					gameGui.exitApp();
				}
			}
		);
				
	}
	
	public void initiateGame() {
		// On user click start: the center panel flips to the main game
		NumberOfPlayersFrame s = new NumberOfPlayersFrame(gameGui);
		s.setLocationRelativeTo(gameGui);
		s.setVisible(true);
		
		if (s.getAccepted()) {
			System.out.println("I'm here!");
			ArrayList<Army> players = s.getArmyList();
			setArmies(players);
			
			for (int i = 2; i < 40; i += 2)
				riskValues.add(i);
			for (Army a: armies){
				a.addReinforcements(armies.size());
				a.addRiskValues(riskValues);
			}
			
			turn = armies.get(0);
			setupGameCurrentPlayer();
			
			gameGui.flipToGame();
		}
	}
	
	public void setArmies(ArrayList<Army> players) {
		armies = players;
	}
	
	// Processes the color clicked and uses information to update countries
	public void processColor(int blueIndex, Point x)
	{
		if (blueIndex >= 0 && blueIndex < countries.size())
			try {
				processClick(countries.get(blueIndex)); // retrieves color information based on country
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	
	public void processClick(Country c) throws InterruptedException {
		// sends information on the country clicked to the Engine to be processed
		readClick(c);
		updateIndicator();
	}
	
	// Updates the indicator
	public void updateIndicator() {
		if (gameState != previousState) {
			turnIndicator.setText(gameState);
		}
		previousState = gameState;
		if (!turn.armyColor.equals(previousColor)) {
			turnIndicator.changeColor(turn.armyColor);
		}
		previousColor = turn.armyColor;
		
		reinIndicator.setText("<html><font color = \"white\" size = \"5\">Reinforcements: " 
				+ turn.reinforcements + "</font></head>");
	}
	
	/**
	 * Builds the country array
	 * @param in: The scanner containing the file with the country data
	 */
	public void buildCountries(Scanner in){
		int a = Integer.parseInt(in.nextLine());
		for (int i = 0; i < a; i ++)
			countries.add(new Country(in.nextLine(), Integer.parseInt(in.nextLine()), Integer.parseInt(in.nextLine())));
	}
	
	/**
	 * Sets the neighbors of each country
	 * @param in: The scanner containing the file with the neighbor data
	 */
	public void buildNeighbors(Scanner in){
		int a = in.nextInt();
		for (int i = 0; i < a; i ++){
			ArrayList<Country> temp = new ArrayList<Country>();
			int b = in.nextInt();
			for (int j = 0; j < b; j ++)
				temp.add(countries.get(in.nextInt()));
			countries.get(i).addNeighbors(temp);
		}
	}
	
	/**
	 * Builds the continents array
	 * @param in: The scanner containing the file with Continent Data
	 */
	public void buildContinents(Scanner in){
		int a = Integer.parseInt(in.nextLine());
		for (int i = 0; i < a; i ++){
			String continentName = in.nextLine();
			int troopValue = Integer.parseInt(in.nextLine());
			ArrayList<Country> temp = new ArrayList<Country>();
			int b = Integer.parseInt(in.nextLine());
			int c = Integer.parseInt(in.nextLine());
			for (int j = b; j <= c; j ++)
				temp.add(countries.get(j));
			continents.add(new Continent(continentName, troopValue, temp));
		}
	}
	
	/**
	 * 
	 * @return A Boolean indicated whether or not any territories remained
	 * unoccupied
	 */
	public boolean unoccupiedTerritory(){
		for (Country a: countries)
			if (a.army == null) return true;
		return false;
	}
	
	public void updateGame(){
		for (Army a: armies){
			if (a.countries.size() == 0)
				armies.remove(a);
			for (Continent b: continents){
				a.continents.remove(b);
				if (b.completeControl(a)){
					System.out.println(b.completeControl(a));
					a.continents.add(b);
				}
			}
		}
	}

	
	public void readClick(Country c) throws InterruptedException {
		if (gameState == PRE_GAME) preGame(c);
		else if (gameState == REINFORCE) reinforce(c);
		else if (gameState == RECRUIT) recruit(c);
		else if (gameState == ATTACK_A) attackA(c);
		else if (gameState == ATTACK_B) attackB(c);
		else if (gameState == OCCUPY) occupy(c);
		else if (gameState == FORTIFY_A) fortifyA(c);
		else if (gameState == FORTIFY_B) fortifyB(c);
		else if (gameState == FORTIFY_C) fortifyC(c);
		else if (gameState == END_GAME) System.out.println("GAME OVER");
		for (Country a: countries)
			a.updateLabel();
	}
	
	public void endClick(){
		if (gameState == ATTACK_A || gameState == ATTACK_B) gameState = FORTIFY_A;
		else if (gameState == OCCUPY) gameState = ATTACK_A;
		else if (gameState == FORTIFY_A || gameState == FORTIFY_B || gameState == FORTIFY_C){
			rotate();
			turn.reinforcements();
			gameState = RECRUIT;
		}
		donor.toggleSpecialOff();
		for (Country a: countries)
			a.updateLabel();
	}
	
	public void preGame(Country c){
		if (c.troops != 0) return;
		c.occupy(turn);
		turn.reinforcements --;
		rotate();
		if (!unoccupiedTerritory()) gameState = REINFORCE;
	}
	
	public void reinforce(Country c){
		if (!c.army.equals(turn)) return;
		c.troops ++;
		turn.reinforcements --;
		rotate();
		gameState = RECRUIT;
		for (Army a: armies)
			if (a.reinforcements > 0) gameState = REINFORCE;
		if (gameState == RECRUIT){
			turn.reinforcements();
		}
	}
	
	public void recruit(Country c){
		if (!c.army.equals(turn)) return;
		c.troops ++;
		turn.reinforcements --;
		if (turn.reinforcements <= 0) gameState = ATTACK_A;
	}
	
	public void attackA(Country c){
		if (!c.army.equals(turn)) return;
		donor = c;
		gameState = ATTACK_B;
		donor.toggleSpecialOn();
	}
	
	public void attackB(Country c) throws InterruptedException{
		reciever = c;
		if (c.invade(donor)){
			updateGame();
			if (armies.size() == 1) gameState = END_GAME;
			else gameState = OCCUPY;
		}
		else{
			gameState = ATTACK_A;
			donor.toggleSpecialOff();
		}
	}
	
	public void occupy(Country c){
		if (!c.equals(reciever)) return;
		donor.troops --;
		reciever.troops ++;
		if (donor.troops == 1){
			gameState = ATTACK_A;
			donor.toggleSpecialOff();
		}
	}
	
	public void fortifyA(Country c){
		donor = c;
		if (!donor.army.equals(turn) || donor.troops <= 1) return;
		gameState = FORTIFY_B;
		donor.toggleSpecialOn();
	}
	
	public void fortifyB(Country c){
		reciever = c;
		if (!reciever.canReinforce(donor)) return;
		donor.troops --;
		reciever.troops ++;
		gameState = FORTIFY_C;
	}
	
	public void fortifyC(Country c){
		if (!c.equals(reciever)) return;
		donor.troops --;
		reciever.troops ++;
		if (donor.troops == 1){
			rotate();
			turn.reinforcements();
			gameState = RECRUIT;
			donor.toggleSpecialOff();
		}
	}
	
	public void rotate(){
		if (armies.indexOf(turn) == armies.size() - 1) turn = armies.get(0);
		else turn = armies.get(armies.indexOf(turn) + 1);
	}
}
