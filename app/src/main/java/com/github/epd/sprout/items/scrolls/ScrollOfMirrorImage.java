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
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.actors.mobs.npcs.MirrorImage;
import com.github.epd.sprout.items.wands.WandOfBlink;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

import static com.github.epd.sprout.Dungeon.hero;

public class ScrollOfMirrorImage extends Scroll {

	private static final int NIMAGES = 3;

	{
		initials = 4;
		name = Messages.get(this, "name");
		consumedValue = 5;
	}

	private static final String TXT_PREVENTING = Messages.get(ScrollOfMirrorImage.class, "prevent");

	@Override
	protected void doRead() {

		if (Dungeon.depth > 50) {
			GLog.w(TXT_PREVENTING);
			Sample.INSTANCE.play(Assets.SND_READ);
			Invisibility.dispel();

			setKnown();

			curUser.spendAndNext(TIME_TO_READ);
			return;
		}

		ArrayList<Integer> respawnPoints = new ArrayList<Integer>();

		for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
			int p = curUser.pos + PathFinder.NEIGHBOURS8[i];
			if (Actor.findChar(p) == null
					&& (Level.passable[p] || Level.avoid[p])) {
				respawnPoints.add(p);
			}
		}

		int nImages = NIMAGES;
		while (nImages > 0 && respawnPoints.size() > 0) {
			int index = Random.index(respawnPoints);

			MirrorImage mob = new MirrorImage();
			mob.duplicate(hero);
			GameScene.add(mob);
			WandOfBlink.appear(mob, respawnPoints.get(index));

			respawnPoints.remove(index);
			nImages--;
		}

		if (nImages < NIMAGES) {
			setKnown();
		}

		Sample.INSTANCE.play(Assets.SND_READ);
		Invisibility.dispel();

		curUser.spendAndNext(TIME_TO_READ);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
