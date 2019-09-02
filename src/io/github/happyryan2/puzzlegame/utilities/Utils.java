package io.github.happyryan2.puzzlegame.utilities;

import java.awt.Point;

public class Utils {
	public static double dist(int x1, int y1, int x2, int y2) {
		int dx = x1 - x2;
		int dy = y1 - y2;
		return Math.sqrt((dx * dx) + (dy * dy));
	}
	public static int distSq(int x1, int y1, int x2, int y2) {
		int dx = x1 - x2;
		int dy = y1 - y2;
		return (dx * dx) + (dy * dy);
	}
}
