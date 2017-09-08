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
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Bat;
import com.github.epd.sprout.actors.mobs.DwarfKingTomb;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

//TODO: Optimize this

public class Pickaxe extends Weapon {

	public static final String AC_MINE = Messages.get(Pickaxe.class,"ac_mine");

	public static final float TIME_TO_MINE = 2;

	private static final String TXT_NO_VEIN = Messages.get(Pickaxe.class,"no_vein");

	private static final Glowing BLOODY = new Glowing(0x550000);

	{
		name = Messages.get(this,"name");
		image = ItemSpriteSheet.PICKAXE;

		unique = true;

		defaultAction = AC_MINE;

		STR = 14;
		MIN = 10;
		MAX = 22;
	}

	public boolean bloodStained = false;
	
	
		
	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_MINE);
		return actions;
	}

	@Override
	public void execute(final Hero hero, String action) {

		if (action == AC_MINE) {

			if ((Dungeon.depth < 11 || Dungeon.depth > 15) && !(Dungeon.depth==32) && !(Dungeon.depth > 55 || Dungeon.depth < 66)) {
				GLog.w(TXT_NO_VEIN);
				return;
			}

			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {

				final int pos = hero.pos + PathFinder.NEIGHBOURS8[i];
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
								hunger.satisfy(-Hunger.STARVING / 10);
								BuffIndicator.refreshHero();
							}

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
	public void proc(Char attacker, Char defender, int damage) {
		if (!bloodStained && defender instanceof Bat && (defender.HP <= damage)) {
			bloodStained = true;
			updateQuickslot();
		}
		if (defender instanceof DwarfKingTomb){
			
			defender.damage(Random.Int(100,200), this);
		}
	}

	private static final String BLOODSTAINED = "bloodStained";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);

		bundle.put(BLOODSTAINED, bloodStained);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		bloodStained = bundle.getBoolean(BLOODSTAINED);
	}

	@Override
	public Glowing glowing() {
		return bloodStained ? BLOODY : null;
	}

	@Override
	public String info() {
		return Messages.get(this,"desc");
	}
}
