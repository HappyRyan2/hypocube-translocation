package io.github.happyryan2.puzzlegame.levels;

import java.util.List;
import java.util.ArrayList;

import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.game.Level;

public class Level5 extends Level {
	List content = new ArrayList();
	public Level5() {
		super.id = 5;
		super.requirements.add(4);
		super.x = 600;
		super.y = -200;

		super.content.add(new Retractor(2, 3, "up", false));
		super.content.add(new Wall(0, 0));
		super.content.add(new Wall(0, 4));
		super.content.add(new Wall(3, 4));
		super.content.add(new Player(1, 2));
		super.content.add(new Extender(0, 2, "right", false));
		super.content.add(new Retractor(2, 4, "up", false));
		super.content.add(new Extender(3, 0, "left", false));
		super.content.add(new Wall(3, 1));
		super.content.add(new Goal(2, 4));
	}
}
