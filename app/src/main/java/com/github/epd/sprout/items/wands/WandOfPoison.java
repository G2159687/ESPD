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
package com.github.epd.sprout.items.wands;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.actors.buffs.Strength;
import com.github.epd.sprout.effects.MagicMissile;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class WandOfPoison extends Wand {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.WAND_POISON;
	}

	@Override
	protected void onZap(Ballistica bolt) {
		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null) {

			int poisonbase = 5;

			if (Dungeon.hero.buff(Strength.class) != null) {
				poisonbase *= (int) 4f;
				Buff.detach(Dungeon.hero, Strength.class);
			}

			Buff.affect(ch, Poison.class).set(
					Poison.durationFactor(ch) * (poisonbase + level() * 2));

		} else {

			GLog.i(Messages.get(this, "nothing"));

		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.poison(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", 5 + level() * 2);
	}
}
