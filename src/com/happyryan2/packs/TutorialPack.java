package com.happyryan2.packs;

import java.util.List;
import java.util.ArrayList;

import com.happyryan2.game.LevelPack;
import com.happyryan2.levels.*;

public class TutorialPack extends LevelPack {
	public TutorialPack() {
		this.levels = new ArrayList();
		this.levels.add(new Tutorial1());
		this.levels.add(new Tutorial2());
		this.levels.add(new Tutorial3());
		this.levels.add(new Tutorial4());
		this.levels.add(new Tutorial5());
	}
}
