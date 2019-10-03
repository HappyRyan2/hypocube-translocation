package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.game.Level;

public class Level12 extends Level {
	public Level12() {
		super.id = 12;
		super.x = 200;
		super.y = 600;
		super.requirements.add(10);

		super.content.add(new Extender(5, 1, "right", false));
		super.content.add(new Player(5, 2));
		super.content.add(new Goal(6, 2));
		super.content.add(new Wall(8, 2));
		super.content.add(new Wall(7, 2));
		super.content.add(new Wall(0, 2));
		super.content.add(new Wall(1, 2));
		super.content.add(new LongExtender(0, 1, "right", false));
		super.content.add(new LongExtender(8, 1, "left", false));
		super.content.add(new Extender(4, 0, "down", false));
	}
}
