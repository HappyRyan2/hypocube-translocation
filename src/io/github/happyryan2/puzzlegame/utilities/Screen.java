/*
This file creates the screen that the game is played on, and is also the application entry point.
*/

package io.github.happyryan2.puzzlegame.utilities;

import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.io.File;
import java.net.URL;

import io.github.happyryan2.puzzlegame.game.*;

public class Screen extends JPanel {
	public static JFrame frame = new JFrame("Hypocube Translocation - version 1.0");
	public static JPanel canvas = new Screen();
	public static int screenW = 0;
	public static int screenH = 0;
	public static String cursor = "default";
	public static Font fontRighteous = ResourceLoader.loadFont("res" + File.separator + "fonts" + File.separator + "righteous.ttf");
	public static Font fontOxygen = ResourceLoader.loadFont("res" + File.separator + "fonts" + File.separator + "oxygen.ttf");
	public static int frameCount = 0;
	public static boolean loading = true;

    public static void main(String[] args) {
		/* create JFrame + canvas */
		canvas.setDoubleBuffered(true);
        canvas.setSize(400, 400);
        frame.add(canvas);
        frame.pack();
		frame.setSize(800, 800);
		frame.setResizable(true);
        frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas.setFocusable(true);

		/* listen for input */
		canvas.addKeyListener(new KeyInputs());
		canvas.addMouseListener(new MouseClick());

		/* load resources */
		try {
			URL url = ClassLoader.getSystemResource("res/graphics/icon.png");
			Toolkit kit = Toolkit.getDefaultToolkit();
			Image img = kit.createImage(url);
			frame.setIconImage(img);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		/* schedule framerate interval function */
		Delay delay = new Delay();
		Timer timer = new Timer(true); // true = asynchronous
		timer.scheduleAtFixedRate(delay, 0, 1000 / 60);
    }

	public static int width() {
		return frame.getSize().width;
	}
	public static int height() {
		return frame.getSize().height;
	}

	public void paint(Graphics g) {
		//clear background
		g.setColor(new Color(200, 200, 200));
		g.fillRect(0, 0, screenW, screenH);
		if(loading) {
			g.setFont(new Font("Monospace", Font.PLAIN, 30));
			g.setColor(new Color(150, 150, 150));
			centerText(g, 400, 400, "loading...");
		}
		//draw game graphics
		Game.display(g);
		loading = false;
		//display warning for small screen sizes
		if(Screen.screenW < 600 || Screen.screenH < 600) {
			g.setColor(new Color(200, 200, 200));
			g.fillRect(0, 0, screenW, screenH);
			g.setColor(new Color(59, 67, 70));
			g.setFont(fontOxygen.deriveFont(30f));
			centerText(g, Screen.screenW / 2, Screen.screenH / 2, "The screen is too small");
		}
	}

	public static void centerText(Graphics g, float x, float y, String text) {
		int width = g.getFontMetrics().stringWidth(text);
		// int height = g.getFontMetrics().getHeight();
		g.drawString(text, (int) x - (width / 2), (int) y /* + (height / 2) */);
	}
	public static void scaleImage(Graphics g, Image img, int w, int h) {
		/*
		Scales 'img' to fit in rectangle w/ dimensions ('w', 'h'). Assumes the rectangles are similar.
		*/
		Graphics2D g2 = (Graphics2D) g;
		float ratioX = ((float) w) / img.getWidth(null);
		float ratioY = ((float) h) / img.getHeight(null);
		g2.scale(ratioX, ratioY);
		g2.drawImage(img, 0, 0, null);
		g2.scale(1 / ratioX, 1 / ratioY);
	}
}
