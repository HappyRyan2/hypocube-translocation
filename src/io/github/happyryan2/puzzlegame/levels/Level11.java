package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.game.Level;

public class Level11 extends Level {
	public Level11() {
		super.id = 11;
		super.x = 400;
		super.y = 200;
		super.requirements.add(9);

		super.content.add(new Retractor(1, 1, "down", false, true));
		super.content.add(new Retractor(2, 3, "up", false));
		super.content.add(new Retractor(0, 3, "right", false));
		super.content.add(new Player(2, 4));
		super.content.add(new Goal(1, 3));
		super.content.add(new Extender(0, 1, "right", false));
		super.content.add(new Wall(1, 4));
		super.content.add(new Wall(0, 2));
		super.content.add(new Wall(0, 0));
		super.content.add(new Wall(1, 0));
	}
}
