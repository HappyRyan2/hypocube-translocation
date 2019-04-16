/*
This file creates the screen that the game is played on, and is also the application entry point.
*/

package com.happyryan2.utilities;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Timer;
import java.lang.Math;

import com.happyryan2.game.*;
// import com.happyryan2.levels.*;

public class Screen extends JPanel {
	public static JFrame frame = new JFrame("HypoCube Translocation - version 0.1");
	public static JPanel canvas = new Screen();
	public static int screenW = 0;
	public static int screenH = 0;
	public static String cursor = "default";

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
    }

	public void paint(Graphics g) {
		//clear background
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, 800, 800);
		//draw game graphics
		Game.display(g);
	}

	public static void resetTransform() {

	}

}
