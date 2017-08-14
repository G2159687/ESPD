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
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.Fire;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Thief;
import com.github.epd.sprout.effects.particles.ElmoParticle;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.food.ChargrilledMeat;
import com.github.epd.sprout.items.food.MysteryMeat;
import com.github.epd.sprout.items.rings.RingOfElements.Resistance;
import com.github.epd.sprout.items.scrolls.Scroll;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Burning extends Buff implements Hero.Doom {

	private static final float DURATION = 8f;

	private float left;

	private static final String LEFT = "left";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(LEFT, left);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		left = bundle.getFloat(LEFT);
	}

	@Override
	public boolean act() {

		if (target.isAlive()) {

			if (target instanceof Hero) {
				Buff.prolong(target, Light.class, TICK * 1.01f);
			}

			target.damage(Random.Int(1, 5), this);

			if (target instanceof Hero) {

				Hero hero = (Hero) target;
				Item item = hero.belongings.randomUnequipped();
				if (item instanceof Scroll) {

					item = item.detach(hero.belongings.backpack);
					GLog.w(Messages.get(this,"up"), item.toString());

					Heap.burnFX(hero.pos);

				} else if (item instanceof MysteryMeat) {

					item = item.detach(hero.belongings.backpack);
					ChargrilledMeat steak = new ChargrilledMeat();
					if (!steak.collect(hero.belongings.backpack)) {
						Dungeon.level.drop(steak, hero.pos).sprite.drop();
					}
					GLog.w(Messages.get(this,"up"), item.toString());

					Heap.burnFX(hero.pos);

				}

			} else if (target instanceof Thief
					&& ((Thief) target).item instanceof Scroll) {

				((Thief) target).item = null;
				target.sprite.emitter().burst(ElmoParticle.FACTORY, 6);
			}

		} else {
			detach();
		}

		if (Level.flamable[target.pos]) {
			GameScene.add(Blob.seed(target.pos, 4, Fire.class));
		}

		spend(TICK);
		left -= TICK;

		if (left <= 0
				|| Random.Float() > (2 + (float) target.HP / target.HT) / 3
				|| (Level.water[target.pos] && !target.flying)) {

			detach();
		}

		return true;
	}

	public void reignite(Char ch) {
		left = duration(ch);
	}

	@Override
	public int icon() {
		return BuffIndicator.FIRE;
	}

	@Override
	public String toString() {
		return Messages.get(this,"name");
	}

	public static float duration(Char ch) {
		Resistance r = ch.buff(Resistance.class);
		return r != null ? r.durationFactor() * DURATION : DURATION;
	}

	@Override
	public void onDeath() {
		Dungeon.fail(ResultDescriptions.BURNING);
		GLog.n(Messages.get(this,"die"));
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns(left));
	}
}
