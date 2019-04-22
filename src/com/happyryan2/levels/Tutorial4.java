package com.happyryan2.levels;

import com.happyryan2.game.Level;
import com.happyryan2.objects.*;

public class Tutorial4 extends Level {
	public Tutorial4() {
		super.infoTextTop = "you can push multiple objects with one extender";
		super.infoTextBottom = "(and it doesn't matter if they are extended or not)";
		super.content.add(new Retractor(3, 2, "left"));
		super.content.add(new Retractor(2, 2, "up"));
		super.content.add(new Player(1, 1));
		super.content.add(new Goal(0, 1));
	}
}
