
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;

public class Blindness extends FlavourBuff {

	@Override
	public void detach() {
		super.detach();
		Dungeon.observe();
	}

	@Override
	public int icon() {
		return BuffIndicator.BLINDNESS;
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
