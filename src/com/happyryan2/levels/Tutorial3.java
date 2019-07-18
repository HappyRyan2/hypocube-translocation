package com.happyryan2.levels;

import java.util.List;
import java.util.ArrayList;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Tutorial3 extends Level {
	List content = new ArrayList();
	public Tutorial3() {
		super.requirements.add(2);
		super.id = 3;
		super.x = 200;
		super.y = -200;

		super.content.add(new Extender(1, 0, "down", false));
		super.content.add(new Extender(2, 0, "left", false));
		super.content.add(new Retractor(2, 1, "down", false));
		super.content.add(new Retractor(2, 3, "left", false));
		super.content.add(new Goal(1, 2));
		super.content.add(new Player(0, 1));
	}
}
