package com.happyryan2.packs;

import com.happyryan2.game.LevelPack;
import com.happyryan2.levels.*;

public class ChallengePack extends LevelPack {
	public ChallengePack() {
		super.name = "Challenge Pack";
        super.levels.add(new Challenge1());
        super.levels.add(new Challenge2());
        super.levels.add(new Challenge3());
        super.levels.add(new Challenge4());
	}
}
