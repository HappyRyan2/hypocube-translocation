package com.happyryan2.utilities;

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
		catch(Exception e) {
			System.out.println("Image file not found.");
			e.printStackTrace();
		}
		return null;
	}
}

// blue color for player: rgb(66, 139, 255)
