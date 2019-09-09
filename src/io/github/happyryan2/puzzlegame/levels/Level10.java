package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.game.Level;

public class Level10 extends Level {
	public Level10() {
		super.x = 1000;
		super.y = 200;
		super.requirements.add(8);
		super.id = 10;

		super.manualSize = true;
		super.width = 7;
		super.height = 1;

		super.content.add(new Extender(0, 0, "right", true));
		super.content.add(new Extender(1, 0, "right", true));
		super.content.add(new Extender(2, 0, "right", true));
		super.content.add(new Player(3, 0));
		super.content.add(new Goal(6, 0));
	}
}
