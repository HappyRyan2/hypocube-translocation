package com.happyryan2.packs;

import java.util.List;
import java.util.ArrayList;

import com.happyryan2.game.LevelPack;
import com.happyryan2.levels.Tutorial1;

public class TutorialPack extends LevelPack {
	public TutorialPack() {
		this.levels = new ArrayList();
		this.levels.add(new Tutorial1());
	}
}
