package io.github.happyryan2.puzzlegame.utilities;

import java.awt.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ResourceLoader {
	public static Image loadImage(String pathStr) {
		try {
			Image img = ImageIO.read(new File(pathStr));
			return img;
		}
		catch(Exception e) { }
		return null;
	}
	public static Font loadFont(String pathStr) {
		try {
			File dir = new File(System.getProperty("user.dir"));
			File res = new File(dir.getPath() + "/res");
			Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(dir.getPath() + "\\" + pathStr)).deriveFont(40f);
	    	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(customFont);
			return customFont;
		}
		catch(Exception e) {
			System.out.println("Could not find font file.");
			return null;
		}
	}
}
