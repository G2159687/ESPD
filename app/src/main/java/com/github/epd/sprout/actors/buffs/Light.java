
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;

public class Light extends FlavourBuff {

	public static final float DURATION = 250f;
	public static final int DISTANCE = 4;

	@Override
	public boolean attachTo(Char target) {
		if (super.attachTo(target)) {
			if (Dungeon.level != null) {
				target.viewDistance = Math.max(Dungeon.level.viewDistance,
						DISTANCE);
				Dungeon.observe();
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void detach() {
		target.viewDistance = Dungeon.level.viewDistance;
		Dungeon.observe(DISTANCE + 1);
		super.detach();
	}

	@Override
	public int icon() {
		return BuffIndicator.LIGHT;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}
}
