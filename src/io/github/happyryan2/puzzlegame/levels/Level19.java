package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level19 extends Level  {
	public Level19() {
		super.id = 19;
		super.x = 1000;
		super.y = 400;
		super.requireAll = true;
		super.requirements.add(18);
		super.requirements.add(10);

		super.content.add(new LongExtender(0, 1, "right", true));
		super.content.add(new Retractor(6, 1, "left", true));
		super.content.add(new Retractor(7, 1, "left", true));
		super.content.add(new Retractor(6, 0, "down", false));
		super.content.add(new Goal(0, 2));
		super.content.add(new Player(3, 2));
	}
}
