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
package com.github.epd.sprout.items.potions;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.ConfusionGas;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Levitation;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class PotionOfLevitation extends Potion {

	{
		initials = 4;
		name = Messages.get(this, "name");
	}

	private static final String TXT_PREVENTING = Messages.get(PotionOfLevitation.class, "prevent");

	@Override
	public void shatter(int cell) {

		if (Dungeon.visible[cell]) {
			setKnown();

			splash(cell);
			Sample.INSTANCE.play(Assets.SND_SHATTER);
		}

		GameScene.add(Blob.seed(cell, 1000, ConfusionGas.class));
	}

	@Override
	public void apply(Hero hero) {
		setKnown();
		Buff.affect(hero, Levitation.class, Levitation.DURATION);
		GLog.i(Messages.get(this, "float"));
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public void execute(final Hero hero, String action) {
		if (action.equals(AC_DRINK)) {

			if (Dungeon.depth > 50) {
				GLog.w(TXT_PREVENTING);
				return;
			}
		}

		super.execute(hero, action);

	}

	@Override
	public int price() {
		return isKnown() ? 35 * quantity : super.price();
	}
}
