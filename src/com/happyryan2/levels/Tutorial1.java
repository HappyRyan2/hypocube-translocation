package com.happyryan2.levels;

import java.util.List;
import java.util.ArrayList;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Tutorial1 extends Level {
	List content = new ArrayList();
	public Tutorial1() {
		super.discovered = true;
		super.id = 1;
		super.x = 0;
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
