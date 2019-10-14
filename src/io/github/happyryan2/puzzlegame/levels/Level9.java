package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.game.Level;

public class Level9 extends Level {
	public Level9() {
		super.id = 9;
		super.x = 400;
		super.y = 0;
		super.requirements.add(8);

		super.content.add(new Retractor(1, 0, "down", false));
		super.content.add(new Extender(0, 3, "right", false));
		super.content.add(new Player(1, 2));
		super.content.add(new Goal(4, 3));
		super.content.add(new Wall(3, 0));
		super.content.add(new Wall(4, 0));
		super.content.add(new Wall(4, 1));
		super.content.add(new Wall(4, 2));
		super.content.add(new Wall(0, 0));
		super.content.add(new Retractor(2, 1, "left", false));
		super.content.add(new Retractor(0, 2, "down", false));
	}
}
