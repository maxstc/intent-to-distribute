package sprite;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Handles the loading and retrieving of sprites
 * @author Max St. Claire
 *
 */
public class SpriteHandler {

	Map<String, Integer> spriteIds;
	Image[] sprites;

	/**
	 * Constructs a new SpriteHandler and reads from ./res/ for sprites
	 */
	public SpriteHandler() {
		spriteIds = new HashMap<>();
		
		File resourceFolder = new File("./res/");
		File[] images = resourceFolder.listFiles();
		sprites = new Image[images.length + 1];
		sprites[0] = null;
		for (int i = 0; i < images.length; i++) {
			try {
				sprites[i + 1] = ImageIO.read(images[i]);
				spriteIds.put(images[i].getName(), i + 1);
			}
			catch (IOException e) {
				System.out.println("IOException on file: " + images[i]);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gets the sprite number corresponding to a specific sprite file name
	 * @param spriteName the file name of the sprite
	 * @return the index to be used in getSprite()
	 */
	public int getSpriteNum(String spriteName) {
		return spriteIds.get(spriteName);
	}
	
	/**
	 * Gets a sprite from its index from getSpriteNum(). Note: 0 will always return null and denotes a game object with no visual aspect.
	 * @param spriteIndex
	 * @return
	 */
	public Image getSprite(int spriteIndex) {
		return sprites[spriteIndex];
	}
}
