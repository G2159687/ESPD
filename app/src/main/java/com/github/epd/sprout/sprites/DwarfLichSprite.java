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
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.actors.mobs.RedWraith;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class DwarfLichSprite extends MobSprite {

	public DwarfLichSprite() {
		super();

		texture(Assets.DWARFLICH);

		TextureFilm frames = new TextureFilm(texture, 12, 16);

		idle = new Animation(12, true);
		idle.frames(frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3);

		run = new Animation(15, true);
		run.frames(frames, 4, 5, 6, 7, 8, 9);

		attack = new Animation(15, false);
		attack.frames(frames, 14, 15, 16);

		die = new Animation(12, false);
		die.frames(frames, 10, 11, 12, 13);

		play(idle);
	}

	@Override
	public int blood() {
		return 0xFF44FF22;
	}
	
	public void boneExplode(int cell) {
		
		Camera.main.shake(3, 0.7f);
		for (int n : PathFinder.NEIGHBOURS9) {
			int c = cell + n;
			if (c >= 0 && c < Level.getLength()) {
				if (Dungeon.visible[c] && Level.passable[c]) {
					Sample.INSTANCE.play(Assets.SND_BONES);
					CellEmitter.center(c).start(Speck.factory(Speck.RATTLE), 0.1f, 3);
					CellEmitter.center(c).start(Speck.factory(Speck.ROCK), 0.07f, 10);
				}

				if(Random.Int(100)==42){RedWraith.spawnAt(c);}
				
				
				Char ch = Actor.findChar(c);
				if (ch != null && ch==Dungeon.hero) {
					// those not at the center of the blast take damage less
					// consistently.
					int minDamage = c == cell ? Dungeon.depth + 5 : 1;
					int maxDamage = 10 + Dungeon.depth * 3;
					                    
					
					int dmg = Random.NormalIntRange(minDamage, maxDamage) - Random.Int(ch.dr());
					
					
					if (dmg > 0) {
						ch.damage(dmg, this);
						if(Random.Int(15)==1){Buff.prolong(ch, Paralysis.class, 1);}
					}
											

					if (ch == Dungeon.hero && !ch.isAlive()){
						Dungeon.fail(Utils.format(ResultDescriptions.ITEM,
								"bone explosion"));
					}
				}
			}
		}

	}

	@Override
	public void attack(int cell) {
		if (!Level.adjacent(cell, ch.pos)) {

			turnTo(ch.pos, cell);
			boneExplode(cell);
			play(attack);

		} else {

			super.attack(cell);

		}
	}
	
	


}
