package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level22 extends Level  {
	public Level22() {
		super.id = 22;
		super.requirements.add(10);
		super.requirements.add(16);
		super.requireAll = true;
		super.x = 600;
		super.y = 600;

		super.content.add(new LongExtender(0, 2, "right", false));
		super.content.add(new Player(1, 2));
		super.content.add(new Goal(8, 2));
		super.content.add(new Retractor(4, 2, "right", false));
		super.content.add(new Retractor(6, 2, "left", false));
		super.content.add(new Retractor(5, 3, "up", true));
		super.content.add(new Wall(0, 0));
		super.content.add(new Wall(1, 0));
		super.content.add(new Wall(2, 0));
		super.content.add(new Wall(3, 0));
		super.content.add(new Wall(8, 0));
		super.content.add(new Wall(7, 0));
		super.content.add(new Wall(0, 1));
		super.content.add(new Wall(1, 1));
		super.content.add(new Wall(2, 1));
		super.content.add(new Wall(3, 1));
		super.content.add(new Wall(8, 1));
		super.content.add(new Wall(7, 1));
		super.content.add(new Wall(8, 3));
		super.content.add(new Wall(7, 3));
		super.content.add(new Wall(3, 3));
		super.content.add(new Wall(2, 3));
		super.content.add(new Wall(1, 3));
		super.content.add(new Wall(0, 3));
	}
}
