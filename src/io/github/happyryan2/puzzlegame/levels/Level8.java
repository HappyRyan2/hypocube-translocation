package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level8 extends Level {
	public Level8() {
		super.id = 8;
		super.requireAll = true;
		super.requirements.add(7);
		super.x = 800;
		super.y = 200;

		super.content.add(new Retractor(0, 1, "right", false));
		super.content.add(new Extender(1, 1, "left", false));
		super.content.add(new Retractor(1, 0, "down", false));
		super.content.add(new Player(3, 2));
		super.content.add(new Goal(3, 1));
	}
}
