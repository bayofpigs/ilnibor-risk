import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
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
 *
 */

public class Engine {
	
	public static final int PRE_GAME = 0, REINFORCE = 1, RECRUIT = 2, ATTACK_A = 3, 
	ATTACK_B = 4, OCCUPY = 5, FORTIFY_A = 6, FORTIFY_B = 7, FORTIFY_C = 8, END_GAME = 9;
	
	public ArrayList<Country> countries; // The array of countries; to be read from Countries.txt
	public ArrayList<Continent> continents; // The array of continents, to be read from Continents.txt
	public ArrayList<Army> armies; // The array of Armies to be read from input
	public Army turn;
	public int gameState;
	
	private Country donor, reciever;
	private ArrayList<Integer> riskValues = new ArrayList<Integer>();
	private GuiFrame gameGui;
	private GameBoardPanel gameBoard;
	private ImageIcon phaseCompleteImage;
	private JButton phaseComplete;
	private File countryFile;
	private File neighborFile;
	private File continentFile;
	
	protected ColorTurnIndicator turnIndicator;
	protected JLabel reinIndicator;

	
	/*
	 * Text versions:
	 * PRE_GAME, OCCUPY = "OCCUPY"
	 * REINFORCE_A, RECRUIT = "REINFORCE"
	 * ATTACK_A, ATTACK_B = "ATTACK"
	 * FORTIFY = "FORTIFY"
	 * END_GAME = "GAME OVER"
	 */
	public Engine(File mapCountries, File mapNeighbors, File mapContinents) throws FileNotFoundException{
		// Initialize the array variables
		countryFile = mapCountries;
		neighborFile = mapNeighbors;
		continentFile = mapContinents;
		gameGui = new GuiFrame();
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
		setUpGameBoardListeners();
		setupMainMenuListener();
		gameGui.setVisible(true);
	}
	
	public void setUpGameBoardListeners() throws IOException {
		final BufferedImage map = ImageIO.read(new File("resources/map.png"));
		gameBoard.addMouseListener(
			new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					int colorIndex = 1000;
					colorIndex = new Color(map.getRGB(e.getX(), e.getY())).getBlue();
					processColor(colorIndex);
				}
			}
		);
	}
	
	// Set ups the turn indicator
	public void setupGameCurrentPlayer() {
		phaseCompleteImage = new ImageIcon("resources/turndone.png");
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
		turnIndicator.changeColor(turn.armyColor);
		reinIndicator.setText("<html><font color = \"white\" size = \"5\">Reinforcements: " 
														+ turn.reinforcements + "</font></head>");
		reinIndicator.setBounds(insets.left + 46, insets.top + 590, indDim.width, indDim.height);
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
		InitiationFrame s = new InitiationFrame(gameGui);
		s.setLocationRelativeTo(gameGui);
		s.setVisible(true);
		
		if (s.getAccepted()) {
			ArrayList<Army> players = s.getArmyList();
			armies = players;
			
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
	
	// Processes the color clicked and uses information to update countries
	public void processColor(int blueIndex)
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
		turnIndicator.setText(gameState);
		turnIndicator.changeColor(turn.armyColor);
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
			countries.add(new Country(in.nextLine(), Integer.parseInt(in.nextLine()), Integer.parseInt(in.nextLine()), gameGui.messages));
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
	 * Returns whether any territories are still unoccupied
	 * @return A boolean that tells whether any territories are still unoccupied
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
					//Debug
					//log.write("Engine 1");
					//log.write("" + b.completeControl(a));
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
		switch (gameState) {
		case PRE_GAME:
			preGame(c);
			break;
		case REINFORCE:
			reinforce(c);
			break;
		case RECRUIT:
			recruit(c);
			break;
		case ATTACK_A:
			attackA(c);
			break;
		case ATTACK_B:
			attackB(c);
			break;
		case OCCUPY:
			occupy(c);
			break;
		case FORTIFY_A:
			fortifyA(c);
			break;
		case FORTIFY_B:
			fortifyB(c);
			break;
		case FORTIFY_C:
			fortifyC(c);
			break;
		case END_GAME:
			endGame(c);
			break;
		default:
			//log.write("INVALID GAME STATE IN ENGINE");
			break;
		}
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
				break;
			case END_GAME:
				gameBoard.instructionLabel.setText(GameBoardPanel.sENDGAME);
				break;
			default:
				//gameBoard.instructionLabel.setText("Invalid Game State in Engine");
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
		
		donor.special = false;
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
		donor.special = true;
	}
	
	public void attackB(Country c) throws InterruptedException{
		reciever = c;
		donor.special = false;
		if (c.equals(donor)) gameState = ATTACK_A;
		else if (c.invade(donor)){
			updateGame();
			if (armies.size() == 1) gameState = END_GAME;
			else if (donor.troops >= 2){
				gameState = OCCUPY;
				donor.special = true;
			}
			else gameState = ATTACK_A;
		} else donor.special = true;
	}
	
	public void occupy(Country c){
		if (!c.equals(reciever)) return;
		if (donor.troops <= 2){
			gameState = ATTACK_A;
			donor.special = false;
		}
		donor.troops --;
		reciever.troops ++;
	}
	
	public void fortifyA(Country c){
		donor = c;
		if (!donor.army.equals(turn) || donor.troops <= 1) return;
		gameState = FORTIFY_B;
		donor.special = true;
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
			donor.special = false;
		}
		donor.troops --;
		reciever.troops ++;
	}
	
	public void endGame(Country c){
		gameGui.messages.write("GAME OVER");
	}
	
	public void rotate(){
		if (armies.indexOf(turn) == armies.size() - 1) turn = armies.get(0);
		else turn = armies.get(armies.indexOf(turn) + 1);
	}
}
