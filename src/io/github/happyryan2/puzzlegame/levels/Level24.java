package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level24 extends Level {
	public Level24() {
		super.id = 24;
		super.requirements.add(22);
		super.x = 800;
		super.y = 600;

		super.content.add(new Wall(4, 5));
		super.content.add(new Wall(5, 5));
		super.content.add(new Wall(5, 4));
		super.content.add(new Player(4, 1));
		super.content.add(new Goal(6, 1));
		super.content.add(new Wall(6, 3));
		super.content.add(new Wall(6, 4));
		super.content.add(new Wall(6, 5));
		super.content.add(new Wall(6, 6));
		super.content.add(new Wall(5, 6));
		super.content.add(new Wall(4, 6));
		super.content.add(new Wall(3, 6));
		super.content.add(new Wall(2, 6));
		super.content.add(new Wall(2, 7));
		super.content.add(new Wall(3, 7));
		super.content.add(new Wall(4, 7));
		super.content.add(new Wall(5, 7));
		super.content.add(new Wall(6, 7));
		super.content.add(new Retractor(1, 4, "down", false));
		super.content.add(new Retractor(1, 7, "up", false));
		super.content.add(new LongExtender(0, 5, "right", true));
	}
}
