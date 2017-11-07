
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.items.artifacts.CloakOfShadows;
import com.github.epd.sprout.items.artifacts.TimekeepersHourglass;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;

public class Invisibility extends FlavourBuff {

	public static final float DURATION = 20f;

	@Override
	public boolean attachTo(Char target) {
		if (super.attachTo(target)) {
			target.invisible++;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void detach() {
		if (target.invisible > 0) target.invisible--;
		super.detach();
	}

	@Override
	public int icon() {
		return BuffIndicator.INVISIBLE;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	public static void dispel() {
		Invisibility buff = Dungeon.hero.buff(Invisibility.class);
		if (buff != null) {
			buff.detach();
		}
		CloakOfShadows.cloakStealth cloakBuff = Dungeon.hero
				.buff(CloakOfShadows.cloakStealth.class);
		if (cloakBuff != null) {
			cloakBuff.act();
			cloakBuff.detach();
		}
		// this isn't a form of invisibilty, but it is meant to dispel at the
		// same time as it.
		TimekeepersHourglass.timeFreeze timeFreeze = Dungeon.hero
				.buff(TimekeepersHourglass.timeFreeze.class);
		if (timeFreeze != null) {
			timeFreeze.detach();
		}
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}
}
