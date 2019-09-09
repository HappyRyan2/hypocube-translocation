package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level14 extends Level {
	public Level14() {
		super.id = 14;
		super.requireAll = true;
		super.requirements.add(6);
		super.requirements.add(12);
		super.x = 1000;
		super.y = -200;

		super.content.add(new Retractor(0, 1, "right", true));
		super.content.add(new Retractor(0, 2, "right", true));
		super.content.add(new Retractor(1, 1, "down", false));
		super.content.add(new Wall(0, 0));
		super.content.add(new Wall(1, 0));
		super.content.add(new Wall(2, 0));
		super.content.add(new Wall(3, 0));
		super.content.add(new Wall(4, 3));
		super.content.add(new Wall(6, 2));
		super.content.add(new Player(6, 1));
		super.content.add(new Goal(7, 1));
	}
}
