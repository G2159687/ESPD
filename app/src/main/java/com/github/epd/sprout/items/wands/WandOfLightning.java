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

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Lightning;
import com.github.epd.sprout.effects.particles.SparkParticle;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.traps.LightningTrap;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WandOfLightning extends Wand {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.WAND_LIGHTNING;
	}

	private ArrayList<Char> affected = new ArrayList<>();

	ArrayList<Lightning.Arc> arcs = new ArrayList<>();

	@Override
	protected void onZap(Ballistica bolt) {

		//lightning deals less damage per-target, the more targets that are hit.
		float multiplier = (0.6f + 0.4f * affected.size()) / affected.size();
		if (Level.water[bolt.collisionPos]) multiplier *= 1.5f;

		int min = 5 + level;
		int max = Math.round(10 + (level * level / 4f));

		for (Char ch : affected) {
			ch.damage(Math.round(Random.NormalIntRange(min, max) * multiplier), LightningTrap.LIGHTNING);

			if (ch == Dungeon.hero) Camera.main.shake(2, 0.3f);
			ch.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
			ch.sprite.flash();
		}

		if (!curUser.isAlive()) {
			Dungeon.fail(Utils.format(ResultDescriptions.ITEM, name));
			GLog.n(Messages.get(this, "kill"));
		}
	}

	private void arc(Char ch) {

		affected.add(ch);

		for (int i : PathFinder.NEIGHBOURS8) {
			int cell = ch.pos + i;

			Char n = Actor.findChar(cell);
			if (n != null && !affected.contains(n)) {
				arcs.add(new Lightning.Arc(ch.pos, n.pos));
				arc(n);
			}
		}

		if (Level.water[ch.pos] && !ch.flying) {
			for (int i : PathFinder.NEIGHBOURS8DIST2) {
				int cell = ch.pos + i;
				//player can only be hit by lightning from an adjacent enemy.
				if (!Dungeon.level.insideMap(cell) || Actor.findChar(cell) == Dungeon.hero)
					continue;

				Char n = Actor.findChar(ch.pos + i);
				if (n != null && !affected.contains(n)) {
					arcs.add(new Lightning.Arc(ch.pos, n.pos));
					arc(n);
				}
			}
		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {

		affected.clear();
		arcs.clear();
		arcs.add(new Lightning.Arc(bolt.sourcePos, bolt.collisionPos));

		int cell = bolt.collisionPos;

		Char ch = Actor.findChar(cell);
		if (ch != null) {
			arc(ch);
		} else {
			CellEmitter.center(cell).burst(SparkParticle.FACTORY, 3);
		}

		//don't want to wait for the effect before processing damage.
		curUser.sprite.parent.add(new Lightning(arcs, null));
		callback.call();
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", 5 + level, Math.round(10 + (level * level / 4f)));
	}
}
