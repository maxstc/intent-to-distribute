package gameObject;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.TileData;

/**
 * This class contains geometric/visual data about each tile/hexagon
 */
public class Tile {
	
	private TileData tileData; //used for a tile's non visual data
	
	private static final float SQRT_3 = (float) Math.sqrt(3.0);

	private int x;
	private int y;
	
	private float sideLength;
	
	private Point[] points; //References to the {@code Point}s that are this hexagon's vertices
	//   0 1
	//  5   2
	//   4 3
	
	//the on-screen coordinates of each point
	//these are stored this way because of the parameters used by Graphics.fillPolygon()
	private int[] xPoints;
	private int[] yPoints;
	
	private TileMap tileMap;
	
	private Color color;
	
	public Tile(int x, int y, float sideLength, TileMap tileMap) {
		tileData = new TileData(this);
		
		this.x = x;
		this.y = y;
		
		this.tileMap = tileMap;
		color = Color.BLACK;

		this.sideLength = sideLength;
		
		points = new Point[6];
		xPoints = new int[6];
		yPoints = new int[6];
		
		initializePoints();
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public TileData getTileData() {
		return tileData;
	}
	
	public void resetColor(int mapMode) {
		switch (mapMode) {
		case 0:
			color = rain(tileData.getHeight());
			break;
		case 1:
			color = rain(tileData.getTemp());
			break;
		case 2:
			if (tileData.getIsLand()) {
				color = Color.GREEN;
			}
			else {
				color = Color.BLUE;
			}
			break;
		case 3:
			color = rain(tileData.getPop());
			break;
		case 4:
			color = rain(tileData.getCiv());
			break;
		}
	}
	
	/**
	 * Converts a number from 0 to 1 to a color.
	 * Vaguely follows the color spectrum, with 0 being red, and 1 being purple
	 */
	public Color rain(float num) {
		//num *= 1.2f; for full rainbow
		//red
		int red, green, blue;
		if (num <= 0.2f) {
			red = 255;
		}
		else if (num < 0.4f) {
			red = scale(num, 0.4f, 0.2f);
		}
		else if (num <= 0.8f) {
			red = 0;
		}
		else if (num < 1f) {
			red = scale(num, 0.8f, 1.0f);
		}
		else {
			red = 255;
		}
		
		//green
		if (num < 0.2) {
			green = scale(num, 0f, 0.2f);
		}
		else if (num <= 0.6f) {
			green = 255;
		}
		else if (num < 0.8f) {
			green = scale(num, 0.8f, 0.6f);
		}
		else {
			green = 0;
		}
		
		//blue
		if (num <= 0.4) {
			blue = 0;
		}
		else if (num < 0.6f) {
			blue = scale(num, 0.4f, 0.6f);
		}
		else if (num < 1f) {
			blue = 255;
		}
		else {
			blue = scale(num, 1.2f, 1f);
		}
		
		try {
			return new Color(red, green, blue);
		}
		catch (Exception e) {
			System.out.println("Couldn't set color: " + red + ", " + blue + ", " + green);
			System.out.println(num);
		}
		return Color.white;
	}
	
	/**
	 * Scales a number from 0 to 255 depending on its distance from lowBound to upperBound
	 * 
	 * Used for rain()
	 * @param num
	 * @param lowBound
	 * @param upperBound
	 * @return
	 */
	public int scale(float num, float lowBound, float upperBound) {
		return (int) (255 * (num - lowBound) / (upperBound - lowBound));
	}
	
	/**
	 * Returns a set containing all tiles bordering this one
	 * @return
	 */
	public Set<Tile> getNeighbors() {
		Set<Tile> neighbors = new HashSet<>();
		int mod = x % 2;
		addNeighbor(neighbors, x, y-1);
		addNeighbor(neighbors, x, y+1);
		addNeighbor(neighbors, x-1, y-1+mod);
		addNeighbor(neighbors, x-1, y+mod);
		addNeighbor(neighbors, x+1, y-1+mod);
		addNeighbor(neighbors, x+1, y+mod);
		
		return neighbors;
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	/**
	 * Adds an individual neighbor, if it exists
	 * @param set The set the neighbor will be added to
	 * @param x The neighbor's hexagon-coordinate x value
	 * @param y The neighbor's hexagon-coordinate y value
	 */
	private void addNeighbor(Set<Tile> set, int x, int y) {
		if (x >= 0 && x < tileMap.getTiles().length && y >= 0 && y < tileMap.getTiles()[0].length) {
			set.add(tileMap.getTiles()[x][y]);
		}
	}
	
	/**
	 * Initializes the points of this hexagon, making references to other points when possible
	 */
	private void initializePoints() {
		
		if (x == 0) {
			if (y == 0) {
				points[0] = createPoint(0);
				points[1] = createPoint(1);
			}
			else {
				points[0] = copyPoint(0, -1, 4);
				points[1] = copyPoint(0, -1, 3);
			}
			points[2] = createPoint(2);
			points[3] = createPoint(3);
			points[4] = createPoint(4);
			points[5] = createPoint(5);
		}
		else {
			if (x % 2 == 1) {
				points[0] = copyPoint(-1, 0, 2);
			}
			else if (y == 0) {
				points[0] = createPoint(0);
			}
			else {
				points[0] = copyPoint(0, -1, 4);
			}
			if (y == 0) {
				points[1] = createPoint(1);
			}
			else {
				points[1] = copyPoint(0, -1, 3);
			}
			points[2] = createPoint(2);
			points[3] = createPoint(3);
			if (x % 2 == 1) {
				if (tileMap.getTiles()[0].length == y + 1) {
					points[4] = createPoint(4);
				}
				else {
					points[4] = copyPoint(-1, 1, 2);
				}
				points[5] = copyPoint(-1, 0, 3);
			}
			else {
				points[4] = copyPoint(-1, 0, 2);
				points[5] = copyPoint(-1, 0, 1);
			}
		}
		
	}
	
	/**
	 * Creates a new point
	 * @param pointNumber
	 * @return
	 */
	public Point createPoint(int pointNumber) {
		float halfSideLength = sideLength / 2;
		float halfSideLengthSqrt3 = halfSideLength * SQRT_3;
		
		float xCenter = x * 3 * halfSideLength + 1;
		float yCenter = (y * 2 * SQRT_3 * halfSideLength) + (SQRT_3 / 2);
		if (x % 2 == 1) {
			yCenter += SQRT_3 * halfSideLength;
		}
		
		switch(pointNumber) {
			case 0:
				return tileMap.createNewPoint(xCenter - halfSideLength, yCenter - halfSideLengthSqrt3);
			case 1:
				return tileMap.createNewPoint(xCenter + halfSideLength, yCenter - halfSideLengthSqrt3);
			case 2:
				return tileMap.createNewPoint(xCenter + sideLength, yCenter);
			case 3:
				return tileMap.createNewPoint(xCenter + halfSideLength, yCenter + halfSideLengthSqrt3);
			case 4:
				return tileMap.createNewPoint(xCenter - halfSideLength, yCenter + halfSideLengthSqrt3);
			case 5:
				return tileMap.createNewPoint(xCenter - sideLength, yCenter);
			default:
				System.out.println("Out of bounds point number");
				return null;
		}
	}
	
	/**
	 * Copies an existing point from the {@code tileMap}
	 * @param modX
	 * @param modY
	 * @param targetPointNumber
	 * @return
	 */
	public Point copyPoint(int modX, int modY, int targetPointNumber) {
		return tileMap.getTiles()[x+modX][y+modY].getPoints()[targetPointNumber];
	}
	
	/**
	 * Updates {@code xPoints} and {@code yPoints} to what they should be
	 * @param halfWidth
	 * @param halfHeight
	 * @param cameraX
	 * @param cameraY
	 * @param cameraZoom
	 */
	public void updatePoints() {
		for (int i = 0; i < xPoints.length; i++) {
			xPoints[i] = points[i].getX();
			yPoints[i] = points[i].getY();
		}
	}
	
	/**
	 * Called when this tile is clicked
	 * 
	 * Not used yet
	 */
	public void click() {
		System.out.println(tileData.getHeight());
	}
	
	public int[] getXPoints() {
		return xPoints;
	}
	
	public int[] getYPoints() {
		return yPoints;
	}
	
	public Color getColor() {
		return color;
	}
	
	public Point[] getPoints() {
		return points;
	}
	
	/**
	 * Returns {@code true} if this tile is within the specified bounds, {@code false} otherwise
	 * @param minX
	 * @param maxX
	 * @param minY
	 * @param maxY
	 * @return
	 */
	public boolean isVisible(float minX, float maxX, float minY, float maxY) {
		if (points[2].getRealX() < minX) {
			return false;
		}
		if (points[5].getRealX() > maxX) {
			return false;
		}
		if (points[0].getRealY() > maxY) {
			return false;
		}
		if (points[3].getRealY() < minY) {
			return false;
		}
		return true;
	}
	
	public String toString() {
		String output = "(" + x + ", " + y + ")";
		return output;
	}

}
