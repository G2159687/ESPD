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
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.effects.particles.ElmoParticle;
import com.github.epd.sprout.effects.particles.SparkParticle;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.artifacts.CapeOfThorns;
import com.github.epd.sprout.items.artifacts.LloydsBeacon;
import com.github.epd.sprout.items.journalpages.Sokoban3;
import com.github.epd.sprout.items.keys.SkeletonKey;
import com.github.epd.sprout.items.scrolls.ScrollOfPsionicBlast;
import com.github.epd.sprout.items.weapon.enchantments.Death;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.traps.LightningTrap;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.DM300Sprite;
import com.github.epd.sprout.ui.BossHealthBar;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

public class DM300 extends Mob implements Callback {

	private static final float TIME_TO_ZAP = 2f;

	private static final String TXT_LIGHTNING_KILLED = Messages.get(DM300.class, "kill");

	{
		name = Messages.get(this, "name");
		spriteClass = DM300Sprite.class;

		HP = HT = 500;
		EXP = 30;
		defenseSkill = 40;

		loot = new CapeOfThorns().identify();
		lootChance = 0.333f;

		properties.add(Property.BOSS);
	}

	private int bossAlive = 0;
	private int towerAlive = 1;

	@Override
	public int damageRoll() {

		return Random.NormalIntRange(18, 24) * towerAlive;
	}

	@Override
	public int attackSkill(Char target) {
		return 28;
	}

	@Override
	public int dr() {
		return 10 + (4 * towerAlive);
	}

	@Override
	public boolean act() {
		towerAlive = 1;
		for (Mob mob : Dungeon.level.mobs) {

			if (mob instanceof Tower) {
				towerAlive++;
			}
		}

		GameScene.add(Blob.seed(pos, 30, ToxicGas.class));

		return super.act();
	}

	@Override
	protected boolean canAttack(Char enemy) {
		return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}


	@Override
	protected boolean doAttack(Char enemy) {

		if (Level.distance(pos, enemy.pos) <= 1) {

			return super.doAttack(enemy);

		} else {

			boolean visible = Level.fieldOfView[pos]
					|| Level.fieldOfView[enemy.pos];
			if (visible) {
				sprite.zap(enemy.pos);
			}

			spend(TIME_TO_ZAP);

			if (hit(this, enemy, true)) {
				int dmg = Random.Int(10, 22);
				if (Level.water[enemy.pos] && !enemy.flying) {
					dmg *= 1.5f;
				}
				enemy.damage(dmg, LightningTrap.LIGHTNING);

				enemy.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
				enemy.sprite.flash();

				if (enemy == Dungeon.hero) {

					Camera.main.shake(2, 0.3f);

					if (!enemy.isAlive()) {
						Dungeon.fail(Utils.format(ResultDescriptions.MOB,
								Utils.indefinite(name)));
						GLog.n(TXT_LIGHTNING_KILLED, name);
					}
				}
			} else {
				enemy.sprite
						.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
			}

			return !visible;
		}
	}


	@Override
	public void move(int step) {
		super.move(step);

		if (Dungeon.level.map[step] == Terrain.INACTIVE_TRAP && HP < HT) {

			HP += Random.Int(1, HT - HP);
			sprite.emitter().burst(ElmoParticle.FACTORY, 5);

			if (Dungeon.visible[step] && Dungeon.hero.isAlive()) {
				GLog.n(Messages.get(DM300.class, "heal"));
			}
		}

		int[] cells = {step - 1, step + 1, step - Level.getWidth(),
				step + Level.getWidth(), step - 1 - Level.getWidth(),
				step - 1 + Level.getWidth(), step + 1 - Level.getWidth(),
				step + 1 + Level.getWidth()};
		int cell = cells[Random.Int(cells.length)];

		if (Dungeon.visible[cell]) {
			CellEmitter.get(cell).start(Speck.factory(Speck.ROCK), 0.07f, 10);
			Camera.main.shake(3, 0.7f);
			Sample.INSTANCE.play(Assets.SND_ROCKS);

			if (Level.water[cell]) {
				GameScene.ripple(cell);
			} else if (Dungeon.level.map[cell] == Terrain.EMPTY) {
				Level.set(cell, Terrain.EMPTY_DECO);
				GameScene.updateMap(cell);
			}
		}

		Char ch = Actor.findChar(cell);
		if (ch != null && ch != this) {
			Buff.prolong(ch, Paralysis.class, 2);
		}
	}

	@Override
	public void die(Object cause) {

		super.die(cause);

		for (Mob mob : Dungeon.level.mobs) {

			if (mob instanceof Tower) {
				bossAlive++;
			}
		}

		if (bossAlive == 0) {
			GameScene.bossSlain();
			Dungeon.level.drop(new SkeletonKey(Dungeon.depth), pos).sprite.drop();
		}

		LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
		if (beacon != null) {
			beacon.upgrade();
			GLog.p(Messages.get(LloydsBeacon.class, "stronger"));
		}

		Item.autocollect(new Sokoban3(), pos);

		yell(Messages.get(DM300.class, "die"));
	}

	@Override
	public void notice() {
		super.notice();
		BossHealthBar.assignBoss(this);
		yell(Messages.get(DM300.class, "notice"));
	}

	@Override
	public void call() {
		next();
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}


	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

	static {
		RESISTANCES.add(Death.class);
		RESISTANCES.add(ScrollOfPsionicBlast.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(ToxicGas.class);
		IMMUNITIES.add(Terror.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
	}
}
