import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Engine {
	ArrayList<Country> countries;
	ArrayList<Continent> continents;
	ArrayList<Army> armies;
	public Engine(File mapCountries, File mapNeighbors/*, File mapContinents, ArrayList<Army> gameArmies*/) throws FileNotFoundException{
		countries = new ArrayList<Country>();
		continents = new ArrayList<Continent>();
		armies = new ArrayList<Army>();
		Scanner a = new Scanner(mapCountries);
		buildCountries(a);
		a = new Scanner(mapNeighbors);
		buildNeighbors(a);
	}
	public void buildCountries(Scanner in){
		int a = Integer.parseInt(in.nextLine());
		for (int i = 0; i < a; i ++)
			countries.add(new Country(in.nextLine()));
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
		for (Country c: countries){
			System.out.println(c);
			System.out.println("\tNeighbors: " + c.neighbors);
		}
	}
	public static void main(String[] args) throws FileNotFoundException {
		Engine alpha = new Engine(new File("resources/Countries.txt"), new File("resources/Neighbors.txt"));
	}
}
