package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level22 extends Level  {
	public Level22() {
		super.id = 22;
		super.requirements.add(10);
		super.x = 1200;
		super.y = 200;

		super.content.add(new Extender(0, 1, "right", true));
		super.content.add(new Retractor(1, 1, "right", false));
		super.content.add(new Extender(3, 0, "down", false, true));
		super.content.add(new Player(2, 1));
		super.content.add(new Goal(4, 2));

		// super.content.add(new Retractor(0, 1, "right", true));
		// super.content.add(new Retractor(1, 1, "right", false));
		// super.content.add(new Retractor(3, 0, "down", false));
		// super.content.add(new Player(2, 1));
		// super.content.add(new Goal(4, 2));
	}
}
