package gameObject;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Tile {
	
	TileData tileData;
	
	public float tileDataAlpha;
	public float tileDataBeta;
	
	private static final float SQRT_3 = (float) Math.sqrt(3.0);

	private int x;
	private int y;
	
	private float sideLength;
	
	private Point[] points;
	//   0 1
	//  5   2
	//   4 3
	private int[] xPoints;
	private int[] yPoints;
	
	private TileMap tileMap;
	
	private Color color;
	
	public Tile(int x, int y, float sideLength, TileMap tileMap) {
		
		tileDataAlpha = (float) Math.random();
		
		tileDataBeta = tileDataAlpha;
		
		tileData = new TileData();
		
		this.x = x;
		this.y = y;
		
		this.tileMap = tileMap;
		color = new Color((int) (tileDataAlpha * 255), (int) (tileDataAlpha * 255), (int) (tileDataAlpha * 255));

		this.sideLength = sideLength;
		
		points = new Point[6];
		xPoints = new int[6];
		yPoints = new int[6];
		
		initializePoints();
		
		//updatePoints();
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
	
	public void smooth() {
		tileDataBeta = 0;
		Set<Tile> n = getNeighbors();
		Set<Tile> neighbors = new HashSet<>();
		for (Tile t : n) {
			neighbors.addAll(t.getNeighbors());
		}
		int num = 0;
		for (Tile t : neighbors) {
			tileDataBeta += (t.tileDataAlpha);
			num++;
		}
		tileDataBeta /= num;
		tileDataBeta = tileDataAlpha * 0.3f + tileDataBeta * 0.7f;
		tileDataBeta = sigmoid(tileDataBeta);
	}
	
	private float sigmoid(float x) {
		float y = x/2 - 0.25f;
		return (float) (y/(2 * Math.sqrt(y * y + 0.01f)) + 0.5f);
	}
	
	public void reset() {
		tileDataAlpha = tileDataBeta;
//		if (tileDataAlpha <= 0.6) {
//			color = new Color(100, 125, 20);
//		}
//		else {
//			color = new Color(80, 180, 200);
//		}
		color = new Color((int) (tileDataAlpha * 255), (int) (tileDataAlpha * 255), (int) (tileDataAlpha * 255));
	}
	
	public Set<Tile> getNeighbors() {
		Set<Tile> neighbors = new HashSet<>();
		int mod = x % 2;
		addNeighbor(neighbors, x, y-1);
		addNeighbor(neighbors, x, y+1);
		addNeighbor(neighbors, x-1, y-1+mod);
		addNeighbor(neighbors, x-1, y+mod);
		addNeighbor(neighbors, x+1, y-1+mod);
		addNeighbor(neighbors, x+1, y+mod);
		
//		addNeighbor(neighbors, x-2, y);
//		addNeighbor(neighbors, x-2, y-1);
//		addNeighbor(neighbors, x-1, y-2+mod);
//		addNeighbor(neighbors, x, y-2);
//		addNeighbor(neighbors, x+1, y-2+mod);
//		addNeighbor(neighbors, x+2, y-1);
//		addNeighbor(neighbors, x+2, y);
//		addNeighbor(neighbors, x+2, y+1);
//		addNeighbor(neighbors, x+1, y+1+mod);
//		addNeighbor(neighbors, x, y+2);
//		addNeighbor(neighbors, x-1, y+1+mod);
//		addNeighbor(neighbors, x-2, y+1+mod);
		
		return neighbors;
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	private void addNeighbor(Set<Tile> set, int x, int y) {
		if (x >= 0 && x < tileMap.getTiles().length && y >= 0 && y < tileMap.getTiles()[0].length) {
			set.add(tileMap.getTiles()[x][y]);
		}
	}
	
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
	
	public Point copyPoint(int modX, int modY, int targetPointNumber) {
		return tileMap.getTiles()[x+modX][y+modY].getPoints()[targetPointNumber];
	}
	
	public void updatePoints(int halfWidth, int halfHeight, float cameraX, float cameraY, float cameraZoom) {
		for (int i = 0; i < xPoints.length; i++) {
			xPoints[i] = points[i].getX();
			yPoints[i] = points[i].getY();
		}
	}
	
	public void click(int x, int y) {
		
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
