package com.happyryan2.levels;

import com.happyryan2.game.Level;
import com.happyryan2.objects.*;

public class Tutorial4 extends Level {
	public Tutorial4() {
		super.manualSize = true;
		super.width = 4;
		super.height = 4;
		super.content.add(new Goal(2, 0));
		super.content.add(new Extender(2, 2, "none", false));
		super.content.add(new Extender(2, 3, "up", true));
		super.content.add(new Extender(1, 3, "up", false));
		super.content.add(new Extender(0, 3, "right", false));
		super.content.add(new Player(2, 1));
	}
}
