import javax.swing.JLabel;
import java.awt.Color;

public class TroopLabel extends JLabel {
	private int numTroops;
	private Color color;
	
	public TroopLabel() {
		numTroops = 0;
		color = Color.gray;
		this.setOpaque(true);
		this.setNumber();
		this.setBackground(color);
	}
	
	public void setColor(Color c) {
		color = c;
		setBackground(color);
	}
	
	public void setNumber() {
		this.setText("" + numTroops);
	}
	
	public void decTroops() {
		numTroops--;
		setNumber();
	}
	
	public void incTroops() {
		numTroops++;
		setNumber();
	}
	
	public void setTroops(int troops) {
		numTroops = troops;
		setNumber();
	}
}
