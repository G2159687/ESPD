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
package com.github.epd.sprout.plants;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.effects.particles.ShaftParticle;
import com.github.epd.sprout.items.potions.PotionOfHealing;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.utils.Utils;
import com.watabou.utils.Bundle;

public class Sungrass extends Plant {

	private static final String TXT_DESC = Messages.get(Sungrass.class,"desc");

	{
		image = 4;
		plantName = Messages.get(this,"name");
	}

	@Override
	public void activate(Char ch) {
		super.activate(ch);

		if (ch == Dungeon.hero) {
			Buff.affect(ch, Health.class).level = ch.HT;
		}

		if (Dungeon.visible[pos]) {
			CellEmitter.get(pos).start(ShaftParticle.FACTORY, 0.2f, 3);
		}
	}

	@Override
	public String desc() {
		return TXT_DESC;
	}

	public static class Seed extends Plant.Seed {
		{
			plantName = Messages.get(Sungrass.class,"name");

			name = Messages.get(this,"name");
			image = ItemSpriteSheet.SEED_SUNGRASS;

			plantClass = Sungrass.class;
			alchemyClass = PotionOfHealing.class;

			bones = true;
		}

		@Override
		public String desc() {
			return Messages.get(Plant.class,"seeddesc", plantName);
		}	}

	public static class Health extends Buff {

		private static final float STEP = 1f;

		private int pos;
		private int healCurr = 1;
		private int count = 0;
		private int level;

		@Override
		public boolean attachTo(Char target) {
			pos = target.pos;
			return super.attachTo(target);
		}

		@Override
		public boolean act() {
			if (target.pos != pos) {
				detach();
			}
			if (count == 5) {
				if (level <= healCurr * .025 * target.HT) {
					target.HP = Math.min(target.HT, target.HP + level);
					target.sprite.emitter().burst(Speck.factory(Speck.HEALING),
							1);
					detach();
				} else {
					target.HP = Math.min(target.HT, target.HP
							+ (int) (healCurr * .025 * target.HT));
					level -= (healCurr * .025 * target.HT);
					if (healCurr < 6)
						healCurr++;
					target.sprite.emitter().burst(Speck.factory(Speck.HEALING),
							1);
					if (target.HP == target.HT && target instanceof Hero){
						((Hero)target).resting = false;
						}
				}
				count = 1;
			} else {
				count++;
			}
			if (level <= 0)
				detach();
			spend(STEP);
			return true;
		}

		public int absorb(int damage) {
			level -= damage;
			if (level <= 0)
				detach();
			return damage;
		}

		@Override
		public int icon() {
			return BuffIndicator.HEALING;
		}

		@Override
		public String toString() {
			return Utils.format(Messages.get(this,"name", level));
		}

		@Override
		public String desc() { return Messages.get(this,"desc", level); }

		private static final String POS = "pos";
		private static final String HEALCURR = "healCurr";
		private static final String COUNT = "count";
		private static final String LEVEL = "level";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(POS, pos);
			bundle.put(HEALCURR, healCurr);
			bundle.put(COUNT, count);
			bundle.put(LEVEL, level);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			pos = bundle.getInt(POS);
			healCurr = bundle.getInt(HEALCURR);
			count = bundle.getInt(COUNT);
			level = bundle.getInt(LEVEL);

		}
	}
}
