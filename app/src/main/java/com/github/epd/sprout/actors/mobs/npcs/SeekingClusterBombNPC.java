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
package com.github.epd.sprout.actors.mobs.npcs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.items.ClusterBomb;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.SeekingBombSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class SeekingClusterBombNPC extends NPC {

	{
		name = Messages.get(SeekingBombNPC.class, "name");
		spriteClass = SeekingBombSprite.class;

		state = HUNTING;

	}

	private static final float SPAWN_DELAY = 0.1f;


	@Override
	public int attackSkill(Char target) {
		return 99;
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		int dmg = super.attackProc(enemy, damage);

		ClusterBomb cbomb = new ClusterBomb();
		for (int n : PathFinder.NEIGHBOURS8DIST2) {
			int c = pos + n;
			if (Random.Int(3) == 0) {
				cbomb.explode(c);
				//spend(2f);
			}
		}

		yell(Messages.get(SeekingBombNPC.class, "yelltwo"));


		destroy();
		sprite.die();

		return dmg;
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

	@Override
	public String description() {
		return Messages.get(SeekingBombNPC.class, "desc");
	}


	@Override
	public boolean interact() {

		int curPos = pos;

		moveSprite(pos, Dungeon.hero.pos);
		move(Dungeon.hero.pos);

		Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
		Dungeon.hero.move(curPos);

		Dungeon.hero.spend(1 / Dungeon.hero.speed());
		Dungeon.hero.busy();

		return true;
	}

	public static SeekingClusterBombNPC spawnAt(int pos) {

		SeekingClusterBombNPC b = new SeekingClusterBombNPC();

		b.pos = pos;
		b.state = b.HUNTING;
		GameScene.add(b, SPAWN_DELAY);

		return b;

	}

	@Override
	public void die(Object cause) {
		ClusterBomb cbomb = new ClusterBomb();
		for (int n : PathFinder.NEIGHBOURS8DIST2) {
			int c = pos + n;
			if (Random.Int(3) == 0) {
				cbomb.explode(c);
				//spend(2f);
			}
		}

		yell("KA-BOOM!!! KA-BOOM!!! KA-BOOM!!!");


		super.die(cause);
	}

}