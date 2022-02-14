package display;

import javax.swing.JFrame;

import engine.Game;

public class MainFrame extends JFrame {
	
	private Display display;
	
	public MainFrame(int width, int height, Game game) {
		
		setTitle("itd");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
		
       	display = new Display(width, height, game);
		add(display);
		pack();
		display.createBuffer();
		
		setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}
	
	public Display getDisplay() {
		return display;
	}
	
	public void giveStats(int fps, int ups) {
		display.giveStats(fps, ups);
	}
	
}
