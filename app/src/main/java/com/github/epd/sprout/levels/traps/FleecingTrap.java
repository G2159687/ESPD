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
package com.github.epd.sprout.levels.traps;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokoban;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanBlack;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanCorner;
import com.github.epd.sprout.actors.mobs.npcs.SheepSokobanSwitch;
import com.github.epd.sprout.effects.particles.ShadowParticle;
import com.github.epd.sprout.items.Ankh;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.bags.Bag;
import com.github.epd.sprout.items.scrolls.ScrollOfMagicalInfusion;
import com.github.epd.sprout.items.scrolls.ScrollOfUpgrade;
import com.github.epd.sprout.items.wands.WandOfFlock.Sheep;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.utils.Random;

public class FleecingTrap {

	private static final String name = Messages.get(FleecingTrap.class, "name");

	// 00x66CCEE

	public static void trigger(int pos, Char ch) {

		if (ch instanceof SheepSokoban || ch instanceof SheepSokobanCorner || ch instanceof SheepSokobanSwitch || ch instanceof Sheep || ch instanceof SheepSokobanBlack) {
			Camera.main.shake(2, 0.3f);
			ch.destroy();
			ch.sprite.killAndErase();
			ch.sprite.emitter().burst(ShadowParticle.UP, 5);

		} else if (ch != null) {

			int dmg = ch.HP;
			boolean port = true;

			if (ch == Dungeon.hero) {

				Hero hero = Dungeon.hero;

				Item item = hero.belongings.randomUnequipped();
				Bag bag = hero.belongings.backpack;
				//bags do not protect against this trap
				if (item instanceof Bag) {
					bag = (Bag) item;
					item = Random.element(bag.items);
				}
				if (!(item == null || item.level > 5 || item.unique)) {
					if (!item.stackable) {
						item.detachAll(bag);
						GLog.w(Messages.get(FleecingTrap.class, "one", item.name()));
					} else {
						int n = Random.NormalIntRange(1, (item.quantity() + 1) / 2);
						if (!((item instanceof Ankh || item instanceof ScrollOfUpgrade || item instanceof ScrollOfMagicalInfusion) && item.quantity() > 1)) {
							for (int i = 1; i <= n; i++)
								item.detach(bag);
							GLog.w(Messages.get(FleecingTrap.class, "some", item.name()));
						} else {
							item.detach(bag);
							GLog.w(Messages.get(FleecingTrap.class, "one", item.name()));
						}
					}
					port = false;
				}
			}

			Camera.main.shake(2, 0.3f);
			ch.sprite.emitter().burst(ShadowParticle.UP, 5);

			if (ch == Dungeon.hero && port) {
				Dungeon.hero.damage(Math.round(0.95f * (float)ch.HP), FLEECE);

				if (!Dungeon.hero.isAlive()) {
					Dungeon.fail(Utils.format(ResultDescriptions.TRAP, name));
					GLog.n(Messages.get(FleecingTrap.class, "kill"));
				}
			}
		}

		Dungeon.hero.next();

	}

	public static final Fleece FLEECE = new Fleece();

	public static class Fleece {
	}

}
