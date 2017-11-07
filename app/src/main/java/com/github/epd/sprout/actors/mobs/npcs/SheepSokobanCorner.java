
package com.github.epd.sprout.actors.mobs.npcs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.SokobanCornerSheepSprite;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class SheepSokobanCorner extends NPC {

	private static final String[] QUOTES = {Messages.get(SheepSokoban.class, "one"), Messages.get(SheepSokoban.class, "two"), Messages.get(SheepSokoban.class, "three"), Messages.get(SheepSokoban.class, "four")};

	{
		name = Messages.get(SheepSokobanCorner.class, "name");
		spriteClass = SokobanCornerSheepSprite.class;

		properties.add(Property.IMMOVABLE);
	}


	@Override
	protected boolean act() {
		throwItem();
		return super.act();
	}

	@Override
	public void damage(int dmg, Object src) {
	}

	@Override
	public void add(Buff buff) {
	}

	@Override
	public String description() {
		return Messages.get(SheepSokobanCorner.class, "desc");
	}

/*  -W-1 -W  -W+1
 *  -1    P  +1
 *  W-1   W  W+1
 * 
 */

	@Override
	public boolean interact() {
		int curPos = pos;
		int movPos = pos;
		int width = Dungeon.level.getWidth();
		boolean moved = false;
		int posDif = Dungeon.hero.pos - curPos;

		if (posDif == 1) {
			movPos = curPos - 1;
		} else if (posDif == -1) {
			movPos = curPos + 1;
		} else if (posDif == width) {
			movPos = curPos - width;
		} else if (posDif == -width) {
			movPos = curPos + width;
		} else if (posDif == -width + 1) {
			movPos = curPos + width - 1;

		} else if (posDif == -width - 1) {
			movPos = curPos + width + 1;

		} else if (posDif == width + 1) {
			movPos = curPos - (width + 1);

		} else if (posDif == width - 1) {
			movPos = curPos - (width - 1);
		}

		if (movPos != pos && (Level.passable[movPos] || Level.avoid[movPos]) && Actor.findChar(movPos) == null) {

			moveSprite(curPos, movPos);
			move(movPos);
			moved = true;

		}

		if (moved) {
			Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
			Dungeon.hero.move(curPos);
		}

		GLog.n(Messages.get(SheepSokoban.class, "sheepname") + Random.element(QUOTES));

		return true;

	}

}
