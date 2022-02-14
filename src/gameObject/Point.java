package gameObject;

public class Point {

	private float x;
	private float y;
	private int currentX;
	private int currentY;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
		currentX = (int) x;
		currentY = (int) y;
	}

	public void update(float cameraX, float cameraY, float cameraZoom, int halfWidth, int halfHeight) {
		currentX = (int) ((x - cameraX) * cameraZoom) + halfWidth;
		currentY = (int) ((y - cameraY) * cameraZoom) + halfHeight;
	}

	public int getX() {
		return currentX;
	}
	public int getY() {
		return currentY;
	}
	
	public float getRealX() {
		return x;
	}
	public float getRealY() {
		return y;
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

}
