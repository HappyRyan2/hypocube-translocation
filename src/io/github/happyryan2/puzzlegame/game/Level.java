package io.github.happyryan2.puzzlegame.game;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.util.ArrayList;

import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.utilities.Screen;
import io.github.happyryan2.puzzlegame.utilities.MouseClick;
import io.github.happyryan2.puzzlegame.utilities.MousePos;
import io.github.happyryan2.puzzlegame.utilities.ResourceLoader;
import io.github.happyryan2.puzzlegame.game.LevelSelect;
import io.github.happyryan2.puzzlegame.game.LevelConnector;

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
	public ImageButton exit = new ImageButton(30, 30, 40, "res/graphics/buttons/pause2.png", new Color(255, 255, 255), new Color(175, 175, 175)); // exit button on play screen
	public ImageButton exit2 = new ImageButton(0, 0, 50, "res/graphics/buttons/next.png", new Color(255, 255, 255), new Color(175, 175, 175)); // exit button on win screen
	public ImageButton restart2 = new ImageButton(0, 0, 50, "res/graphics/buttons/restart.png", new Color(255, 255, 255), new Color(175, 175, 175)); // restart button on play screen
	public ImageButton restart = new ImageButton(100, 30, 40, "res/graphics/buttons/restart.png", new Color(255, 255, 255), new Color(175, 175, 175)); // restart button on win screen
	public ImageButton undo = new ImageButton(170, 30, 40, "res/graphics/buttons/undo.png", new Color(255, 255, 255), new Color(175, 175, 175));

	public boolean paused = false;

	/* Visual size + location of top-left corner (in pixels) */
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
	public Level(Level level) {
		/* Copy constructor */
		this();
		this.index = level.index;
		this.depth = level.depth;
		this.width = level.width; this.height = level.height;
		for(short i = 0; i < level.content.size(); i ++) {
			Thing thing = (Thing) level.content.get(i);
			if(thing instanceof Extender) {
				Extender extender = new Extender((Extender) thing);
				this.content.add(new Extender(extender));
			}
			else if(thing instanceof Retractor) {
				Retractor retractor = new Retractor((Retractor) thing);
				this.content.add(new Retractor(retractor));
			}
			else if(thing instanceof LongExtender) {
				LongExtender longExtender = new LongExtender((LongExtender) thing);
				this.content.add(new LongExtender(longExtender));
			}
			else if(thing instanceof Player) {
				this.content.add(new Player((Player) thing));
			}
			else if(thing instanceof Goal) {
				this.content.add(new Goal((Goal) thing));
			}
			else if(thing instanceof Wall) {
				this.content.add(new Wall((Wall) thing));
			}
		}
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
			else if(thing instanceof LongExtender) {
				copyLoop: for(short j = 0; j < level.content.size(); j ++) {
					Thing thing2 = (Thing) level.content.get(j);
					if(thing2 instanceof LongExtender && thing2.x == thing.x && thing2.y == thing.y && thing2.dir == thing.dir && thing2.extension == thing.extension) {
						hasCopy = true;
						break copyLoop;
					}
				}
			}
			else if(thing instanceof Wall) {
				copyLoop: for(short j = 0; j < level.content.size(); j ++) {
					Thing thing2 = (Thing) level.content.get(j);
					if(thing2 instanceof Wall && thing2.x == thing.x && thing2.y == thing.y) {
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
		/* Move win GUI + buttons away */
		this.completeNow = false;
		this.completionY = -800;
		this.undo.y = 30;
		this.exit.y = 30;
		this.restart.y = 30;
		/* Reset all game objects to original position */
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			thing.x = thing.origX;
			thing.y = thing.origY;
			if(thing instanceof Extender || thing instanceof Retractor || thing instanceof LongExtender) {
				thing.extending = false;
				thing.retracting = false;
				thing.extension = thing.origExtension;
			}
			else if(thing instanceof Player || thing instanceof Goal) {
				thing.deleted = false;
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
		/* Update level size */
		this.resize();
		/* Buttons shown while playing */
		this.restart.update();
		this.exit.update();
		if(UndoStack.stack.size() != 0) {
			this.undo.update();
		}
		else if(this.undo.hoverY > 0) {
			this.undo.hoverY --;
		}
		if(this.undo.pressed && !this.undo.pressedBefore && !Game.chainUndo && !Game.chainUndoLastFrame && !this.transitioning() && UndoStack.stack.size() > 0) {
			UndoStack.undoAction();
		}
		if(this.exit.pressed) {
			UndoStack.resetStack();
			Game.transition = 255;
			Game.state = "level-select";
			return;
		}
		if(this.restart.pressed && !this.restart.pressedBefore) {
			Game.transition = 255;
			this.paused = false;
			this.reset();
			UndoStack.resetStack();
		}
		/* Update objects in level */
		this.updateContent();
		/* Win menu buttons */
		if(this.isComplete()) {
			if(!this.completedBefore) {
				this.completedBefore = true;
				Game.saveProgress();
			}
			this.completeNow = true;
			this.restart2.update();
			this.exit2.update();
			this.exit.y -= 5;
			this.undo.y -= 5;
			this.restart.y -= 5;
			if(this.exit2.pressed) {
				Game.transition = 255;
				Game.state = "level-select";
				UndoStack.resetStack();
				for(short i = 0; i < LevelSelect.levelConnectors.size(); i ++) {
					LevelConnector connector = (LevelConnector) LevelSelect.levelConnectors.get(i);
					if(connector.previousLevel == this.id && connector.animationProgress == 0) {
						connector.animationProgress = 1;
					}
				}
			}
			if(this.restart2.pressed) {
				Game.transition = 255;
				this.reset();
				UndoStack.resetStack();
			}
		}
		if(Game.timeSinceLastAction < 1 / Game.animationSpeed) {
			Game.timeSinceLastAction ++;
		}
		// System.out.println("transitioning(true) ? " + this.transitioning(true));
		// System.out.println("last action? " + Game.lastAction);
		if(Game.chainUndo && !this.transitioning(true) && !Game.lastAction) {
			// System.out.println("Doing a chain undo");
			UndoStack.undoAction();
		}
		// this.fastForward();
	}
	public void updateContent() {
		/* Load content */
		this.clearSelected();
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			thing.update();
		}
		/* Undos + movement */
		if(Game.lastAction && Game.timeSinceLastAction >= 1 / Game.animationSpeed) {
			for(short i = 0; i < this.content.size(); i ++) {
				Thing thing = (Thing) this.content.get(i);
				thing.x = Math.round(thing.x);
				thing.y = Math.round(thing.y);
				thing.extension = Math.round(thing.extension);
				thing.extending = false;
				thing.retracting = false;
				thing.moveDir = "none";
				thing.timeMoving = 0;
				if(thing instanceof LongExtender) {
					LongExtender ex = (LongExtender) thing;
					ex.timeExtending = 0;
					ex.timeRetracting = 0;
				}
			}
			Game.lastAction = false;
			Game.chainUndo = false;
		}
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			thing.move();
		}
	}
	public void display(Graphics g) {
		/* walls + background */
		g.setColor(wallColor);
		g.fillRect(0, 0, Screen.screenW, Screen.screenH);
		for(byte x = 0; x < this.width; x ++) {
			for(byte y = 0; y < this.height; y ++) {
				this.fillArea(g, x, y);
			}
		}
		/* Display objects */
		g.translate(this.left, this.top);
		for(short i = 0; i < this.content.size(); i ++) {
			/* Display goals underneath the rest of the objects*/
			Thing thing = (Thing) this.content.get(i);
			if(thing instanceof Goal) {
				thing.display(g);
			}
		}
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(!(thing instanceof Goal)) {
				thing.display(g);
			}
		}
		g.translate(-this.left, -this.top);
		/* GUI box for winning */
		if(this.isComplete()) {
			this.restart2.display(g);
			this.exit2.display(g);
			if(this.completionY < 0) {
				this.completionY += Math.max((0 - this.completionY) / 15, 1);
			}
			g.setColor(new Color(100, 100, 100, 150));
			g.fillRect(Screen.screenW / 2 - 200, this.completionY, 400, Screen.screenH);
			g.setFont(Screen.fontRighteous);
			g.setColor(new Color(255, 255, 255));
			Screen.centerText(g, Screen.screenW / 2, this.completionY + 266, "Level Complete");
			this.restart2.y = this.completionY + Screen.screenH / 3 * 2;
			this.exit2.y = this.completionY + Screen.screenH / 3 * 2;
			this.restart2.display(g);
			this.exit2.display(g);
			this.restart2.x = Screen.screenW / 2 - 50;
			this.exit2.x = Screen.screenW / 2 + 50;
		}
		else {
			this.completionY = -Screen.screenH;
		}
		if(this.paused) {
			this.restart.x = Screen.screenW / 2 - 100;
			this.restart.y = Screen.screenH / 2 - 75;
			this.exit.x = Screen.screenW / 2 - 100;
			this.exit.y = Screen.screenH / 2;
			g.setColor(new Color(100, 100, 100, 150));
			g.fillRect(Screen.screenW / 2 - 200, 0, 400, Screen.screenH);
			g.setFont(Screen.fontRighteous);
			g.setColor(new Color(255, 255, 255));
			Screen.centerText(g, Screen.screenW / 2, Screen.screenH / 2 - 105, "Menu");
			this.restart.display(g);
			this.exit.display(g);
		}
		/* Buttons */
		this.exit.display(g);
		this.undo.display(g);
		this.restart.display(g);
	}
	public void displayLevelSelect(Graphics g) {
		/* Don't display if the level has not been discovered */
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
		/* Display level image with opacity */
		this.opacity += 0.05;
		Image img = (this.completedBefore) ? LevelSelect.completeLevel : (this.canPlay() ? LevelSelect.incompleteLevel : LevelSelect.inaccessibleLevel);
		Graphics2D g2 = (Graphics2D) g;
		if(this.opacity < 1) {
			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.opacity);
			g2.setComposite(composite);
		}
		g2.translate(this.x, this.y);
		Screen.scaleImage(g2, img, 100, 100);
		/* Display level number */
		if(this.completedBefore) {
			g2.setColor(new Color(66, 139, 255));
		}
		else if(this.canPlay()) {
			g2.setColor(new Color(191, 54, 255));
		}
		else {
			g2.setColor(new Color(150, 150, 150));
		}
		g2.setFont(Screen.fontRighteous.deriveFont(40f));
		Screen.centerText(g, 50, 65, "" + this.id);
		g2.translate(-this.x, -this.y);
		AlphaComposite composite2 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
		g2.setComposite(composite2);
	}
	public void updateLevelSelect() {
		/* Detect clicks */
		if(MousePos.x > x + LevelSelect.scrollX + (Screen.screenW / 2) && MousePos.x < x + LevelSelect.scrollX + (Screen.screenW / 2) + 100 && MousePos.y > y + LevelSelect.scrollY + (Screen.screenH / 2) && MousePos.y < y + LevelSelect.scrollY + (Screen.screenH / 2) + 100 && this.opacity >= 1 && this.canPlay()) {
			Screen.cursor = "hand";
			if(MouseClick.mouseIsPressed && !MouseClick.pressedBefore) {
				Game.levelOpen = this.id;
				Game.currentLevel = this;
				Game.state = "play";
				Game.transition = 255;
				this.reset();
				this.resize();
			}
		}
		/* Update whether it has been discovered or not */
		if(this.requireAll && false) {
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
		if(this.id == 1) {
			return true;
		}
		if(this.requireAll) {
			/* Return false if some of the required levels have not been completed */
			for(short i = 0; i < Game.levels.size(); i ++) {
				Level level = (Level) Game.levels.get(i);
				for(short j = 0; j < this.requirements.size(); j ++) {
					int req = (int) this.requirements.get(j);
					if(level.id == req && !level.completedBefore) {
						return false;
					}
				}
			}
			/* Return false if any of the lines are still showing the animation */
			for(short i = 0; i < LevelSelect.levelConnectors.size(); i ++) {
				LevelConnector connector = (LevelConnector) LevelSelect.levelConnectors.get(i);
				if(connector.nextLevel == this.id && connector.animationProgress < connector.size) {
					return false;
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
			if(thing instanceof LongExtender && Game.debugging) {
				// System.out.println("Found a long extener at (" + thing.x + ", " + thing.y + ")");
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
			if(
				(thing.x == x && thing.y >= y - thing.extension && thing.y < y && thing.dir == "down") ||
				(thing.x == x && thing.y <= y + thing.extension && thing.y > y && thing.dir == "up") ||
				(thing.y == y && thing.x >= x - thing.extension && thing.x < x && thing.dir == "right") ||
				(thing.y == y && thing.x <= x + thing.extension && thing.x > x && thing.dir == "left")
			) {
				return thing;
			}
		}
		// if you haven't found anything by now, there must not be anything there
		return null;
	};
	public void select(float x, float y) {
		/* Selects the object at (x, y) */
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
		if(thing != null && !(thing instanceof Goal)) {
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
					// System.out.println("something is being pulled up");
					this.moveObject(x, y + 1, "up");
				}
			}
			else if(dir == "down") {
				/* Find things being pushed by this object */
				this.moveObject(x, y + 1, dir);
				/* Find things being pulled by this object */
				Thing retractor = this.getAtPos(x, y - 1);
				if(retractor != null && !retractor.selected && retractor instanceof Retractor && retractor.y + retractor.extension == y - 1 && retractor.dir == "down") {
					this.moveObject(x, y - 1, "down");
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
			// System.out.println("moved object at (" + x + ", " + y + ") " + dir);
			this.select(x, y);
			thing.checkMovement(dir);
		}
	}

	public void moveSelected(String dir) {
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing.selected) {
				thing.x = Math.round((float) thing.x);
				thing.y = Math.round((float) thing.y);
				thing.moveDir = dir;
				thing.timeMoving = 0;
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
					return false;
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
		return true;
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

	// public void fastForward() {
	// 	for(short i = 0; i < this.content.size(); i ++) {
	// 		Thing thing = (Thing) this.content.get(i);
	// 		if(thing instanceof Goal) {
	// 			continue;
	// 		}
	// 		if((thing instanceof Extender || thing instanceof Retractor) && thing.extending) {
	// 			thing.extension = 1;
	// 			thing.extending = false;
	// 		}
	// 		if((thing instanceof Extender || thing instanceof Retractor) && thing.retracting) {
	// 			thing.extension = 0;
	// 			thing.retracting = false;
	// 		}
	// 		if(thing instanceof LongExtender) {
	// 			if(thing.extending) {
	// 				thing.extension = Math.round(thing.extension + 1);
	// 			}
	// 			else if(thing.retracting) {
	// 				thing.extension = Math.round(thing.extension - 1);
	// 			}
	// 			if((thing.extending || thing.retracting) && !Game.chainUndo) {
	// 				LongExtender le = (LongExtender) thing;
	// 				le.timeExtending = (int) (1 / Game.animationSpeed);
	// 				le.timeRetracting = (int) (1 / Game.animationSpeed);
	// 				thing.move();
	// 				if(thing.extension != 0) {
	// 					System.out.println("Extension during fastForward(): " + thing.extension + ", Time moving: " + thing.timeMoving);
	// 				}
	// 				// thing.extending = false;
	// 				// thing.retracting = false;
	// 			}
	// 		}
	// 		if(thing instanceof Retractor) {
	// 			System.out.println("It's direction is: " + thing.moveDir);
	// 		}
	// 		if(thing.moveDir == "up") {
	// 			thing.y = Math.round(thing.y - 1);
	// 		}
	// 		else if(thing.moveDir == "down") {
	// 			thing.y = Math.round(thing.y + 1);
	// 		}
	// 		else if(thing.moveDir == "right") {
	// 			thing.x = Math.round(thing.x + 1);
	// 		}
	// 		else if(thing.moveDir == "left") {
	// 			System.out.println("Moving something left (inside fastForward())");
	// 			thing.x = Math.round(thing.x - 1);
	// 		}
	// 		thing.moveDir = "none";
	// 	}
	// 	if(this.transitioning()) {
	// 		if(Game.chainUndo) {
	// 			UndoStack.undoAction();
	// 			if(Game.lastAction) {
	// 				Game.chainUndo = false;
	// 			}
	// 		}
	// 		for(short i = 0; i < this.content.size(); i ++) {
	// 			Thing thing = (Thing) this.content.get(i);
	// 			if(thing.moveDir == "left") {
	// 				System.out.println("Something is moving left (right before fastForward() recurses)");
	// 			}
	// 		}
	// 		this.fastForward();
	// 	}
	// }
	public void fastForward() {
		boolean transitioningBefore = true;
		int numIterations = 0;
		while(this.transitioning(true)) {
			numIterations ++;
			this.updateContent();
			// this.printContent();
			if(!this.transitioning(true) && !transitioningBefore) {
				return;
			}
			if(numIterations > 1000) {
				System.out.println("Possible infinte loop?");
				Game.currentLevel.printContent();
				System.out.println("Is it doing a chain undo? " + Game.chainUndo);
			}
			transitioningBefore = this.transitioning();
			System.out.println("In the while loop");
		}
		// System.out.println("While loop ended.");
		if(true) { return; }
		if(Game.chainUndo) {
			for(short i = 0; i < this.content.size(); i ++) {
				Thing thing = (Thing) this.content.get(i);
				if(thing.moveDir != "none") {
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
						System.out.println("Moving something left (inside fastForward())");
						thing.x = Math.round(thing.x - 1);
					}
					thing.moveDir = "none";
				}
				if(thing instanceof LongExtender) {
					if(thing.extending) {
						thing.extension = Math.round(thing.extension + 1) - Game.animationSpeed;
						LongExtender le = (LongExtender) thing;
						le.timeExtending = (int) (1 / Game.animationSpeed);
					}
					else if(thing.retracting) {
						thing.extension = Math.round(thing.extension - 1) - Game.animationSpeed;
						LongExtender le = (LongExtender) thing;
						le.timeRetracting = (int) (1 / Game.animationSpeed);
					}
				}
				else if(thing instanceof Extender || thing instanceof Retractor) {
					if(thing.extending) {
						thing.extension = 1;
						thing.extending = false;
					}
					else if(thing.retracting) {
						thing.extension = 0;
						thing.retracting = false;
					}
				}
			}
			this.snapToGrid();
			if(!Game.lastAction) {
				UndoStack.undoAction(true);
				this.fastForward();
			}
		}
		else {
			for(short i = 0; i < this.content.size(); i ++) {
				Thing thing = (Thing) this.content.get(i);
				if(thing.moveDir != "none") {
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
						System.out.println("Moving something left (inside fastForward())");
						thing.x = Math.round(thing.x - 1);
					}
					thing.moveDir = "none";
				}
				if(thing instanceof LongExtender) {
					if(thing.extending) {
						thing.extension = Math.round(thing.extension + 1) - Game.animationSpeed;
						LongExtender le = (LongExtender) thing;
						le.timeExtending = (int) (1 / Game.animationSpeed);
						le.move();
					}
					else if(thing.retracting) {
						thing.extension = Math.round(thing.extension - 1) - Game.animationSpeed;
						LongExtender le = (LongExtender) thing;
						le.timeRetracting = (int) (1 / Game.animationSpeed);
						le.move();
					}
				}
				else if(thing instanceof Extender || thing instanceof Retractor) {
					if(thing.extending) {
						thing.extension = 1;
						thing.extending = false;
					}
					else if(thing.retracting) {
						thing.extension = 0;
						thing.retracting = false;
					}
				}
			}
			if(this.transitioning()) {
				this.fastForward();
			}
		}
	}
	public boolean transitioning(boolean ignoreChainUndos) {
		if(Game.chainUndo && !ignoreChainUndos) {
			return true;
		}
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing.moveDir != "none" || thing.extending || thing.retracting && !(thing instanceof Goal)) {
				// System.out.println("Found an object at (" + thing.x + ", " + thing.y + ") that is transitioning");
				// if(thing.moveDir != "none") {
				// 	System.out.println(" - it is moving " + thing.moveDir);
				// }
				// else if(thing.extending) {
				// 	System.out.println(" - it is extending");
				// }
				// else if(thing.retracting) {
				// 	System.out.println(" - it is retracting");
				// }
				return true;
			}
		}
		return false;
	}
	public boolean transitioning() {
		return this.transitioning(false);
	}

	public boolean isEmpty(int x, int y) {
		/* Return whether there is not a wall at that position */
		if(x < 0 || y < 0 || x >= width || y >= height || this.getAtPos(x, y) instanceof Wall) {
			return false;
		}
		return true;
	}
	public void fillArea(Graphics g, int x, int y) {
		/*
		Fills an area the correct color + with rounded corners where necessary.
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
			else if(thing instanceof LongExtender) {
				str += "a" + (thing.isWeak ? " weak" : "") + " long extender at (" + thing.x + ", " + thing.y + ") with an extension of " + thing.extension;
			}
			else if(thing instanceof Wall) {
				str += "a wall at (" + thing.x + ", " + thing.y + ")";
			}
			str += ", ";
		}
		return str;
	}
	public void printContent() {
		System.out.println("-----------------");
		System.out.println("Objects in level " + this.id + ":");
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing instanceof Player) {
				System.out.println(" - A player at (" + thing.x + ", " + thing.y + ")");
			}
			else if(thing instanceof Goal) {
				System.out.println(" - A goal at (" + thing.x + ", " + thing.y + ")");
			}
			else if(thing instanceof Extender) {
				System.out.println(" - A " + (thing.isWeak ? " weak" : "") + " extender at (" + thing.x + ", " + thing.y + ") that " + (thing.extension == 1 ? " is " : " is not ") + " extended");
			}
			else if(thing instanceof Retractor) {
				System.out.println(" - A " + (thing.isWeak ? " weak" : "") + " extender at (" + thing.x + ", " + thing.y + ") that " + (thing.extension == 1 ? " is " : " is not ") + " extended");
			}
			else if(thing instanceof LongExtender) {
				System.out.println(" - A " + (thing.isWeak ? "weak " : "") + "long extender at (" + thing.x + ", " + thing.y + ") with an extension of " + thing.extension);
			}
			else if(thing instanceof Wall) {
				System.out.println(" - A wall at (" + thing.x + ", " + thing.y + ")");
			}
		}
		System.out.println("End printing for level " + this.id);
		System.out.println("-----------------");
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

	public boolean canTileBePushed(int x, int y, String dir) {
		/* Return false if it is being pushed into a border wall */
		if((x == 0 && dir == "left") || (x == this.width - 1 && dir == "right") || (y == 0 && dir == "up") || (y == this.height - 1 && dir == "down")) {
			return false;
		}
		/* Return false if it is being pushed into a regular wall */
		if(dir == "left" && this.getAtPos(x - 1, y) instanceof Wall) {
			return false;
		}
		else if(dir == "right" && this.getAtPos(x + 1, y) instanceof Wall) {
			return false;
		}
		else if(dir == "up" && this.getAtPos(x, y - 1) instanceof Wall) {
			return false;
		}
		else if(dir == "down" && this.getAtPos(x, y + 1) instanceof Wall) {
			return false;
		}
		return true;
	}
	public boolean canSelectedBePushed(String dir) {
		/* Returns whether all the selected objects can be pushed in the direction. */
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing.selected && !thing.canBePushed(dir)) {
				return false;
			}
		}
		return true;
	}

	public void snapToGrid() {
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			thing.x = Math.round((float) thing.x);
			thing.y = Math.round((float) thing.y);
			thing.extension = Math.round((float) thing.extension);
			thing.timeMoving = 0;
			thing.moveDir = "none";
		}
	}
}
