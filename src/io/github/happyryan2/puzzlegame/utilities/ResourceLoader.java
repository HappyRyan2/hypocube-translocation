package io.github.happyryan2.puzzlegame.utilities;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class ResourceLoader {
	public static Image loadImage(String path) {
		try {
			InputStream stream = loadResource(path);
			Image image = ImageIO.read(stream);
			return image;
		}
		catch(Exception e) {
			System.out.println("Error while loading image:");
			e.printStackTrace();
		}
		return null;
	}
	public static Font loadFont(String path) {
		try {
			InputStream stream = loadResource(path);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
			ge.registerFont(font);
			return font;
		}
		catch(Exception e) {
			System.out.println("Error while loading font:");
			e.printStackTrace();
		}
		return null;
	}
	public static InputStream loadResource(String path) {
		try {
			path = path.replace(File.separatorChar, '/');
			InputStream stream = ResourceLoader.class.getClassLoader().getResourceAsStream(path);
			return stream;
		}
		catch(Exception e) {
			System.out.println("Error while loading resource:");
			e.printStackTrace();
			return null;
		}
	}
}
