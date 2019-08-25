package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.game.Level;

public class Level12 extends Level {
	public Level12() {
		super.id = 12;
		super.x = 400;
		super.y = 200;
		super.requirements.add(10);

		super.content.add(new Retractor(0, 0, "right", false));
		super.content.add(new Retractor(1, 0, "down", false, true));
		super.content.add(new Player(2, 3));
		super.content.add(new Goal(1, 2));
		super.content.add(new Retractor(0, 2, "right", false));
		super.content.add(new Wall(0, 1));
		super.content.add(new Wall(1, 3));
		super.content.add(new Retractor(2, 2, "up", false));
	}
}
