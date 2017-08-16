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
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Strength;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Lightning;
import com.github.epd.sprout.effects.LightningLarge;
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
import java.util.HashSet;

public class WandOfLightning extends Wand {

	{
		name = Messages.get(this,"name");
		image = ItemSpriteSheet.WAND_LIGHTNING;
	}

	private ArrayList<Char> affected = new ArrayList<Char>();

	private int[] points = new int[20];
	private int nPoints;

	@Override
	protected void onZap(Ballistica bolt) {
		// Everything is processed in fx() method
		if (!curUser.isAlive()) {
			Dungeon.fail(Utils.format(ResultDescriptions.ITEM, name));
			GLog.n(Messages.get(this,"kill"));
		}
	}

	private void hit(Char ch, int damage) {

		if (damage < 1) {
			return;
		}

		if (ch == Dungeon.hero) {
			Camera.main.shake(2, 0.3f);
		}

		affected.add(ch);
		if (Dungeon.hero.buff(Strength.class) != null){ damage *= (int) 4f; Buff.detach(Dungeon.hero, Strength.class);}
		ch.damage(Level.water[ch.pos] && !ch.flying ? damage * 2
				: damage, LightningTrap.LIGHTNING);

		ch.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
		ch.sprite.flash();

		points[nPoints++] = ch.pos;

		HashSet<Char> ns = new HashSet<Char>();
		for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
			Char n = Actor.findChar(ch.pos + PathFinder.NEIGHBOURS8[i]);
			if (n != null && !affected.contains(n)) {
				ns.add(n);
			}
		}

		if (ns.size() > 0) {
			hit(Random.element(ns), Random.Int(damage / 2, damage));
		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {

		nPoints = 0;
		points[nPoints++] = Dungeon.hero.pos;

		int cell = bolt.collisionPos;

		Char ch = Actor.findChar(cell);
		if (ch != null) {

			affected.clear();
			int lvl = level();
			hit(ch, Random.Int(5 + lvl / 2, 10 + lvl));

		} else {

			points[nPoints++] = cell;
			CellEmitter.center(cell).burst(SparkParticle.FACTORY, 3);

		}
		if(Random.Int(10)<5){
		curUser.sprite.parent.add(new Lightning(points, nPoints, callback));
		} else {
			curUser.sprite.parent.add(new LightningLarge(points, nPoints, callback));	
		}
	}

	@Override
	public String desc() {
		return Messages.get(this,"desc");
	}
}
