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
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Strength;
import com.github.epd.sprout.effects.Beam;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.PurpleParticle;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WandOfDisintegration extends Wand {

	{
		name = Messages.get(this, "name");
		collisionProperties = Ballistica.WONT_STOP;
		image = ItemSpriteSheet.WAND_DISINTEGRATION;
	}

	@Override
	protected void onZap(Ballistica beam) {

		boolean terrainAffected = false;

		int level = level();

		int maxDistance = Math.min(distance(), beam.dist);

		ArrayList<Char> chars = new ArrayList<Char>();

		int terrainPassed = 2, terrainBonus = 0;

		for (int c : beam.subPath(1, maxDistance)) {

			//we don't want to count passed terrain after the last enemy hit. That would be a lot of bonus levels.
			//terrainPassed starts at 2, equivalent of rounding up when /3 for integer arithmetic.
			terrainBonus += terrainPassed / 3;
			terrainPassed = 1;

			Char ch;
			if ((ch = Actor.findChar(c)) != null) {
				chars.add(ch);
			}

			if (Level.flamable[c]) {

				Level.set(c, Terrain.EMBERS);
				GameScene.updateMap(c);
				terrainAffected = true;

			}

			if (!Level.passable[c])
				terrainPassed++;

			CellEmitter.center(c).burst(PurpleParticle.BURST,
					Random.IntRange(1, 2));
		}

		if (terrainAffected) {
			Dungeon.observe();
		}

		int lvl = level + chars.size() + terrainBonus;
		int dmgMin = lvl;
		int dmgMax = 8 + lvl * 4;
		if (Dungeon.hero.buff(Strength.class) != null) {
			dmgMin *= (int) 4f;
			dmgMax *= (int) 4f;
			Buff.detach(Dungeon.hero, Strength.class);
		}
		for (Char ch : chars) {
			ch.damage(Random.NormalIntRange(dmgMin, dmgMax), this);
			ch.sprite.centerEmitter().burst(PurpleParticle.BURST,
					Random.IntRange(1, 2));
			ch.sprite.flash();
		}
	}

	private int distance() {
		return level() + 4;
	}

	@Override
	protected void fx(Ballistica beam, Callback callback) {

		int cell = beam.path.get(Math.min(beam.dist, distance()));
		curUser.sprite.parent.add(new Beam.DeathRay(curUser.sprite.center(),
				DungeonTilemap.tileCenterToWorld(cell)));
		callback.call();
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", level, 8 + level() * level() / 3);
	}

	@Override
	public void proc(Char attacker, Char defender, int damage) {
		if (defender.isAlive())
			if (level > Random.IntRange(0, 50)) {
				attacker.sprite.parent.add(new Beam.DeathRay(attacker.sprite.center(), DungeonTilemap.tileCenterToWorld(defender.pos)));
				defender.damage(Random.NormalIntRange(level, 2 * level), this);
			}
	}
}
