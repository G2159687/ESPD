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
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.buffs.Blindness;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class ScrollOfPsionicBlast extends Scroll {

	{
		initials = 5;
		name = Messages.get(this, "name");
		consumedValue = 10;

		bones = true;
	}

	@Override
	protected void doRead() {

		GameScene.flash(0xFFFFFF);

		Sample.INSTANCE.play(Assets.SND_BLAST);
		Invisibility.dispel();

		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (Level.fieldOfView[mob.pos] && mob.hostile) {
				mob.damage(mob.HT, this);
			}
		}

		curUser.damage(Math.max(curUser.HT / 5, curUser.HP / 2), this);
		Buff.prolong(curUser, Paralysis.class, Random.Int(4, 6));
		Buff.prolong(curUser, Blindness.class, Random.Int(6, 9));
		Dungeon.observe();

		setKnown();

		curUser.spendAndNext(TIME_TO_READ);

		if (!checkOriginalGenMobs() &&
				!Dungeon.level.cleared && Dungeon.depth > 2 && Dungeon.depth < 25 && !Dungeon.bossLevel(Dungeon.depth)
				) {
			Dungeon.level.cleared = true;
			GameScene.levelCleared();
			if (Dungeon.depth > 0) {
				Statistics.prevfloormoves = Math.max(Dungeon.pars[Dungeon.depth] - Dungeon.level.currentmoves, 0);
				if (Statistics.prevfloormoves > 1) {
					GLog.h(Messages.get(Mob.class, "draw1", Statistics.prevfloormoves));
				} else if (Statistics.prevfloormoves == 1) {
					GLog.h(Messages.get(Mob.class, "draw2"));
				} else if (Statistics.prevfloormoves == 0) {
					GLog.h(Messages.get(Mob.class, "draw3"));
				}
			}
		}

		if (!curUser.isAlive()) {
			Dungeon.fail(Utils.format(ResultDescriptions.ITEM, name));
			GLog.n(Messages.get(this, "ondeath"));
		}
	}

	public boolean checkOriginalGenMobs() {
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (mob.originalgen) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public int price() {
		return isKnown() ? 80 * quantity : super.price();
	}
}
