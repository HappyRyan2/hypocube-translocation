package io.github.happyryan2.puzzlegame.utilities;

import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class ImageLoader {
	public static Image loadImage(String pathStr) {
		try {
			Image img = ImageIO.read(new File(pathStr));
			return img;
		}
		catch(Exception e) { }
		return null;
	}
}

// blue color for player: rgb(66, 139, 255)
