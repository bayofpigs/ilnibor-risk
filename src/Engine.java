import java.awt.Color;
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
 * @version 2.0
 * @see Game
 */

public class Engine {
	
	public static final int PRE_GAME = 0, REINFORCE = 1, RECRUIT = 2, ATTACK_A = 3, ATTACK_B = 4, OCCUPY = 5, FORTIFY_A = 6, FORTIFY_B = 7, FORTIFY_C = 8;
	public ArrayList<Country> countries; // The array of countries; to be read from Countries.txt
	public ArrayList<Continent> continents; // The array of continents, to be read from Continents.txt
	public ArrayList<Army> armies; // The array of Armies to be read from input
	public Army turn;
	public int gameState;
	
	private Country donor, reciever;
	private GuiFrame gameGui;
	private GameBoard gameBoard;
	private JButton phaseComplete;
	protected TurnIndicator turnIndicator;
	protected JLabel reinIndicator;
	
	public Engine(File mapCountries, File mapNeighbors, File mapContinents) throws FileNotFoundException{
		gameGui = new GuiFrame();
		countries = new ArrayList<Country>();
		continents = new ArrayList<Continent>();
		Scanner a = new Scanner(mapCountries);
		buildCountries(a);
		a = new Scanner(mapNeighbors);
		buildNeighbors(a);
		a = new Scanner(mapContinents);
		buildContinents(a);
		gameBoard = new GameBoard(countries);
		gameGui.setGameBoardPanelInformation(gameBoard);
	}
	public void start() throws IOException{
		donor = countries.get(0);
		reciever = countries.get(0);
		gameState = PRE_GAME;
		colorAnalyzer();
		mainMenu();
		gameGui.setVisible(true);
	}
	
	public void initiateGame() {
		ArmySelection s = new ArmySelection(gameGui);
		s.setLocationRelativeTo(gameGui);
		s.setVisible(true);
		if (s.getAccepted()) {
			armies = s.getArmyList();
			turn = armies.get(0);
			gameInformation();
			gameGui.flipToGame();
		}
	}
	
	public void colorAnalyzer() throws IOException {
		final BufferedImage map = ImageIO.read(new File("resources/map.png"));
		gameBoard.addMouseListener(
			new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					int blueIndex = 1000;
					blueIndex = new Color(map.getRGB(e.getX(), e.getY())).getBlue();
					if (blueIndex >= 0 && blueIndex < countries.size())
						try {processClick(countries.get(blueIndex));}
						catch (InterruptedException e1) {}
				}
			}
		);
	}
	
	public void gameInformation() {
		phaseComplete = new JButton(new ImageIcon("resources/turndone.png"));
		phaseComplete.setBounds(46, 443, 200, 72);
		phaseComplete.addMouseListener(
			new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {endClick();}
			}
		);
		gameBoard.add(phaseComplete);
		turnIndicator = new TurnIndicator();
		turnIndicator.setBounds(46, 530, 200, 72);
		turnIndicator.setText(gameState);
		turnIndicator.changeColor(turn.armyColor);
		gameBoard.add(turnIndicator);
		reinIndicator = new JLabel();
		reinIndicator.setText("<html><font color = \"white\" size = \"5\">Reinforcements: " + turn.reinforcements + "</font></head>");
		reinIndicator.setBounds(46, 590, 200, 72);
		gameBoard.add(reinIndicator);
	}
	
	/**
	 * Sets up listener for the start and exit buttons on the mainmenu
	 */
	public void mainMenu() {
		gameGui.mainMenu.startButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {initiateGame();}
			}		
		);
		gameGui.mainMenu.exitButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {gameGui.exitApp();}
			}
		);			
	}
	
	
	public void processClick(Country c) throws InterruptedException {
		switch (gameState) {
			case PRE_GAME:	preGame(c); break;
			case REINFORCE:	reinforce(c); break;
			case RECRUIT:	recruit(c); break;
			case ATTACK_A:	attackA(c); break;
			case ATTACK_B: 	attackB(c); break;
			case OCCUPY: 	occupy(c); break;
			case FORTIFY_A: fortifyA(c); break;
			case FORTIFY_B: fortifyB(c); break;
			case FORTIFY_C: fortifyC(c); break;
		} updateGame();
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
		for (int i = 0; i < armies.size(); i ++){
			Army a = armies.get(i);
			if (a.countries.size() == 0 && gameState > REINFORCE) armies.remove(i);
			for (Continent b: continents){
				a.continents.remove(b);
				if (b.completeControl(a)) a.continents.add(b);
			}
		}
		for (Country a: countries)
			a.updateLabel();
		turnIndicator.setText(gameState);
		turnIndicator.changeColor(turn.armyColor);
		reinIndicator.setText("<html><font color = \"white\" size = \"5\">Reinforcements: " + turn.reinforcements + "</font></head>");
		if (armies.size() == 1) endGame();
	}
	
	public void endClick(){
		if (gameState == ATTACK_A || gameState == ATTACK_B) gameState = FORTIFY_A;
		else if (gameState == OCCUPY) gameState = ATTACK_A;
		else if (gameState == FORTIFY_A || gameState == FORTIFY_B || gameState == FORTIFY_C){
			rotate();
			turn.reinforcements();
			gameState = RECRUIT;
		} updateGame();
		donor.special = false;
	}
	
	public void preGame(Country c){
		if (c.troops != 0) return;
		c.army = turn;
		turn.countries.add(c);
		c.troops ++;
		turn.reinforcements --;
		rotate();
		gameState = REINFORCE;
		for (Country a: countries)
			if (a.army == null) gameState = PRE_GAME;
	}
	
	public void reinforce(Country c){
		if (!c.army.equals(turn)) return;
		c.troops ++;
		turn.reinforcements --;
		rotate();
		if (turn.reinforcements == 0){
			gameState = RECRUIT;
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
		else if (c.invade(donor))
			if (donor.troops >= 2){
				gameState = OCCUPY;
				donor.special = true;
			} else gameState = ATTACK_A;
		else donor.special = true;
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
	
	public void endGame(){
		gameGui.messages.write("GAME OVER");
	}
	
	public void rotate(){
		if (armies.indexOf(turn) == armies.size() - 1) turn = armies.get(0);
		else turn = armies.get(armies.indexOf(turn) + 1);
	}
}
