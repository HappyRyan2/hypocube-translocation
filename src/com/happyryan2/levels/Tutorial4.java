package com.happyryan2.levels;

import com.happyryan2.game.Level;
import com.happyryan2.objects.*;

public class Tutorial4 extends Level {
	public Tutorial4() {
		super.manualSize = true;
		super.width = 3;
		super.height = 4;
		super.content.add(new Retractor(0, 0, "down"));
		super.content.add(new Retractor(0, 2, "right"));
		super.content.add(new Retractor(1, 3, "up", true));
		super.content.add(new Player(1, 2));
		super.content.add(new Goal(1, 1));
		super.content.add(new Extender(1, 1, "none"));
	}
}
