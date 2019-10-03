package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level16 extends Level {
	public Level16() {
		super.id = 16;
		super.requirements.add(7);
		super.x = 600;
		super.y = 400;

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
