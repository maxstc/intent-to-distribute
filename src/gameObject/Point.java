package gameObject;

/**
 * This class contains data on a point that can update given a camera's position and zoom
 * 
 * It is often referenced by multiple tiles to avoid having equivalent points updated twice (i.e. when hexagons' vertices overlap)
 */
public class Point {

	//absolute position in relation to other points
	private float x;
	private float y;
	
	//the position this point would be drawn on the screen if it were visible
	private int currentX;
	private int currentY;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
		currentX = (int) x;
		currentY = (int) y;
	}

	/**
	 * Update the point's position
	 * @param cameraX
	 * @param cameraY
	 * @param cameraZoom
	 * @param halfWidth
	 * @param halfHeight
	 */
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
