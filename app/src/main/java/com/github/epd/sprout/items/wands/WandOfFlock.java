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

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.mobs.FlyingProtector;
import com.github.epd.sprout.actors.mobs.npcs.NPC;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.MagicMissile;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.sprites.SheepSprite;
import com.github.epd.sprout.utils.BArray;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WandOfFlock extends Wand {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.WAND_FLOCK;
	}

	@Override
	protected void onZap(Ballistica bolt) {

		int level = level();

		int n = level + 2;

		int cell = bolt.collisionPos;

		boolean[] passable = BArray.or(Level.passable, Level.avoid, null);
		for (Actor actor : Actor.all()) {
			if (actor instanceof Char) {
				passable[((Char) actor).pos] = false;
			}
		}

		PathFinder.buildDistanceMap(cell, passable, n);
		int dist = 0;

		if (Actor.findChar(cell) != null) {
			PathFinder.distance[cell] = Integer.MAX_VALUE;
			dist = 1;
		}

		float lifespan = (level <= 17) ? level + 3 : 20;

		sheepLabel:
		for (int i = 0; i < n; i++) {
			do {
				for (int j = 0; j < Level.getLength(); j++) {
					if (PathFinder.distance[j] == dist) {

						Sheep sheep = new Sheep();
						sheep.lifespan = lifespan;
						sheep.pos = j;
						GameScene.add(sheep);
						Dungeon.level.mobPress(sheep);

						CellEmitter.get(j).burst(Speck.factory(Speck.WOOL), 4);

						PathFinder.distance[j] = Integer.MAX_VALUE;

						continue sheepLabel;
					}
				}
				dist++;
			} while (dist < n);
		}

		if (Dungeon.depth > 50 && Dungeon.depth < 55) {
			int spawnCell = Dungeon.level.randomRespawnCellMob();
			if (spawnCell > 0) {
				FlyingProtector.spawnAt(spawnCell);
				GLog.w(Messages.get(this, "s1"));
				GLog.w(Messages.get(this, "s2"));
			}
		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.wool(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	public static class Sheep extends NPC {

		private static final String[] QUOTES = {Messages.get(WandOfFlock.class, "1"), Messages.get(WandOfFlock.class, "2"), Messages.get(WandOfFlock.class, "3"),
				Messages.get(WandOfFlock.class, "4")};

		{
			name = Messages.get(WandOfFlock.class, "sname");
			spriteClass = SheepSprite.class;
		}

		public float lifespan;

		private boolean initialized = false;

		@Override
		protected boolean act() {
			if (initialized) {
				HP = 0;

				destroy();
				sprite.die();

			} else {
				initialized = true;
				spend(lifespan + Random.Float(2));
			}
			return true;
		}

		@Override
		public void damage(int dmg, Object src) {
		}

		@Override
		public void add(Buff buff) {
		}

		@Override
		public String description() {
			return Messages.get(WandOfFlock.class, "sdesc");
		}

		@Override
		public boolean interact() {
			yell(Random.element(QUOTES));
			return false;
		}
	}
}
