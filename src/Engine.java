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
 * @Debugger Mike Zhang
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
	private MessageLogFrame log;
	private GameBoardPanel gameBoard;
	private BufferedImage countryMap; //Stores map with corresponding country images
	private String phaseCompleteDir;
	private ImageIcon phaseCompleteImage;
	private JButton phaseComplete;
	protected ColorTurnIndicator turnIndicator;
	protected int previousState;
	protected Color previousColor;
	protected JLabel reinIndicator;
	private File countryFile;
	private File neighborFile;
	private File continentFile;
	
	/*
	 * Text versions:
	 * PRE_GAME, OCCUPY = "OCCUPY"
	 * REINFORCE_A, RECRUIT = "REINFORCE"
	 * ATTACK_A, ATTACK_B = "ATTACK"
	 * FORTIFY = "FORTIFY"
	 * END_GAME = "GAME OVER"
	 */
	public Engine(File mapCountries, File mapNeighbors, File mapContinents, GuiFrame gui) throws FileNotFoundException{
		// Initialize the array variables
		countryFile = mapCountries;
		neighborFile = mapNeighbors;
		continentFile = mapContinents;
		gameGui = gui;
		log = gui.messages;
		countries = new ArrayList<Country>();
		continents = new ArrayList<Continent>();
		setupGame();
	}
	
	public void setupGame() throws FileNotFoundException {
		countries.clear();
		continents.clear();
		
		countries = new ArrayList<Country>();
		continents = new ArrayList<Continent>();
		
		// Fill the countries array with the contents of Countries.txt file
		Scanner a = new Scanner(countryFile);
		buildCountries(a);
		
		// Add neighbors to each country
		a = new Scanner(neighborFile);
		buildNeighbors(a);
		
		// Fill the continents array with the contents of the Continents.txt file
		a = new Scanner(continentFile);
		buildContinents(a);
		
		// Initalize the GUI
		gameBoard = new GameBoardPanel(countries);
		gameGui.setGameBoardPanelInformation(gameBoard);
	}
	
	public void start() throws IOException{
		donor = countries.get(0);
		reciever = countries.get(0);
		gameState = PRE_GAME;
		countryMap = ImageIO.read(new File("resources/map.png"));
		previousState = 0;
		previousColor = Color.gray;
		setUpGameBoardListeners();
		setupMainMenuListener();
	}
	
	public void randomize() throws InterruptedException{
		for (Country a: countries)
			readClick(a);
	}
	
	public void restart() throws IOException, InterruptedException {
		setupGame();
		start();
	}
	
	public void setUpGameBoardListeners() {
		gameBoard.addMouseListener(
			new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					//invalid click color index = 1000
					int colorIndex = 1000;
					// Debug code
					//System.out.println(e.getX() + " " + e.getY());
					if (e.getX() < 1160 && e.getX() > 14 && e.getY() >= 0 && e.getY() < 670)
						colorIndex = new Color((((BufferedImage) countryMap).getRGB(e.getX() - 14, e.getY()))).getBlue();
					processColor(colorIndex, e.getPoint());
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
					try {
						randomize();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					updateIndicator();
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
		NumberOfPlayersFrame s = new NumberOfPlayersFrame(gameGui, log);
		s.setLocationRelativeTo(gameGui);
		s.setVisible(true);
		
		if (s.getAccepted()) {
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
			countries.add(new Country(in.nextLine(), Integer.parseInt(in.nextLine()), Integer.parseInt(in.nextLine()), log));
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
					log.write("Engine 1");
					log.write("" + b.completeControl(a));
					a.continents.add(b);
				}
			}
		}
	}

	
	public void readClick(Country c) throws InterruptedException {
		/*
		 * Series of if statements can be subbed by a single
		 * switch (select case structure)
		 */
		if (gameState == PRE_GAME) preGame(c);
		else if (gameState == REINFORCE) reinforce(c);
		else if (gameState == RECRUIT) recruit(c);
		else if (gameState == ATTACK_A) attackA(c);
		else if (gameState == ATTACK_B) attackB(c);
		else if (gameState == OCCUPY) occupy(c);
		else if (gameState == FORTIFY_A) fortifyA(c);
		else if (gameState == FORTIFY_B) fortifyB(c);
		else if (gameState == FORTIFY_C) fortifyC(c);
		else if (gameState == END_GAME){ log.write("ENGINE 2"); log.write("GAME OVER");}
		for (Country a: countries)
			a.updateLabel();
		
		changeInstruction();
	}
	
	public void changeInstruction() {
		switch(gameState) {
			case PRE_GAME: 
				gameBoard.instructionLabel.setText(GameBoardPanel.sPREGAME);
				break;
			case REINFORCE:
				gameBoard.instructionLabel.setText(GameBoardPanel.sREINFORCE);
				break;
			case RECRUIT:
				gameBoard.instructionLabel.setText(GameBoardPanel.sRECRUIT);
				break;
			case ATTACK_A:
				gameBoard.instructionLabel.setText(GameBoardPanel.sATTACK_A);
				break;
			case ATTACK_B:
				gameBoard.instructionLabel.setText(GameBoardPanel.sATTACK_B);
				break;
			case OCCUPY:
				gameBoard.instructionLabel.setText(GameBoardPanel.sOCCUPY);
				break;
			case FORTIFY_A:
				gameBoard.instructionLabel.setText(GameBoardPanel.sFORTIFY);
				break;
			case FORTIFY_B:
				gameBoard.instructionLabel.setText(GameBoardPanel.sFORTIFY_B);
				break;
			case FORTIFY_C:
				gameBoard.instructionLabel.setText(GameBoardPanel.sFORTIFY_C);
			case END_GAME:
				gameBoard.instructionLabel.setText(GameBoardPanel.sENDGAME);
				break;
		}
	}
	
	public void endClick(){
		if (gameState == ATTACK_A || gameState == ATTACK_B) gameState = FORTIFY_A;
		else if (gameState == OCCUPY) {
			gameState = ATTACK_A;
		} else if (gameState == FORTIFY_A || gameState == FORTIFY_B || gameState == FORTIFY_C){
			rotate();
			turn.reinforcements();
			gameState = RECRUIT;
		}
		
		donor.toggleSpecialOff();
		changeInstruction();
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
		donor = c;
		if (!c.army.equals(turn) || c.troops <= 1) return;
		gameState = ATTACK_B;
		donor.toggleSpecialOn();
	}
	
	public void attackB(Country c) throws InterruptedException{
		if (c.equals(donor)){
			gameState = ATTACK_A;
			donor.toggleSpecialOff();
			return;
		}
		reciever = c;
		if (c.invade(donor)){
			updateGame();
			if (armies.size() == 1) gameState = END_GAME;
			else if (donor.troops >= 1) gameState = OCCUPY;
			else {
				gameState = ATTACK_A;
				donor.toggleSpecialOff();
			}
		}
		if (donor.troops <= 1){
			gameState = ATTACK_A;
			donor.toggleSpecialOff();
		}
	}
	
	public void occupy(Country c){
		if (!c.equals(reciever)) return;
		if (donor.troops <= 2){
			gameState = ATTACK_A;
			donor.toggleSpecialOff();
		}
		donor.troops --;
		reciever.troops ++;
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
		if (donor.troops <= 2){
			rotate();
			turn.reinforcements();
			gameState = RECRUIT;
			donor.toggleSpecialOff();
		}
		donor.troops --;
		reciever.troops ++;
	}
	
	public void rotate(){
		if (armies.indexOf(turn) == armies.size() - 1) turn = armies.get(0);
		else turn = armies.get(armies.indexOf(turn) + 1);
	}
}
