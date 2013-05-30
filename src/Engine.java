import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Engine {
	ArrayList<Country> countries;
	ArrayList<Continent> continents;
	ArrayList<Army> armies;
	public Engine(File mapCountries, File mapNeighbors, File mapContinents, ArrayList<Army> gameArmies) throws FileNotFoundException{
		countries = new ArrayList<Country>();
		continents = new ArrayList<Continent>();
		armies = gameArmies;
		Scanner a = new Scanner(mapCountries);
		buildCountries(a);
		a = new Scanner(mapNeighbors);
		buildNeighbors(a);
		a = new Scanner(mapContinents);
		buildContinents(a);
	}
	
	public void buildCountries(Scanner in){
		int a = Integer.parseInt(in.nextLine());
		for (int i = 0; i < a; i ++)
			countries.add(new Country(in.nextLine(), Integer.parseInt(in.nextLine()), Integer.parseInt(in.nextLine())));
	}
	
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
	
	public boolean unoccupiedTerritory(){
		for (Country a: countries)
			if (a.army == null) return true;
		return false;
	}
	
	public void preGame(){
		for (int i = 0; unoccupiedTerritory(); i ++){
			if (i > armies.size()) i = 0;
			occupyCountry().occupy(armies.get(i));
		}
	}
	
	public void game(){
		for (int i = 0; checkGame(); i ++){
			if (i > armies.size()) i = 0;
			
		}
	}
	
	public void reinforce(){
		
	}
	
	public boolean checkGame(){
		for (Army a: armies){
			if (a.countries.size() == 0)
				armies.remove(a);
			for (Continent b: continents)
				if (b.completeControl(a)) a.continents.add(b);
		}
		return (armies.size() > 1);
	}
	
	public void readClick(Country c) {
		System.out.println("Country " + c);
	}
	
	public Country occupyCountry(){
		//returns a country that is unoccupied (country.army = null) by reading mouse click
		Country clickedOn = new Country("This is the country they clicked on", 5, 5);
		if (clickedOn.army == null)
			return clickedOn;
		return occupyCountry();
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		Engine alpha = new Engine(new File("resources/Countries.txt"), new File("resources/Neighbors.txt"), new File("resources/Continents.txt"), null);
	}
}
