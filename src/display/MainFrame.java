package display;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

import engine.Game;

/**
 * This is the main JFrame that contains all the graphics
 */
public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = -7432036344419817493L;

	private Display display;
	
	public MainFrame(int width, int height, Game game) {
		
		setTitle("itd");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
		 
       	display = new Display(game);
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
	
}
