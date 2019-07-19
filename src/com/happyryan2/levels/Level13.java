package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Level13 extends Level {
	public Level13() {
		super.manualSize = true;
		super.width = 3;
		super.height = 5;
		super.content.add(new Retractor(0, 1, "right"));
		super.content.add(new Retractor(1, 3, "up"));
		super.content.add(new Retractor(1, 4, "up", true));
		super.content.add(new Player(1, 2));
		super.content.add(new Goal(1, 0));
	}
}
