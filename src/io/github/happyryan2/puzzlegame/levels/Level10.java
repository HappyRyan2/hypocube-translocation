package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.game.Level;

public class Level10 extends Level {
	public Level10() {
		super.x = 400;
		super.y = 600;
		super.requirements.add(7);
		super.id = 10;

		super.content.add(new Wall(4, 0));
		super.content.add(new Wall(2, 0));
		super.content.add(new Wall(0, 3));
		super.content.add(new Wall(5, 2));
		super.content.add(new Wall(5, 3));
		super.content.add(new Player(2, 2));
		super.content.add(new Goal(2, 3));
		super.content.add(new Extender(5, 0, "down", false));
		super.content.add(new Extender(3, 0, "down", false, true));
		super.content.add(new LongExtender(0, 1, "right", false));
	}
}
