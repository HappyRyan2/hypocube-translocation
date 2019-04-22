/*
This file creates the screen that the game is played on, and is also the application entry point.
*/

package com.happyryan2.utilities;

import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
// import java.awt.Color;
// import java.awt.Cursor;
// import java.awt.Font;
import java.awt.*;
import java.awt.image.BufferedImage;
// import javax.swing.JFrame;
// import javax.swing.JPanel;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.util.Timer;
import java.lang.Math;
import java.io.File;
import java.net.URL;

import com.happyryan2.game.*;
// import com.happyryan2.levels.*;

public class Screen extends JPanel {
	public static JFrame frame = new JFrame("HypoCube Translocation - version 0.1");
	public static JPanel canvas = new Screen();
	public static int screenW = 0;
	public static int screenH = 0;
	public static String cursor = "default";
	public static Font fontRighteous;

    public static void main(String[] args) {
		//create JFrame + canvas for graphics
		canvas.setDoubleBuffered(true);
        canvas.setSize(400, 400);
        frame.add(canvas);
        frame.pack();
		frame.setSize(800, 800);
		frame.setResizable(false);
        frame.setVisible(true);
		canvas.setFocusable(true);

		//schedule framerate interval function
		Delay delay = new Delay();
		Timer timer = new Timer(true); // true = asynchronous
		timer.scheduleAtFixedRate(delay, 0, 1000 / 60);

		//listen for user inputs
		canvas.addKeyListener(new KeyInputs());
		canvas.addMouseListener(new MouseClick());

		//load resources
		try {
			File dir;
			if(System.getProperty("user.dir").contains("src")) {
				// we are in the source directory, this is an experimental version being run by the dev
				System.out.println("in the directory: 'src'");
				dir = new File(new File(System.getProperty("user.dir")).getParent());
			}
			else {
				dir = new File(System.getProperty("user.dir"));
			}
			File res = new File(dir.getPath() + "/res");
			Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(res.getPath() + "/fonts/righteous.ttf")).deriveFont(40f);
	    	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(customFont);
			fontRighteous = customFont;
		}
		catch (Exception e) {
			// System.out.println("resource loading failed...");
			e.printStackTrace();
		}
		try {
			File dir;
			if(System.getProperty("user.dir").contains("src")) {
				System.out.println("in the directory: 'src' (for the app icon)");
				dir = new File(new File(System.getProperty("user.dir")).getParent());
			}
			else {
				dir = new File(System.getProperty("user.dir"));
			}
			System.out.println(dir.getPath() + "\\res\\graphics\\icon.png");
			// URL url = ClassLoader.getSystemResource(dir.getPath() + "res/graphics/icon.png");
			URL url = frame.getClass().getResource(dir.getPath() + "\\res\\graphics\\icon.png");
			BufferedImage image = ImageIO.read(url);
			frame.setIconImage(image);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
    }

	public void paint(Graphics g) {
		//clear background
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, 800, 800);
		//draw game graphics
		Game.display(g);
	}

	public static void centerText(Graphics g, float x, float y, String text) {
		int width = g.getFontMetrics().stringWidth(text);
		g.drawString(text, (int) x - (width / 2), (int) y);
	}

	public static void resetTransform() {

	}

}
