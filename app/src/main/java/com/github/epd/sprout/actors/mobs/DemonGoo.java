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

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.actors.buffs.Light;
import com.github.epd.sprout.actors.buffs.Ooze;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.actors.buffs.Roots;
import com.github.epd.sprout.effects.Pushing;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.potions.PotionOfMending;
import com.github.epd.sprout.items.scrolls.ScrollOfPsionicBlast;
import com.github.epd.sprout.items.weapon.enchantments.Death;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.features.Door;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.DemonGooSprite;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class DemonGoo extends Mob {
	
protected static final float SPAWN_DELAY = 2f;

private int demonGooGeneration = 0;

private static final String DEMONGOOGENERATION = "demonGooGeneration";

	{
		name = Messages.get(this,"name");
		HP = HT = 200+(adj(0)*Random.NormalIntRange(4, 7));
		EXP = 10;
		defenseSkill = 10+adj(1);
		//10
		spriteClass = DemonGooSprite.class;
		baseSpeed = 2f;
		viewDistance = Light.DISTANCE;

		loot = new PotionOfMending();
		lootChance = 1f;
	}

	private static final float SPLIT_DELAY = 1f;	
	
	@Override
	protected boolean act() {
		boolean result = super.act();
		
		if (Level.water[pos] && HP < HT) {
			sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			HP++;
		} else if(Level.water[pos] && HP == HT && HT < 200){
			sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			HT=HT+5;
			HP=HT;
		}
		return result;
	}

		
	@Override
	public int damageRoll() {
			return Random.NormalIntRange(30+adj(1), 60+adj(1));
	}

	@Override
	public int attackSkill(Char target) {
		return 35+adj(1);
	}

	@Override
	public int dr() {
		return 20+adj(1);
		//10
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DEMONGOOGENERATION, demonGooGeneration);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		demonGooGeneration = bundle.getInt(DEMONGOOGENERATION);
	}

	@Override
	public int defenseProc(Char enemy, int damage) {
		
		if (HP >= damage + 2) {
			ArrayList<Integer> candidates = new ArrayList<Integer>();
			boolean[] passable = Level.passable;

			int[] neighbours = { pos + 1, pos - 1, pos + Level.getWidth(),
					pos - Level.getWidth() };
			for (int n : neighbours) {
				if (passable[n] && Actor.findChar(n) == null) {
					candidates.add(n);
				}
			}

			if (candidates.size() > 0) {
				GLog.n(Messages.get(DemonGoo.class,"divide"));
				DemonGoo clone = split();
				clone.HP = (HP - damage) / 2;
				clone.pos = Random.element(candidates);
				clone.state = clone.HUNTING;

				if (Dungeon.level.map[clone.pos] == Terrain.DOOR) {
					Door.enter(clone.pos, clone);
				}

				GameScene.add(clone, SPLIT_DELAY);
				Actor.addDelayed(new Pushing(clone, pos, clone.pos), -1);

				HP -= clone.HP;
			}
		}

		return damage;
	}


	private DemonGoo split() {
		DemonGoo clone = new DemonGoo();
		clone.demonGooGeneration = demonGooGeneration + 1;
		if (buff(Burning.class) != null) {
			Buff.affect(clone, Burning.class).reignite(clone);
		}
		if (buff(Poison.class) != null) {
			Buff.affect(clone, Poison.class).set(2);
		}
		return clone;
	}
	
	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(3) == 0) {
			Buff.affect(enemy, Ooze.class);
			enemy.sprite.burst(0x000000, 5);
		}				
		return damage;
	}

	@Override
	public void notice() {
		super.notice();
		yell(Messages.get(DemonGoo.class,"notice"));
	}

	@Override
	public String description() {
		return Messages.get(this,"desc");
	}

	@Override
	public void die(Object cause) {

		super.die(cause);
       	yell(Messages.get(DemonGoo.class,"die"));
	}
	
	

	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add(ToxicGas.class);
		RESISTANCES.add(Death.class);
		RESISTANCES.add(ScrollOfPsionicBlast.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(Roots.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
		
	
}
