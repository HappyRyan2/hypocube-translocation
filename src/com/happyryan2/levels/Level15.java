package com.happyryan2.levels;

import com.happyryan2.game.Level;
import com.happyryan2.objects.*;

public class Level15 extends Level {
	public Level15() {
		super.id = 15;
		super.requireAll = true;
		super.requirements.add(7);
		super.requirements.add(13);
		super.x = 1000;
		super.y = -200;

		super.manualSize = true;
		super.width = 7;
		super.height = 4;

		/* Previously level 9 */
		super.content.add(new Retractor(0, 1, "right", true));
		super.content.add(new Retractor(0, 2, "right", true));
		super.content.add(new Retractor(1, 1, "down", false));
		super.content.add(new Player(5, 1));
		super.content.add(new Goal(6, 1));
		super.content.add(new Extender(5, 3, "none", false));
	}
}
