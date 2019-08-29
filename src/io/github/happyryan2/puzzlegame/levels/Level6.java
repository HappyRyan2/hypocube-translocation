package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.game.Level;

public class Level6 extends Level {
	public Level6() {
		super.id = 6;
		super.requirements.add(4);
		super.x = 800;
		super.y = -200;

		super.content.add(new Wall(1, 3));
		super.content.add(new Wall(2, 1));
		super.content.add(new Wall(3, 3));
		super.content.add(new Wall(4, 1));
		super.content.add(new Wall(5, 3));
		super.content.add(new Wall(6, 1));
		super.content.add(new Player(7, 0));
		super.content.add(new Goal(7, 1));
		super.content.add(new Retractor(2, 4, "up", false));
		super.content.add(new Retractor(2, 2, "right", false));
		super.content.add(new Wall(0, 4));
		super.content.add(new Wall(1, 4));
		super.content.add(new Wall(3, 4));
		super.content.add(new Wall(4, 4));
		super.content.add(new Wall(5, 4));
		super.content.add(new Wall(6, 4));
		super.content.add(new Wall(7, 4));
		super.content.add(new Wall(7, 3));
		super.content.add(new Wall(0, 0));
		super.content.add(new Wall(1, 0));
		super.content.add(new Wall(2, 0));
		super.content.add(new Extender(4, 3, "down", false));
		super.content.add(new Wall(4, 0));
	}
}
