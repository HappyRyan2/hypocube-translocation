package io.github.happyryan2.puzzlegame.levels;

import java.util.List;
import java.util.ArrayList;

import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.game.Level;

public class Level1 extends Level {
	List content = new ArrayList();
	public Level1() {
		super.discovered = true;
		super.id = 1;
		super.x = 200;
		super.y = 0;

		super.manualSize = true;
		super.width = 3;
		super.height = 3;

		super.content.add(new Extender(0, 1, "right"));
		super.content.add(new Extender(2, 0, "down"));
		super.content.add(new Player(1, 1));
		super.content.add(new Goal(2, 2));
	}
}
