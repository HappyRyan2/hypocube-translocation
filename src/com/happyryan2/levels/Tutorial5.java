package com.happyryan2.levels;

import java.util.List;
import java.util.ArrayList;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Tutorial5 extends Level {
	List content = new ArrayList();
	public Tutorial5() {
		super.id = 5;
		super.requirements.add(4);
		super.x = 600;
		super.y = -200;

		super.content.add(new Retractor(1, 0, "down"));
		super.content.add(new Goal(1, 1));
		super.content.add(new Retractor(1, 2, "left"));
		super.content.add(new Player(1, 3));
		super.content.add(new Extender(1, 4, "down"));
	}
}
