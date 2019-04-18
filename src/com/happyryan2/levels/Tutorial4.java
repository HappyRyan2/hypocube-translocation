package com.happyryan2.levels;

import java.util.List;
import java.util.ArrayList;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Tutorial4 extends Level {
	List content = new ArrayList();
	public Tutorial4() {
		// super.content.add(new Retractor(0, 0, "down"));
		// super.content.add(new Retractor(0, 1, "down"));
		// super.content.add(new Extender(1, 0, "down"));
		// super.content.add(new Extender(3, 1, "left"));
		// super.content.add(new Extender(2, 1, "none"));
		// super.content.add(new Extender(1, 1, "none"));
		// super.content.add(new Extender(0, 3, "left"));
		// super.content.add(new Retractor(1, 4, "left"));
		// super.content.add(new Player(3, 2));
		// super.content.add(new Goal(4, 2));
		super.content.add(new Retractor(1, 0, "down"));
		super.content.add(new Extender(0, 1, "left"));
		super.content.add(new Retractor(1, 2, "left"));
		super.content.add(new Player(1, 3));
		super.content.add(new Extender(1, 4, "up"));
		super.content.add(new Goal(2, 1));
	}
}
