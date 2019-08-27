package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level14 extends Level {
	public Level14() {
		super.id = 14;
		super.requireAll = true;
		super.requirements.add(6);
		super.requirements.add(12);
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
