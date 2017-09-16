/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.github.epd.sprout.items.scrolls;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.SpellSprite;
import com.github.epd.sprout.effects.particles.EnergyParticle;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfRecharging extends Scroll {

	{
		initials = 7;
		name = Messages.get(this, "name");
		consumedValue = 10;
	}

	@Override
	protected void doRead() {

		int count = curUser.belongings.charge(true);
		charge(curUser);

		Sample.INSTANCE.play(Assets.SND_READ);
		Invisibility.dispel();

		if (count > 0) {
			GLog.i(Messages.get(this, "surge"));
			SpellSprite.show(curUser, SpellSprite.CHARGE);
		} else {
			GLog.i(Messages.get(this, "surge"));
		}
		setKnown();

		curUser.spendAndNext(TIME_TO_READ);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	public static void charge(Hero hero) {
		hero.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 15);
	}

	@Override
	public int price() {
		return isKnown() ? 40 * quantity : super.price();
	}
}
