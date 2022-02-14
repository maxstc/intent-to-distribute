package display;

public class Camera {
	private float x;
	private float y;
	private float zoom;
	
	public Camera() {
		this.x = 50;
		this.y = 50;
		this.zoom = 5;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getZoom() {
		return zoom;
	}
	
	public void update(float dx, float dy, float mouseScroll) {
		x+=dx;
		y+=dy;
		
//		if (x < 0) {
//			x = 0;
//		}
//		if (x > 150) {
//			x = 150;
//		}
//		if (y < 0) {
//			y = 0;
//		}
//		if (y > 173.21f) {
//			y = 173.21f;
//		}
		
		zoom+=(-1 * mouseScroll * (zoom / 10f));
		if (zoom < 1) {
			zoom = 1;
		}
		if (zoom > 100) {
			zoom = 100;
		}
		
	}
}
