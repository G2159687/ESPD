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

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.CorruptGas;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.items.StoneOre;
import com.github.epd.sprout.items.weapon.enchantments.Death;
import com.github.epd.sprout.items.weapon.melee.relic.RelicMeleeWeapon;
import com.github.epd.sprout.items.weapon.missiles.JupitersWraith;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.KupuaSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Kupua extends Mob {
  //Gullin
	
	{
		name = Messages.get(this,"name");
		spriteClass = KupuaSprite.class;

		HP = HT = 550+(adj(0)*Random.NormalIntRange(3, 7));
		defenseSkill = 15+adj(1);
		baseSpeed = 2f;

		EXP = 10;
		maxLvl = 999;

		loot = new StoneOre();
		lootChance = 0.9f;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(70+adj(0), 160+adj(0));
	}
		
	@Override
	public int attackSkill(Char target) {
		return 120+adj(0);
	}

	@Override
	public int dr() {
		return 50+adj(0);
	}

	@Override
	public void damage(int dmg, Object src) {
		
		if(!(src instanceof RelicMeleeWeapon || src instanceof JupitersWraith)){
			int max = Math.round(dmg*.5f);
			dmg = Random.Int(1,max);
		}
		
		if (dmg > HT/8){
		GameScene.add(Blob.seed(pos, 30, CorruptGas.class));
		}
		
		super.damage(dmg, src);
	}
	
	@Override
	public String defenseVerb() {
		return Messages.get(this,"def");
	}

	@Override
	public String description() {
		return Messages.get(this,"desc");
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add(Death.class);
		IMMUNITIES.add(ToxicGas.class);
		IMMUNITIES.add(CorruptGas.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
