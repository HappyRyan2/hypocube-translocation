package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level20 extends Level  {
	public Level20() {
		super.id = 20;
		super.x = 1000;
		super.y = 600;
		super.requirements.add(19);

		super.content.add(new LongExtender(0, 2, "right", true));
		super.content.add(new Wall(6, 4));
		super.content.add(new Wall(5, 4));
		super.content.add(new Wall(5, 3));
		super.content.add(new Wall(6, 3));
		super.content.add(new Wall(4, 0));
		super.content.add(new Wall(5, 0));
		super.content.add(new Wall(6, 0));
		super.content.add(new Player(0, 3));
		super.content.add(new Goal(0, 4));
		super.content.add(new Wall(4, 3));
		super.content.add(new Wall(4, 4));
		super.content.add(new Wall(7, 0));
		super.content.add(new Wall(7, 2));
		super.content.add(new Wall(7, 3));
		super.content.add(new Wall(7, 4));
		super.content.add(new Retractor(2, 0, "left", false));
		super.content.add(new Wall(3, 0));
		super.content.add(new Retractor(1, 1, "up", false));
	}
}
