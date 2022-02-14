package input;

import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import engine.Game;

public class Input implements KeyListener, MouseListener, MouseWheelListener {
    
	private Game game;
	
    private boolean[] pressed;
    private float mouseScrollAmount;

    public Input(Game game) {
        pressed = new boolean[255];
        this.game = game;
        mouseScrollAmount = 0.0f;
    }

    public boolean isPressed(int keyCode) {
        return pressed[keyCode];
    }
    @Override
	public void keyTyped(KeyEvent e) {
		game.press(e.getKeyChar());
	}
	@Override
	public void keyPressed(KeyEvent e) {
		pressed[e.getKeyCode()] = true;
	}
	@Override
	public void keyReleased(KeyEvent e) {
		pressed[e.getKeyCode()] = false;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {
		game.click(e.getX(), e.getY(), e.getButton() == e.BUTTON1);
	}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	
	public int getX() {
		return MouseInfo.getPointerInfo().getLocation().x;
	}
	public int getY() {
		return MouseInfo.getPointerInfo().getLocation().y;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseScrollAmount += e.getPreciseWheelRotation();
	}
	
	public float getMouseWheelRotation() {
		float output = mouseScrollAmount;
		mouseScrollAmount = 0;
		return output;
	}
}
