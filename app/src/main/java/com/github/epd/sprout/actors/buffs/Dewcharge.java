
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;

public class Dewcharge extends FlavourBuff {

	public static final float DURATION = 50f;

	@Override
	public int icon() {
		return BuffIndicator.DEWCHARGE;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", Dungeon.hero.buff(Dewcharge.class).dispTurnsInt());
	}

}
