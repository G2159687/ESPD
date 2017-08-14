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
import com.github.epd.sprout.actors.buffs.Amok;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.actors.buffs.Charm;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.actors.buffs.Sleep;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.actors.buffs.Vertigo;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.HallsKey;
import com.github.epd.sprout.items.scrolls.ScrollOfPsionicBlast;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.items.weapon.Weapon.Enchantment;
import com.github.epd.sprout.items.weapon.enchantments.Death;
import com.github.epd.sprout.items.weapon.enchantments.Leech;
import com.github.epd.sprout.items.weapon.melee.MeleeWeapon;
import com.github.epd.sprout.items.weapon.melee.relic.RelicMeleeWeapon;
import com.github.epd.sprout.items.weapon.missiles.JupitersWraith;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.SentinelSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class MineSentinel extends Mob {
	

	{
		name = Messages.get(this,"name");
		spriteClass = SentinelSprite.class;

		EXP = 25;
		state = PASSIVE;
	}
	private static final int REGENERATION = 100;

	private Weapon weapon;

	public MineSentinel() {
		super();

		do {
			weapon = (Weapon) Generator.random(Generator.Category.WEAPON);
		} while (!(weapon instanceof MeleeWeapon) || weapon.level < 0);

		weapon.identify();
		weapon.enchant(Enchantment.randomLow());
		weapon.upgrade(10);
		

		HP = HT = 500 + Dungeon.depth * 20;
		//HP = HT = 5;
		defenseSkill = 20+adj(1);
		//defenseSkill = 2;
	}

	private static final String WEAPON = "weapon";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(WEAPON, weapon);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		weapon = (Weapon) bundle.get(WEAPON);
	}

	@Override
	protected boolean act() {
		
		Hero hero = Dungeon.hero;
		ArrayList<Integer> spawnPoints = new ArrayList<Integer>();
		
		
		if(state==HUNTING){
			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				int p = pos + PathFinder.NEIGHBOURS8[i];
				Char ch = Actor.findChar(p);
				if (ch != null && ch instanceof MineSentinel &&  Random.Int(10)<2) {
					ch.damage(1, this);
					if (((Mob)ch).state==PASSIVE){
						((Mob)ch).state = HUNTING;
					}
				 break;
				}
			}
			
		}
		
		if (!heroNear() && Random.Float() < 0.50f && state==HUNTING){
			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				int p = hero.pos + PathFinder.NEIGHBOURS8[i];
				if (Actor.findChar(p) == null
						&& (Level.passable[p] || Level.avoid[p])) {
					spawnPoints.add(p);
				}
			}
			
			if (spawnPoints.size() > 0) {
				int newPos;
				newPos=Random.element(spawnPoints);
				Actor.freeCell(pos);
				CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
				pos = newPos;
				sprite.place(pos);
				sprite.visible = Dungeon.visible[pos];
			}
			
		} else {
		
		 if (HP<(HT/4) && Random.Float() < 0.50f && state!=PASSIVE){
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
				HP += REGENERATION;
			}					
			
		 }
		}
		return super.act();
	}
	
	protected boolean heroNear (){
		boolean check=false;
		for (int i : PathFinder.NEIGHBOURS9DIST2){
			int cell=pos+i;
			if (Actor.findChar(cell) != null	
				&& (Actor.findChar(cell) instanceof Hero)
				){
				check=true;
			}			
		}		
		return check;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(weapon.MIN*2, weapon.MAX*2);
	}

	@Override
	public int attackSkill(Char target) {
		return (int) ((30 + Dungeon.depth*2) * weapon.ACU);
	}

	@Override
	protected float attackDelay() {
		return weapon.DLY;
	}

	@Override
	public int dr() {
		//return Dungeon.depth*3;
		return 3;
	}

	@Override
	public void damage(int dmg, Object src) {

		if (state == PASSIVE) {
			state = HUNTING;
		}
		
		if(!(src instanceof RelicMeleeWeapon || src instanceof JupitersWraith)){
			int max = Math.round(dmg*.25f);
			dmg = Random.Int(1,max);
		}

		super.damage(dmg, src);
	}
	

	@Override
	public int attackProc(Char enemy, int damage) {
		weapon.proc(this, enemy, damage);
		return damage;
	}

	@Override
	public void beckon(int cell) {
		// Do nothing
	}

	@Override
	public void die(Object cause) {
		Dungeon.level.drop(weapon, pos).sprite.drop();
		if (!Dungeon.limitedDrops.hallskey.dropped() && Dungeon.depth==24) {
			Dungeon.limitedDrops.hallskey.drop();
			Dungeon.level.drop(new HallsKey(), pos).sprite.drop();
			explodeDew(pos);				
		} 
		super.die(cause);
	}

		@Override
	public boolean reset() {
		state = PASSIVE;
		return true;
	}

	@Override
	public String description() {
		return Messages.get(this,"desc", weapon.name());
	}

	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add(ToxicGas.class);
		RESISTANCES.add(Poison.class);
		RESISTANCES.add(Death.class);
		IMMUNITIES.add(Leech.class);
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
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
