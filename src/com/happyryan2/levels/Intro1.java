package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Intro1 extends Level {
	public Intro1() {
		super.id = 6;
		super.requirements.add(5);
		super.x = 600;
		super.y = 0;

		super.manualSize = true;
		super.width = 4;
		super.height = 3;

		super.content.add(new Retractor(3, 2, "left"));
		super.content.add(new Retractor(2, 2, "up"));
		super.content.add(new Player(1, 0));
		super.content.add(new Goal(0, 1));
	}
}
