package display;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageHandler {

	private Map<String, Image> images;
	
	public ImageHandler(String path) {
		initialize(path);
	}
	
	public void initialize(String path) {
		File folder = new File(path);
		for (File entry : folder.listFiles()) {
			addImage(entry);
		}
	}
	
	public void addImage(File entry) {
		if (entry.getName().substring(entry.getName().length() - 4, entry.getName().length()).equals(".png")) {
			try {
				images.put(entry.getName(), ImageIO.read(entry));
			} catch (IOException e) {
				System.out.println("Failed to read a file: " + entry.getName());
				e.printStackTrace();
			}
		}
	}
	
}
