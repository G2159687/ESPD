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
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Roots;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.MrDestructoSprite;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Lichen extends Mob {
	

	{
		name = Messages.get(this,"name");
		spriteClass = MrDestructoSprite.class;
		hostile = false;
		state = HUNTING;
		HP = HT= 100;
		defenseSkill = 3;	
		viewDistance = 1;
	}

	
	private static final float SPAWN_DELAY = 0.1f;
	
	@Override
	public int dr() {
		return 5;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange(1, 3);
	}
	
	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(1) == 0) {
			Buff.prolong(enemy, Roots.class, 10);
		}

		return damage;
	}
	
	@Override
	public void move(int step) {		
	}
		
	@Override
	protected Char chooseEnemy() {

		if (enemy == null || !enemy.isAlive()) {
			HashSet<Mob> enemies = new HashSet<Mob>();
			for (Mob mob : Dungeon.level.mobs) {
				if (mob.hostile && Level.fieldOfView[mob.pos]) {
					enemies.add(mob);
				}
			}

			enemy = enemies.size() > 0 ? Random.element(enemies) : null;
		}

		return enemy;
	}

	public static void spawnAroundChance(int pos) {
		for (int n : PathFinder.NEIGHBOURS4) {
			int cell = pos + n;
			if (Level.passable[cell] && Actor.findChar(cell) == null && Random.Float()<0.25f) {
				spawnAt(cell);
				GLog.i(Messages.get(Lichen.class,"spawn"));
			}
		}
	}
		
    public static Lichen spawnAt(int pos) {
		
    	Lichen b = new Lichen();  
    	
			b.pos = pos;
			b.state = b.HUNTING;
			GameScene.add(b, SPAWN_DELAY);

			return b;
     
     }
		
	@Override
	public int attackSkill(Char target) {
		return 20+(Dungeon.depth);
	}

	@Override
	protected float attackDelay() {
		return 0.5f;
	}
		
	
	@Override
	public void beckon(int cell) {
	}
	
	@Override
	public String description() {
		return Messages.get(this,"desc");
	}

	
			
}
