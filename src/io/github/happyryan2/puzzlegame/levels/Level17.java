package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level17 extends Level {
	public Level17() {
		super.id = 17;
		super.requirements.add(16);
		super.x = 800;
		super.y = 400;

		super.content.add(new Extender(0, 1, "right", true));
		super.content.add(new Retractor(1, 1, "right", false));
		super.content.add(new Extender(3, 0, "down", false, true));
		super.content.add(new Player(2, 1));
		super.content.add(new Goal(4, 2));
	}
}
