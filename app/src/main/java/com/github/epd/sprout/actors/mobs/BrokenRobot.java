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
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Light;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.BlastParticle;
import com.github.epd.sprout.effects.particles.PurpleParticle;
import com.github.epd.sprout.effects.particles.SmokeParticle;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.RedDewdrop;
import com.github.epd.sprout.items.scrolls.ScrollOfRecharging;
import com.github.epd.sprout.items.weapon.enchantments.Death;
import com.github.epd.sprout.items.weapon.enchantments.Leech;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.BrokenRobotSprite;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class BrokenRobot extends Mob {

	private static final String TXT_DEATHGAZE_KILLED = Messages.get(BrokenRobot.class, "kill");
	private static final float SPAWN_DELAY = 2f;

	{
		name = Messages.get(this, "name");
		spriteClass = BrokenRobotSprite.class;

		HP = HT = 75 + (adj(0) * Random.NormalIntRange(4, 7));
		defenseSkill = 20 + adj(1);
		viewDistance = Light.DISTANCE;

		EXP = 13;
		maxLvl = 25;


		lootOther = new RedDewdrop();
		lootChanceOther = 0.5f;

		loot = new ScrollOfRecharging();
		lootChance = 0.5f; // by default, see die()
	}

	@Override
	public int dr() {
		return 5;
	}

	@Override
	public boolean act() {

		if (enemySeen) {
			switch (Random.Int(50)) {
				case 1:
					GLog.n(Messages.get(BrokenRobot.class, "broken"));
					explode(pos);
					if (HP < 1) {
						die(null);
					}
					break;
			}
		}

		return super.act();
	}


	private Ballistica beam;

	@Override
	protected boolean canAttack(Char enemy) {
		beam = new Ballistica(pos, enemy.pos, Ballistica.STOP_TERRAIN);
		return beam.subPath(1, beam.dist).contains(enemy.pos);
	}

	@Override
	public int attackSkill(Char target) {
		return 20 + adj(0);
	}

	@Override
	protected float attackDelay() {
		return 3f;
	}

	@Override
	protected boolean doAttack(Char enemy) {

		spend(attackDelay());

		boolean rayVisible = false;

		for (int i : beam.subPath(0, beam.dist)) {
			if (Dungeon.visible[i]) {
				rayVisible = true;
			}
		}

		if (rayVisible) {
			sprite.attack(beam.collisionPos);
			return false;
		} else {
			attack(enemy);
			return true;
		}
	}

	@Override
	public boolean attack(Char enemy) {

		for (int pos : beam.subPath(1, beam.dist)) {

			Char ch = Actor.findChar(pos);
			if (ch == null) {
				continue;
			}

			if (hit(this, ch, true)) {
				ch.damage(Random.NormalIntRange(10, 12 + adj(0)), this);

				if (Dungeon.visible[pos]) {
					ch.sprite.flash();
					CellEmitter.center(pos).burst(PurpleParticle.BURST,
							Random.IntRange(1, 2));
				}

				if (!ch.isAlive() && ch == Dungeon.hero) {
					Dungeon.fail(Utils.format(ResultDescriptions.MOB,
							Utils.indefinite(name)));
					GLog.n(TXT_DEATHGAZE_KILLED, name);
				}
			} else {
				ch.sprite.showStatus(CharSprite.NEUTRAL, ch.defenseVerb());
			}
		}

		return true;
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}

	public static void spawnAround(int pos) {
		for (int n : PathFinder.NEIGHBOURS4) {
			int cell = pos + n;
			if (Level.passable[cell] && Actor.findChar(cell) == null) {
				spawnAt(cell);
			}
		}
	}

	public static BrokenRobot spawnAt(int pos) {

		BrokenRobot b = new BrokenRobot();

		b.pos = pos;
		b.state = b.HUNTING;
		GameScene.add(b, SPAWN_DELAY);

		return b;

	}


	public void explode(int cell) {
		// We're blowing up, so no need for a fuse anymore.

		Sample.INSTANCE.play(Assets.SND_BLAST, 2);

		if (Dungeon.visible[cell]) {
			CellEmitter.center(cell).burst(BlastParticle.FACTORY, 30);
		}

		boolean terrainAffected = false;
		for (int n : PathFinder.NEIGHBOURS9) {
			int c = cell + n;
			if (c >= 0 && c < Level.getLength()) {
				if (Dungeon.visible[c]) {
					CellEmitter.get(c).burst(SmokeParticle.FACTORY, 4);
				}

				if (Level.flamable[c]) {
					Level.set(c, Terrain.EMBERS);
					GameScene.updateMap(c);
					terrainAffected = true;
				}

				// destroys items / triggers bombs caught in the blast.
				Heap heap = Dungeon.level.heaps.get(c);
				if (heap != null)
					heap.explode();

				Char ch = Actor.findChar(c);
				if (ch != null) {
					// those not at the center of the blast take damage less
					// consistently.
					int minDamage = c == cell ? Dungeon.depth + 5 : 1;
					int maxDamage = 10 + Dungeon.depth * 2;

					int dmg = Random.NormalIntRange(minDamage, maxDamage)
							- Random.Int(ch.dr());
					if (dmg > 0) {
						ch.damage(dmg, this);
					}

					if (ch == this && HP < 1) {
						die(this);
						if (sprite != null)
							sprite.killAndErase();
					}
					if (ch == Dungeon.hero && !ch.isAlive())
						// constant is used here in the rare instance a player
						// is killed by a double bomb.
						Dungeon.fail(Utils.format(ResultDescriptions.ITEM,
								"bomb"));
				}
			}
		}

		if (terrainAffected) {
			Dungeon.observe();
		}
	}

	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

	static {
		RESISTANCES.add(Death.class);
		RESISTANCES.add(Leech.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(Terror.class);
		IMMUNITIES.add(ToxicGas.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
