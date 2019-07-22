package com.happyryan2.levels;

import com.happyryan2.game.Level;
import com.happyryan2.objects.*;

public class Level9 extends Level {
	public Level9() {
		super.id = 9;
		super.requireAll = true;
		super.requirements.add(7);
		super.x = 1000;
		super.y = -200;

		super.manualSize = true;
		super.width = 7;
		super.height = 4;

		super.content.add(new Retractor(0, 1, "right", true));
		super.content.add(new Retractor(0, 2, "right", true));
		super.content.add(new Retractor(1, 1, "down", false));
		super.content.add(new Player(5, 1));
		super.content.add(new Goal(6, 1));
		super.content.add(new Extender(5, 3, "none", false));

	}
}
