package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Level14 extends Level {
	public Level14() {
		super.manualSize = true;
		super.width = 4;
		super.height = 3;
		super.content.add(new Player(1, 1));
		super.content.add(new Goal(1, 0));
		super.content.add(new Retractor(1, 0, "right"));
		super.content.add(new Retractor(2, 0, "left"));
		super.content.add(new Extender(0, 2, "right"));
		super.content.add(new Extender(3, 2, "left"));
		super.content.add(new Retractor(1, 2, "up"));
	}
}
