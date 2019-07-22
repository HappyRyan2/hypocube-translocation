package com.happyryan2.game;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.util.ArrayList;

import com.happyryan2.objects.*;
import com.happyryan2.utilities.Screen;
import com.happyryan2.utilities.MouseClick;
import com.happyryan2.utilities.MousePos;
import com.happyryan2.utilities.ImageLoader;
import com.happyryan2.game.Button;
import com.happyryan2.game.LevelSelect;
import com.happyryan2.game.LevelConnector;

public class Level {
	public static Color backgroundColor = new Color(200, 200, 200);
	public static Color wallColor = new Color(150, 150, 150);

	/* Level-dependent variables */
	public int x, y; // coordinates on level select
	public int id; // level number (1, 2, 3, etc.) used by "requirements" array
	public List requirements = new ArrayList(); // which levels are required to gain access to this one
	public boolean requireAll = false; // do you need to complete all the levels in "requirements", or just one? (AND vs OR)
	public boolean manualSize = false;
	public int width = 0;
	public int height = 0;
	public List content;

	public boolean completedBefore = false;
	public boolean completeNow = false;
	public boolean discovered = false;
	private boolean resized = false;
	public int completionY = -800;
	public float opacity = 0;

	/* Buttons */
	public Button next = new Button(500, 100, 50, 50, new Color(200, 200, 200), new Color(255, 255, 255), "icon:arrowright", "circle");
	public Button menu = new Button(400, 100, 50, 50, new Color(200, 200, 200), new Color(255, 255, 255), "icon:3rects", "circle");
	public Button retry = new Button(300, 100, 50, 50, new Color(200, 200, 200), new Color(255, 255, 255), "icon:arrowleft", "circle"); // retry button that shows when you have completed the level
	public Button pause = new Button(30, 30, 40, 40, new Color(175, 175, 175), new Color(255, 255, 255), "icon:2rects", "circle");
	public Button restart = new Button(300, 325, 200, 50, new Color(200, 200, 200), new Color(255, 255, 255), "Restart", "rect"); // retry button that shows when you are on the pause screen
	public Button exit = new Button(300, 400, 200, 50, new Color(200, 200, 200), new Color(255, 255, 255), "Exit", "rect");
	public Button undo = new Button(100, 30, 40, 40, new Color(175, 175, 175), new Color(255, 255, 255), "icon:arrowleft", "circle");

	public boolean lastLevel = false;
	public boolean paused = false;

	/* Visual size + location (in pixels) */
	public int visualWidth = 0;
	public int visualHeight = 0;
	public int top = 0;
	public int left = 0;

	/* Testing properties (mostly used for auto-solving algorithm) */
	public boolean isForTesting = false;
	public int depth = 0;
	public int preX;
	public int preY;
	public int parentIndex;
	public int index;

