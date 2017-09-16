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

import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Amok;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.actors.buffs.Charm;
import com.github.epd.sprout.actors.buffs.Sleep;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.actors.buffs.Vertigo;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.OrbOfZot;
import com.github.epd.sprout.items.scrolls.ScrollOfPsionicBlast;
import com.github.epd.sprout.items.weapon.enchantments.Death;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ShadowYogSprite;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

import java.util.HashSet;

public class ShadowYog extends Mob {

	{
		name = Messages.get(this, "name");
		spriteClass = ShadowYogSprite.class;

		HP = HT = 50 * Dungeon.hero.lvl;

		baseSpeed = 2f;
		defenseSkill = 32;

		EXP = 100;

		state = PASSIVE;
	}

	private int yogsAlive = 0;

	private static final String TXT_DESC = Messages.get(ShadowYog.class, "desc");

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(45, 125);
	}

	@Override
	public int attackSkill(Char target) {
		return 50;
	}

	public ShadowYog() {
		super();
	}

	@Override
	public int dr() {
		return (Dungeon.level.mobs.size());
	}

	@Override
	public void damage(int dmg, Object src) {

		//for (Mob mob : Dungeon.level.mobs) {
		//	mob.beckon(pos);
		//	}

		for (int i = 0; i < 4; i++) {
			int trapPos;
			do {
				trapPos = Random.Int(Level.getLength());
			} while (!Level.fieldOfView[trapPos] || !Level.passable[trapPos]);

			if (Dungeon.level.map[trapPos] == Terrain.INACTIVE_TRAP) {
				Level.set(trapPos, Terrain.SECRET_SUMMONING_TRAP);
				GameScene.updateMap(trapPos);
			}
		}

		if (HP < (HT / 8) && Random.Float() < 0.5f) {
			int newPos = -1;
			for (int i = 0; i < 20; i++) {
				newPos = Dungeon.level.randomRespawnCellMob();
				if (newPos != -1) {
					break;
				}
			}
			if (newPos != -1) {
				Actor.freeCell(pos);
				CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
				pos = newPos;
				sprite.place(pos);
				sprite.visible = Dungeon.visible[pos];
				GLog.n(Messages.get(this, "vanish"));
			}
			if (Dungeon.level.mobs.size() < Dungeon.hero.lvl * 2) {
				SpectralRat.spawnAroundChance(newPos);
			}
		}

		super.damage(dmg, src);
	}

	@Override
	public int defenseProc(Char enemy, int damage) {
		return super.defenseProc(enemy, damage);
	}


	@Override
	public void beckon(int cell) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void die(Object cause) {
		super.die(cause);

		if (Dungeon.isChallenged(Challenges.NO_HERBALISM)) {
			Dungeon.level.drop(new OrbOfZot(), pos).sprite.drop();
		}

		Statistics.shadowYogsKilled++;

		for (Mob mob : Dungeon.level.mobs) {

			if (mob instanceof ShadowYog) {
				yogsAlive++;
			}
		}

		if (yogsAlive == 0) {
			GameScene.bossSlain();
			Dungeon.shadowyogkilled = true;

			Dungeon.level.drop(new OrbOfZot(), pos).sprite.drop();

			for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
				if (mob instanceof Rat || mob instanceof GreyOni || mob instanceof SpectralRat || mob instanceof Eye) {
					mob.die(cause);
				}
			}

			yell(Messages.get(this, "die"));
		}
	}

	@Override
	public void notice() {
		super.notice();
		yell(Messages.get(this, "notice"));
	}

	@Override
	public String description() {
		return TXT_DESC;

	}


	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {

		IMMUNITIES.add(Death.class);
		IMMUNITIES.add(Terror.class);
		IMMUNITIES.add(Amok.class);
		IMMUNITIES.add(Charm.class);
		IMMUNITIES.add(Sleep.class);
		IMMUNITIES.add(Burning.class);
		IMMUNITIES.add(ToxicGas.class);
		IMMUNITIES.add(ScrollOfPsionicBlast.class);
		IMMUNITIES.add(Vertigo.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}

}
