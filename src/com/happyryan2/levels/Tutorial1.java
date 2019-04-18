package com.happyryan2.levels;

import java.util.List;
import java.util.ArrayList;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Tutorial1 extends Level {
	List content = new ArrayList();
	public Tutorial1() {
		super.infoText = "your task: move the blue to the red\nclick an extender to apply force (push) in that direction";
		super.content.add(new Extender(0, 1, "right"));
		super.content.add(new Player(1, 1));
		super.content.add(new Goal(2, 1));
	}
}
