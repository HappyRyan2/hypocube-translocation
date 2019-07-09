package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Intro1 extends Level {
	public Intro1() {
		super.manualSize = true;
		super.width = 3;
		super.height = 3;
		super.content.add(new Player(1, 1));
		super.content.add(new Goal(2, 1));
		super.content.add(new Retractor(0, 2, "right"));
		super.content.add(new Retractor(1, 2, "up"));
		super.content.add(new Retractor(0, 0, "right"));
	}
}
