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
package com.github.epd.sprout.items.quest;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.RedWraith;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

//TODO: Optimize this

public class Pickaxe extends Item {

	public static final String AC_MINE = Messages.get(Pickaxe.class, "ac_mine");
	public static final float TIME_TO_MINE = 2;
	private static final String TXT_NO_VEIN = Messages.get(Pickaxe.class, "no_vein");

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.PICKAXE;

		unique = true;

		defaultAction = AC_MINE;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_MINE);
		return actions;
	}

	@Override
	public void execute(final Hero hero, String action) {

		if (action == AC_MINE) {

			if (!(Dungeon.depth > 10 && Dungeon.depth < 16 || Dungeon.depth > 55 && Dungeon.depth < 66)){
				GLog.w(TXT_NO_VEIN);
				return;
			}

			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {

				final int pos = hero.pos + PathFinder.NEIGHBOURS8[i];
				final Heap heap = Dungeon.level.heaps.get(pos);
				if (Dungeon.level.map[pos] == Terrain.WALL_DECO) {

					hero.spend(TIME_TO_MINE);
					hero.busy();

					hero.sprite.attack(pos, new Callback() {

						@Override
						public void call() {

							CellEmitter.center(pos).burst(
									Speck.factory(Speck.STAR), 7);
							Sample.INSTANCE.play(Assets.SND_EVOKE);

							Level.set(pos, Terrain.WALL);
							GameScene.updateMap(pos);

							DarkGold gold = new DarkGold();
							if (gold.doPickUp(Dungeon.hero)) {
								GLog.i(Messages.get(Hero.class, "have"), gold.name());
							} else {
								Dungeon.level.drop(gold, hero.pos).sprite
										.drop();
							}

							Hunger hunger = hero.buff(Hunger.class);
							if (hunger != null && !hunger.isStarving()) {
								hunger.satisfy(-Hunger.STARVING / 50);
								BuffIndicator.refreshHero();
							}

							hero.onOperateComplete();
						}
					});

					return;
				} else if (heap != null && heap.type == Heap.Type.HARD_TOMB){

					hero.spend(TIME_TO_MINE);
					hero.busy();

					hero.sprite.attack(pos, new Callback() {

						@Override
						public void call() {

							heap.type = Heap.Type.HEAP;
							Sample.INSTANCE.play(Assets.SND_TOMB);
							Camera.main.shake(1, 0.5f);
							RedWraith.spawnAround2x(hero.pos);

							heap.sprite.link();
							heap.sprite.drop();

							GameScene.updateMap(pos);

							hero.onOperateComplete();
						}
					});

					return;
				}
			}

			GLog.w(TXT_NO_VEIN);

		} else {

			super.execute(hero, action);

		}
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
