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
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Amok;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.actors.mobs.Mimic;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfRage extends Scroll {

	{
		initials = 6;
		name = Messages.get(this, "name");
		consumedValue = 5;
	}

	@Override
	protected void doRead() {

		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			mob.beckon(curUser.pos);
			if (Level.fieldOfView[mob.pos]) {
				Buff.prolong(mob, Amok.class, 5f);
			}
		}

		for (Heap heap : Dungeon.level.heaps.values()) {
			if (heap.type == Heap.Type.MIMIC) {
				Mimic m = Mimic.spawnAt(heap.pos, heap.items);
				if (m != null) {
					m.beckon(curUser.pos);
					heap.destroy();
				}
			}
		}

		GLog.w(Messages.get(this, "roar"));
		setKnown();

		curUser.sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.3f,
				3);
		Sample.INSTANCE.play(Assets.SND_CHALLENGE);
		Invisibility.dispel();

		curUser.spendAndNext(TIME_TO_READ);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
