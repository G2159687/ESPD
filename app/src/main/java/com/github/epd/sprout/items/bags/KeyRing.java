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

import com.github.epd.sprout.items.AncientCoin;
import com.github.epd.sprout.items.Bone;
import com.github.epd.sprout.items.BookOfDead;
import com.github.epd.sprout.items.BookOfLife;
import com.github.epd.sprout.items.BookOfTranscendence;
import com.github.epd.sprout.items.CavesKey;
import com.github.epd.sprout.items.CityKey;
import com.github.epd.sprout.items.ConchShell;
import com.github.epd.sprout.items.HallsKey;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.Palantir;
import com.github.epd.sprout.items.PrisonKey;
import com.github.epd.sprout.items.SanChikarah;
import com.github.epd.sprout.items.SanChikarahDeath;
import com.github.epd.sprout.items.SanChikarahLife;
import com.github.epd.sprout.items.SanChikarahTranscend;
import com.github.epd.sprout.items.SewersKey;
import com.github.epd.sprout.items.TenguKey;
import com.github.epd.sprout.items.keys.Key;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class KeyRing extends Bag {

	{
		name = Messages.get(this,"name");
		image = ItemSpriteSheet.KEYRING;

		size = 24;
	}

	@Override
	public boolean grab(Item item) {
		return item instanceof Key
				|| item instanceof CavesKey
				|| item instanceof CityKey
				|| item instanceof TenguKey
				|| item instanceof SewersKey
				|| item instanceof HallsKey
				|| item instanceof PrisonKey
                || item instanceof AncientCoin
                || item instanceof Bone
                || item instanceof BookOfDead
                || item instanceof BookOfLife
                || item instanceof BookOfTranscendence
                || item instanceof ConchShell
                || item instanceof Palantir
                || item instanceof SanChikarah
                || item instanceof SanChikarahDeath
                || item instanceof SanChikarahLife
                || item instanceof SanChikarahTranscend;
	}

	@Override
	public String info() {
		return Messages.get(this,"desc");
	}
}

