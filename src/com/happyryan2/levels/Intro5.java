package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Intro5 extends Level {
	public Intro5() {
		super.content.add(new Retractor(0, 1, "right", true));
		super.content.add(new Retractor(1, 1, "left", false));
		super.content.add(new Retractor(1, 0, "down", false));
		super.content.add(new Player(3, 2));
		super.content.add(new Goal(3, 1));
	}
}
