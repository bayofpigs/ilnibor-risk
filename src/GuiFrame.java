import javax.swing.JFrame;
public class GuiFrame extends JFrame {
	public GuiFrame() {
		add(new MainMenuPanel());
		setTitle("iLNibor's Risk");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(900, 700);
		setLocationRelativeTo(null);
		setResizable(false);
	}
	
	public static void main(String[] args) {
		GuiFrame g = new GuiFrame();
		g.setVisible(true);
	}
}