	public Level() {
		this.content = new ArrayList();
	}
	public Level copy() {
		Level clone = new Level();
		clone.index = this.index;
		clone.depth = this.depth;
		clone.width = this.width; clone.height = this.height;
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing instanceof Extender) {
				Extender extender = new Extender(thing.x, thing.y, thing.dir, thing.isWeak);
				extender.extension = thing.extension;
				clone.content.add(extender);
			}
			else if(thing instanceof Retractor) {
				Retractor retractor = new Retractor(thing.x, thing.y, thing.dir, thing.isWeak);
				retractor.extension = thing.extension;
				clone.content.add(retractor);
			}
			else if(thing instanceof Player) {
				clone.content.add(new Player(thing.x, thing.y));
			}
			else if(thing instanceof Goal) {
				clone.content.add(new Goal(thing.x, thing.y));
			}
			else if(thing instanceof Wall) {
				clone.content.add(new Wall(thing.x, thing.y));
			}
		}
		return clone;
	}
	public boolean equals(Level level) {
		/*
		Return true if this level equals the other level. (Only checks items in level, not buttons, win animation, etc.)
		*/
		if(this.content.size() != level.content.size()) {
			return false;
		}
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			boolean hasCopy = false;
			if(thing instanceof Player) {
				copyLoop: for(short j = 0; j < level.content.size(); j ++) {
					Thing thing2 = (Thing) level.content.get(j);
					if(thing2 instanceof Player && thing2.x == thing.x && thing2.y == thing.y) {
						hasCopy = true;
						break copyLoop;
					}
				}
			}
			else if(thing instanceof Goal) {
				copyLoop: for(short j = 0; j < level.content.size(); j ++) {
					Thing thing2 = (Thing) level.content.get(j);
					if(thing2 instanceof Goal && thing2.x == thing.x && thing2.y == thing.y) {
						hasCopy = true;
						break copyLoop;
					}
				}
			}
			else if(thing instanceof Extender) {
				copyLoop: for(short j = 0; j < level.content.size(); j ++) {
					Thing thing2 = (Thing) level.content.get(j);
					if(thing2 instanceof Extender && thing2.x == thing.x && thing2.y == thing.y && thing2.dir == thing.dir && thing2.extension == thing.extension) {
						hasCopy = true;
						break copyLoop;
					}
				}
			}
			else if(thing instanceof Retractor) {
				copyLoop: for(short j = 0; j < level.content.size(); j ++) {
					Thing thing2 = (Thing) level.content.get(j);
					if(thing2 instanceof Retractor && thing2.x == thing.x && thing2.y == thing.y && thing2.dir == thing.dir && thing2.extension == thing.extension) {
						hasCopy = true;
						break copyLoop;
					}
				}
			}
			if(!hasCopy) {
				return false;
			}
		}
		return true;
	}

	public void reset() {
		this.completeNow = false;
		this.completionY = -800;
		this.pause.y = 30;
		this.undo.y = 30;
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			thing.x = thing.origX;
			thing.y = thing.origY;
			if(thing instanceof Extender || thing instanceof Retractor) {
				thing.extending = false;
				thing.retracting = false;
				thing.extension = 0;
			}
			else if(thing instanceof Player || thing instanceof Goal) {
				thing.deleted = false;
				thing.hoverY = 0;
				thing.color = 0;
				thing.winAnimation = false;
			}
		}
	}
	public void resize() {
		/* Calculate size in grid units */
		if(!this.manualSize && this.width == 0 && this.height == 0) {
			float left = 0;
			float right = 0;
			float top = 0;
			float bottom = 0;
			for(short i = 0; i < this.content.size(); i ++) {
				Thing thing = (Thing) this.content.get(i);
				float x = (float) thing.x;
				float y = (float) thing.y;
				if(x < left) {
					left = x;
				}
				else if(x > right) {
					right = x;
				}
				if(y < top) {
					top = y;
				}
				else if(y > bottom) {
					bottom = y;
				}
			}
			Game.levelSize = (right - left > bottom - top) ? (right - left + 1) : (bottom - top + 1);
			this.width = (int) (right - left + 1);
			this.height = (int) (bottom - top + 1);
		}
		/* Calculate size available (subtract margins) */
		int screenW = Screen.screenW / 4 * 3;
		int screenH = Screen.screenH / 4 * 3;
		if(this.width > this.height && screenW * ((float) (this.height) / this.width) < screenH) {
			/* Wider than it is tall */
			this.visualWidth = screenW;
			this.visualHeight = (int) Math.round(screenW * ((float) (this.height) / this.width));
			this.left = Screen.screenW / 8;
			this.top = (int) Math.round((Screen.screenH - this.visualHeight) / 2);
			Game.tileSize = this.visualHeight / this.height;
		}
		else {
			/* Taller than it is wide */
			this.visualHeight = screenH;
			this.visualWidth = (int) Math.round(screenH * ((float) (this.width) / this.height));
			this.top = Screen.screenH / 8;
			this.left = (int) Math.round((Screen.screenW - this.visualWidth) / 2);
			Game.tileSize = this.visualWidth / this.width;
		}
	}

	public void update() {
		/* initialize */
		if(!resized || true) {
			this.resize();
		}
		if(this.isComplete() || Game.transition > 5) {
			Game.canClick = false;
		}
		if(Game.startingLevel && !MouseClick.mouseIsPressed) {
			Game.canClick = true;
		}
		for(byte i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if((thing instanceof Extender || thing instanceof Retractor) && thing.extension != 0 && thing.extension != 1) {
				Game.canClick = false;
			}
		}
		for(byte i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if((thing instanceof Extender || thing instanceof Retractor) && ((thing.extension != 0 && thing.extension != 1) || thing.extending || thing.retracting)) {
				Game.canClick = false;
				break;
			}
			if(thing instanceof Player && (thing.x != Math.round(thing.x) || thing.y != Math.round(thing.y))) {
				Game.canClick = false;
				break;
			}
		}
		if(this.paused) {
			Game.canClick = false;
			this.restart.update();
			this.exit.update();
			if(this.restart.pressed && !this.isForTesting) {
				Game.transition = 255;
				this.paused = false;
				this.reset();
				Stack.resetStack();
			}
			if(this.exit.pressed) {
				this.paused = false;
				Game.transition = 255;
				Game.state = "level-select";
				Game.canClick = false;
				Game.startingLevel = true;
			}
		}
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			thing.selected = false;
		}
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			thing.update();
		}
		if(this.isComplete()) {
			this.completedBefore = true;
			this.completeNow = true;
			this.retry.update();
			this.menu.update();
			this.pause.y -= 5;
			this.undo.y -= 5;
			if(!this.lastLevel) {
				this.next.update();
			}
			if(this.menu.pressed) {
				Game.transition = 255;
				Game.state = "level-select";
				for(short i = 0; i < LevelSelect.levelConnectors.size(); i ++) {
					LevelConnector connector = (LevelConnector) LevelSelect.levelConnectors.get(i);
					if(connector.previousLevel == this.id && connector.animationProgress == 0) {
						connector.animationProgress = 1;
					}
				}
			}
			if(this.retry.pressed) {
				Game.transition = 255;
				Game.startingLevel = true;
				this.reset();
				Stack.resetStack();
			}
		}
		this.pause.update();
		if(this.pause.pressed && !this.pause.pressedBefore) {
			this.paused = !this.paused;
		}
		this.undo.update();
		if(this.undo.pressed && !this.undo.pressedBefore && Game.canClick) {
			Stack.undoAction();
		}
	}
	public void display(Graphics g) {
		// walls + background
		g.setColor(wallColor);
		g.fillRect(0, 0, Screen.screenW, Screen.screenH);
		for(byte x = 0; x < this.width; x ++) {
			for(byte y = 0; y < this.height; y ++) {
				this.fillArea(g, x, y);
			}
		}
		// sort by y-value (display top ones first)
		List sorted = new ArrayList();
		List unsorted = new ArrayList();
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			unsorted.add(thing);
		}
		while(unsorted.size() > 0) {
			int highestIndex = 0;
			for(short i = 0; i < unsorted.size(); i ++) {
				Thing thing = (Thing) unsorted.get(i);
				Thing highest = (Thing) unsorted.get(highestIndex);
				if(thing.y < highest.y && !(highest instanceof Goal) || thing instanceof Goal) {
					highestIndex = i;
				}
			}
			Thing thing = (Thing) unsorted.get(highestIndex);
			sorted.add(thing);
			unsorted.remove(highestIndex);
		}
		// display objects
		g.translate(this.left, this.top);
		for(short i = 0; i < sorted.size(); i ++) {
			Thing thing = (Thing) sorted.get(i);
			thing.display(g);
		}
		g.translate(-this.left, -this.top);
		//gui box for winning
		if(this.isComplete()) {
			if(this.completionY < 0) {
				this.completionY += Math.max((0 - this.completionY) / 15, 1);
			}
			g.setColor(new Color(100, 100, 100, 150));
			g.fillRect(Screen.screenW / 2 - 200, this.completionY, 400, Screen.screenH);
			g.setFont(Screen.fontRighteous);
			g.setColor(new Color(255, 255, 255));
			Screen.centerText(g, Screen.screenW / 2, this.completionY + 266, "Level Complete");
			this.retry.y = this.completionY + Screen.screenH / 3 * 2;
			this.menu.y = this.completionY + Screen.screenH / 3 * 2;
			this.next.y = this.completionY + Screen.screenH / 3 * 2;
			this.retry.display(g);
			this.menu.display(g);
			if(!this.lastLevel && false) {
				this.next.display(g);
			}
			else {
				this.retry.x = Screen.screenW / 2 - 50;
				this.menu.x = Screen.screenW / 2 + 50;
			}
		}
		else {
			this.completionY = -Screen.screenH;
		}
		if(this.paused) {
			this.restart.x = Screen.screenW / 2 - 75;
			this.restart.y = Screen.screenH / 2 - 75;
			this.exit.x = Screen.screenW / 2 - 75;
			this.exit.y = Screen.screenH / 2;
			g.setColor(new Color(100, 100, 100, 150));
			g.fillRect(Screen.screenW / 2 - 200, 0, 400, Screen.screenH);
			g.setFont(Screen.fontRighteous);
			g.setColor(new Color(255, 255, 255));
			Screen.centerText(g, Screen.screenW / 2, Screen.screenH / 2 - 100, "Menu");
			this.restart.display(g);
			this.exit.display(g);
		}
		// pause button
		this.pause.display(g);
		this.undo.display(g);
	}
	public void displayLevelSelect(Graphics g) {
		if(!this.discovered) {
			return;
		}
		if(this.opacity == 0) {
			boolean playingAnimation = false;
			for(short i = 0; i < LevelSelect.levelConnectors.size(); i ++) {
				LevelConnector connector = (LevelConnector) LevelSelect.levelConnectors.get(i);
				if(connector.animationProgress < connector.size && connector.animationProgress != 0) {
					return;
				}
			}
		}
		this.opacity += 0.05;
		Image img = (this.completedBefore) ? LevelSelect.completeLevel : LevelSelect.incompleteLevel;
		Graphics2D g2 = (Graphics2D) g;
		if(this.opacity < 1) {
			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.opacity);
			g2.setComposite(composite);
		}
		g2.translate(this.x, this.y);
		Screen.scaleImage(g2, img, 100, 100);
		if(this.completedBefore) {
			g2.setColor(new Color(66, 139, 255));
		}
		else {
			g2.setColor(new Color(191, 54, 255));
		}
		g2.setFont(Screen.fontRighteous.deriveFont(40f));
		Screen.centerText(g, 50, 65, "" + this.id);
		g2.translate(-this.x, -this.y);
		AlphaComposite composite2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
		g2.setComposite(composite2);
	}
	public void updateLevelSelect() {
		/* Detect clicks */
		if(MousePos.x > x + LevelSelect.scrollX && MousePos.x < x + LevelSelect.scrollX + 100 && MousePos.y > y + LevelSelect.scrollY && MousePos.y < y + LevelSelect.scrollY + 100 && this.opacity >= 1) {
			Screen.cursor = "hand";
			if(MouseClick.mouseIsPressed && !MouseClick.pressedBefore) {
				Game.levelOpen = this.id;
				Game.currentLevel = this;
				Game.canClick = true;
				Game.startingLevel = true;
				Game.state = "play";
				Game.transition = 255;
				this.reset();
				this.resize();
			}
		}
		/* Update whether it has been discovered or not */
		if(this.requireAll) {
			this.discovered = true;
			loop1: for(short i = 0; i < Game.levels.size(); i ++) {
				Level level = (Level) Game.levels.get(i);
				boolean required = false;
				loop2: for(short j = 0; j < this.requirements.size(); j ++) {
					int req = (int) this.requirements.get(j);
					if(req == level.id) {
						required = true;
						break loop2;
					}
				}
				if(required && !level.completedBefore) {
					this.discovered = false;
					break loop1;
				}
			}
		}
		else {
			this.discovered = false;
			loop1: for(short i = 0; i < Game.levels.size(); i ++) {
				Level level = (Level) Game.levels.get(i);
				boolean required = false;
				loop2: for(short j = 0; j < this.requirements.size(); j ++) {
					int req = (int) this.requirements.get(j);
					if(req == level.id) {
						required = true;
						break loop2;
					}
				}
				if(required && level.completedBefore) {
					this.discovered = true;
					break loop1;
				}
			}
		}
		if(this.id == 1) {
			this.discovered = true;
		}
	}
	public boolean canPlay() {
		if(!this.discovered) {
			return false;
		}
		if(this.requireAll) {
			for(short i = 0; i < Game.levels.size(); i ++) {
				Level level = (Level) Game.levels.get(i);
				for(short j = 0; j < this.requirements.size(); j ++) {
					int req = (int) this.requirements.get(j);
					if(level.id == req && !level.completedBefore) {
						return false;
					}
				}
			}
			return true;
		}
		else {
			for(short i = 0; i < Game.levels.size(); i ++) {
				Level level = (Level) Game.levels.get(i);
				for(short j = 0; j < this.requirements.size(); j ++) {
					int req = (int) this.requirements.get(j);
					if(level.id == req && level.completedBefore) {
						return true;
					}
				}
			}
			return false;
		}
	}

	public Thing getAtPos(float x, float y) {
		// return if there is an extender in that position
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing instanceof Goal) {
				continue;
			}
			if(thing.x == x && thing.y == y) {
				return thing;
			}
		}
		// return if there is an extender pushing out into that position
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing.extension == 0 || thing instanceof Goal) {
				continue;
			}
			if((thing.x == x && thing.y == y - 1 && thing.dir == "down") || (thing.x == x && thing.y == y + 1 && thing.dir == "up") || (thing.y == y && thing.x == x - 1 && thing.dir == "right") || (thing.y == y && thing.x == x + 1 && thing.dir == "left")) {
				return thing;
			}
		}
		// if you haven't found anything by now, there must not be anything there
		return null;
	};
	public void select(float x, float y) {
		Thing thing = this.getAtPos(x, y);
		if(thing != null && !thing.ignoring) {
			thing.selected = true;
		}
	}
	public void moveTile(float x, float y, String dir) {
		/*
		Selects the tile, as well as any other tiles that:
		 - would be pushed by it moving
		 - would be pulled by it moving
		*/
		Thing thing = this.getAtPos(x, y);
		if(thing != null && !thing.ignoring) {
			this.select(x, y);
			if(dir == "left") {
				/* Find things being pushed by this object */
				this.moveObject(x - 1, y, dir);
				/* Find things being pulled by this object */
				Thing retractor = this.getAtPos(x + 1, y);
				if(retractor != null && !retractor.selected && retractor instanceof Retractor && retractor.x - retractor.extension == x + 1 && retractor.dir == "left") {
					this.moveObject(x + 1, y, "left");
				}
			}
			else if(dir == "right") {
				/* Find things being pushed by this object */
				this.moveObject(x + 1, y, dir);
				/* Find things being pulled by this object */
				Thing retractor = (Thing) this.getAtPos(x - 1, y);
				if(retractor != null && !retractor.selected && retractor instanceof Retractor && retractor.x + retractor.extension == x - 1 && retractor.dir == "right") {
					this.moveObject(x - 1, y, "right");
				}
			}
			else if(dir == "up") {
				/* Find things being pushed by this object */
				this.moveObject(x, y - 1, dir);
				/* Find things being pulled by this object */
				Thing retractor = this.getAtPos(x, y + 1);
				if(retractor != null && !retractor.selected && retractor instanceof Retractor && retractor.y - retractor.extension == y + 1 && retractor.dir == "up") {
					this.moveObject(x, y + 1, "up");
				}
			}
			else if(dir == "down") {
				/* Find things being pushed by this object */
				this.moveObject(x, y + 1, dir);
				/* Find things being pulled by this object */
				Thing retractor = this.getAtPos(x, y - 1);
				if(retractor != null && !retractor.selected && retractor instanceof Retractor && retractor.y + retractor.extension == y - 1 && retractor.dir == "down") {
					this.moveObject(x, y + 1, "down");
				}
			}
		}
	}
	public void moveObject(float x, float y, String dir) {
		/*
		Selects the object at that position, as well as any other objects that:
		 - would be pushed by it moving
		 - would be pulled by it moving
		*/
		Thing thing = this.getAtPos(x, y);
		if(thing != null && !thing.ignoring && !thing.selected) {
			this.select(x, y);
			thing.checkMovement(dir);
		}
	}

	public void moveSelected(String dir) {
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing.selected) {
				thing.moveDir = dir;
			}
		}
	}
	public void clearSelected() {
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			thing.selected = false;
			thing.ignoring = false;
		}
	}
	public int numSelected() {
		int num = 0;
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing.selected) {
				num ++;
			}
		}
		return num;
	}

	public boolean isComplete() {
		/*
		Returns true if each goal has a player on it.
		*/
		boolean complete = true;
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing instanceof Goal) {
				boolean occupied = false;
				for(short j = 0; j < this.content.size(); j ++) {
					Thing thing2 = (Thing) this.content.get(j);
					if(thing2 instanceof Player && thing2.x == thing.x && thing2.y == thing.y) {
						occupied = true;
						break;
					}
				}
				if(!occupied) {
					complete = false;
					break;
				}
			}
		}
		boolean hasAGoal = false;
		for(byte i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing instanceof Goal) {
				hasAGoal = true;
			}
		}
		if(!hasAGoal) {
			return false; // level is under construction
		}
		return complete;
	}
	public boolean winAnimationDone() {
		/*
		Returns true if all goals are done with their color-changing animation. Unused
		*/
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing instanceof Goal && thing.color < 255) {
				return false;
			}
		}
		return true;
	}

	public void fastForward() {
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if((thing instanceof Extender || thing instanceof Retractor) && thing.extending) {
				thing.extension = 1;
				thing.extending = false;
			}
			if((thing instanceof Extender || thing instanceof Retractor) && thing.retracting) {
				thing.extension = 0;
				thing.retracting = false;
			}
			if(thing.moveDir == "up") {
				thing.y = Math.round(thing.y - 1);
			}
			else if(thing.moveDir == "down") {
				thing.y = Math.round(thing.y + 1);
			}
			else if(thing.moveDir == "right") {
				thing.x = Math.round(thing.x + 1);
			}
			else if(thing.moveDir == "left") {
				thing.x = Math.round(thing.x - 1);
			}
			thing.moveDir = "none";
		}
	}
	public boolean transitioning() {
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing.moveDir != "none" || thing.extending || thing.retracting) {
				return true;
			}
		}
		return false;
	}

	public boolean isEmpty(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) {
			return false;
		}
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing instanceof Wall && thing.x == x && thing.y == y) {
				return false;
			}
		}
		return true;
	}
	public void fillArea(Graphics g, int x, int y) {
		/*
		Fills an area the correct color + with rounded corners where necessary.
		(Walls = 200, Background = 150)
		*/
		Graphics2D g2 = (Graphics2D) g;
		if(this.isEmpty(x, y)) {
			g2.setColor(backgroundColor);
			RoundRectangle2D rect = new RoundRectangle2D.Float();
			rect.setRoundRect(x * Game.tileSize + this.left, y * Game.tileSize + this.top, Game.tileSize, Game.tileSize, 30 * (Game.tileSize / 300) * 2, 30 * (Game.tileSize / 300) * 2);
			g2.fill(rect);
			int visualX = Math.round(x * Game.tileSize + this.left);
			int visualY = Math.round(y * Game.tileSize + this.top);
			int tileSize = Math.round(Game.tileSize);
			int tileSize2 = Math.round(Game.tileSize / 2);
			if(isEmpty(x + 1, y)) {
				g2.fillRect(visualX + tileSize2, visualY, tileSize2, tileSize);
			}
			if(isEmpty(x - 1, y)) {
				g2.fillRect(visualX, visualY, tileSize2, tileSize);
			}
			if(isEmpty(x, y + 1)) {
				g2.fillRect(visualX, visualY + tileSize2, tileSize, tileSize2);
			}
			if(isEmpty(x, y - 1)) {
				g2.fillRect(visualX, visualY, tileSize, tileSize2);
			}
		}
		else {
			int visualX = Math.round(x * Game.tileSize + this.left);
			int visualY = Math.round(y * Game.tileSize + this.top);
			int tileSize = Math.round(Game.tileSize);
			int tileSize2 = Math.round(Game.tileSize / 2);
			g2.setColor(backgroundColor);
			g2.fillRect(visualX, visualY, tileSize, tileSize);
			g2.setColor(wallColor);
			RoundRectangle2D rect = new RoundRectangle2D.Float();
			rect.setRoundRect(x * Game.tileSize + this.left, y * Game.tileSize + this.top, Game.tileSize, Game.tileSize, 30 * (Game.tileSize / 300) * 2, 30 * (Game.tileSize / 300) * 2);
			g2.fill(rect);
			if(!isEmpty(x + 1, y)) {
				g2.fillRect(visualX + tileSize2, visualY, tileSize2, tileSize);
			}
			if(!isEmpty(x - 1, y)) {
				g2.fillRect(visualX, visualY, tileSize2, tileSize);
			}
			if(!isEmpty(x, y + 1)) {
				g2.fillRect(visualX, visualY + tileSize2, tileSize, tileSize2);
			}
			if(!isEmpty(x, y - 1)) {
				g2.fillRect(visualX, visualY, tileSize, tileSize2);
			}
		}
	}

	public String toString() {
		/*
		Create a string representation of the level's content - used for debugging.
		*/
		String str = "";
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing instanceof Player) {
				str += "a player at (" + thing.x + ", " + thing.y + ")";
			}
			else if(thing instanceof Goal) {
				str += "a goal at (" + thing.x + ", " + thing.y + ")";
			}
			else if(thing instanceof Extender) {
				str += "a" + (thing.isWeak ? " weak" : "") + " extender at (" + thing.x + ", " + thing.y + ") that " + (thing.extension == 1 ? " is " : " is not ") + " extended.";
			}
			else if(thing instanceof Retractor) {
				str += "a" + (thing.isWeak ? " weak" : "") + " retractor at (" + thing.x + ", " + thing.y + ") that " + (thing.extension == 1 ? " is " : " is not ") + " extended.";
			}
			else if(thing instanceof Wall) {
				str += "a wall at (" + thing.x + ", " + thing.y + ")";
			}
			str += ", ";
		}
		return str;
	}
	public String selectedToString() {
		/*
		Create a string representation of the level's seleted content - used for debugging.
		*/
		String str = "";
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(!thing.selected) {
				continue;
			}
			if(thing instanceof Player) {
				str += "a player at (" + thing.x + ", " + thing.y + ")";
			}
			else if(thing instanceof Goal) {
				str += "a goal at (" + thing.x + ", " + thing.y + ")";
			}
			else if(thing instanceof Extender) {
				str += "a" + (thing.isWeak ? " weak" : "") + " extender at (" + thing.x + ", " + thing.y + ") that " + (thing.extension == 1 ? " is " : " is not ") + " extended.";
			}
			else if(thing instanceof Retractor) {
				str += "a" + (thing.isWeak ? " weak" : "") + " retractor at (" + thing.x + ", " + thing.y + ") that " + (thing.extension == 1 ? " is " : " is not ") + " extended.";
			}
			else if(thing instanceof Wall) {
				str += "a wall at (" + thing.x + ", " + thing.y + ")";
			}
			str += ", ";
		}
		return str;
	}
}
