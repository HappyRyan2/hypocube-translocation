package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level15 extends Level {
	public Level15() {
		super.id = 15;
		super.requirements.add(14);
		super.x = 1200;
		super.y = -200;

		super.content.add(new Retractor(0, 1, "right", true));
		super.content.add(new Retractor(0, 2, "right", true));
		super.content.add(new Retractor(1, 1, "down", false));
		super.content.add(new Wall(0, 3));
		super.content.add(new Player(4, 1));
		super.content.add(new Wall(1, 3));
		super.content.add(new Wall(7, 3));
		super.content.add(new Goal(7, 1));
		super.content.add(new Wall(0, 0));
	}
}
