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
package com.github.epd.sprout.items.bags;

import com.github.epd.sprout.items.ActiveMrDestructo;
import com.github.epd.sprout.items.ActiveMrDestructo2;
import com.github.epd.sprout.items.Bomb;
import com.github.epd.sprout.items.ClusterBomb;
import com.github.epd.sprout.items.DizzyBomb;
import com.github.epd.sprout.items.HolyHandGrenade;
import com.github.epd.sprout.items.InactiveMrDestructo;
import com.github.epd.sprout.items.InactiveMrDestructo2;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.OrbOfZot;
import com.github.epd.sprout.items.SeekingBombItem;
import com.github.epd.sprout.items.SeekingClusterBombItem;
import com.github.epd.sprout.items.SmartBomb;
import com.github.epd.sprout.items.scrolls.Scroll;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class ScrollHolder extends Bag {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.HOLDER;

		size = 24;
	}

	@Override
	public boolean grab(Item item) {
		return item instanceof Scroll
				|| item instanceof Bomb
				|| item instanceof DizzyBomb
				|| item instanceof SmartBomb
				|| item instanceof SeekingBombItem
				|| item instanceof ClusterBomb
				|| item instanceof SeekingClusterBombItem
				|| item instanceof ActiveMrDestructo
				|| item instanceof ActiveMrDestructo2
				|| item instanceof InactiveMrDestructo
				|| item instanceof InactiveMrDestructo2
				|| item instanceof OrbOfZot
				|| item instanceof HolyHandGrenade;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
