package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level19 extends Level  {
	public Level19() {
		super.id = 19;
		super.x = 600;
		super.y = 0;
		super.requirements.add(18);

		super.content.add(new Retractor(0, 1, "right", true));
		super.content.add(new Retractor(2, 1, "down", false));
		super.content.add(new Retractor(0, 3, "right", true));
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
