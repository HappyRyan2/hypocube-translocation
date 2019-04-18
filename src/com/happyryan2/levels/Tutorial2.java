package com.happyryan2.levels;

import java.util.List;
import java.util.ArrayList;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Tutorial2 extends Level {
	List content = new ArrayList();
	public Tutorial2() {
		super.content.add(new Retractor(2, 1, "left"));
		super.content.add(new Player(0, 1));
		super.content.add(new Goal(1, 1));
	}
}
