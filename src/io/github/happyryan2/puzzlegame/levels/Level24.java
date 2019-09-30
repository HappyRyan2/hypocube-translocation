package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level24 extends Level {
	public Level24() {
		super.id = 24;
		super.requirements.add(10);
		super.x = 400;
		super.y = 600;

		super.content.add(new Retractor(3, 1, "right", false));
		super.content.add(new Retractor(4, 1, "down", false));
		super.content.add(new LongExtender(2, 2, "right", false));
		super.content.add(new LongExtender(4, 2, "left", false));
		super.content.add(new Player(1, 3));
		super.content.add(new Goal(0, 3));
		super.content.add(new Wall(4, 3));
		super.content.add(new Wall(5, 3));
		super.content.add(new Wall(6, 3));
		super.content.add(new Wall(1, 2));
		super.content.add(new Wall(0, 2));
		super.content.add(new Wall(0, 0));
		super.content.add(new Wall(6, 0));
	}
}
