
package com.github.epd.sprout.actors.mobs.npcs;

import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.SheepSprite;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class SheepSokobanStop extends NPC {

	private static final String[] QUOTES = {Messages.get(SheepSokoban.class, "one"), Messages.get(SheepSokoban.class, "two"), Messages.get(SheepSokoban.class, "three"), Messages.get(SheepSokoban.class, "four")};

	{
		name = Messages.get(SheepSokobanStop.class, "name");
		spriteClass = SheepSprite.class;

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
		return Messages.get(SheepSokobanStop.class, "desc");
	}

/*  -W-1 -W  -W+1
 *  -1    P  +1
 *  W-1   W  W+1
 * 
 */

	@Override
	public boolean interact() {
		GLog.n(Messages.get(SheepSokoban.class, "sheepname") + Random.element(QUOTES));
		return false;
	}

}
