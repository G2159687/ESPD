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

import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.artifacts.Artifact;
import com.github.epd.sprout.items.artifacts.HornOfPlenty;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndBag;

public class ScrollOfUpgrade extends InventoryScroll {

	private static final String TXT_LOOKS_BETTER = Messages.get(ScrollOfUpgrade.class, "looks_better");

	{
		initials = 11;
		name = Messages.get(this, "name");
		inventoryTitle = Messages.get(this, "inv_title");
		mode = WndBag.Mode.UPGRADEABLE;
		consumedValue = 15;

		bones = true;
	}

	@Override
	protected void onItemSelected(Item item) {

		ScrollOfRemoveCurse.uncurse(Dungeon.hero, item);

		if (item instanceof Artifact) {
			if (item.level < ((Artifact) item).levelCap || item.level >= 50) {
				GLog.w(Messages.get(this, "cannot"));
				new ScrollOfUpgrade().collect();
				return;
			}
		}

		if (Dungeon.isChallenged(Challenges.DARKNESS)) {
			item.upgrade(10);
		} else {
			item.upgrade();
		}

		upgrade(curUser);
		GLog.p(TXT_LOOKS_BETTER, item.name());

		if (item instanceof Artifact && item.level > 50){
			item.level = 50;
		}
	}

	public static void upgrade(Hero hero) {
		hero.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}


	@Override
	public int price() {
		return 100 * quantity;
	}
}
