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
package com.github.epd.sprout.items;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.items.artifacts.DriedRose;
import com.github.epd.sprout.items.artifacts.TimekeepersHourglass;
import com.github.epd.sprout.items.wands.WandOfBlink;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.scenes.InterlevelScene;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class LloydsBeacon extends Item {

	private static final String TXT_PREVENTING = Messages.get(LloydsBeacon.class,"prevent");

	private static final String TXT_CREATURES = Messages.get(LloydsBeacon.class,"creatures");

	private static final String TXT_RETURN = Messages.get(LloydsBeacon.class,"return");

	private static final String TXT_INFO = Messages.get(LloydsBeacon.class,"info");

	private static final String TXT_SET = Messages.get(LloydsBeacon.class,"set");

	public static final float TIME_TO_USE = 1;

	public static final String AC_SET = Messages.get(LloydsBeacon.class,"ac_set");
	public static final String AC_RETURN = Messages.get(LloydsBeacon.class,"ac_return");

	private int returnDepth = -1;
	private int returnPos;

	{
		name = Messages.get(LloydsBeacon.class,"name");
		image = ItemSpriteSheet.BEACON;

		unique = true;
	}

	private static final String DEPTH = "depth";
	private static final String POS = "pos";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DEPTH, returnDepth);
		if (returnDepth != -1) {
			bundle.put(POS, returnPos);
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		returnDepth = bundle.getInt(DEPTH);
		returnPos = bundle.getInt(POS);
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_SET);
		if (returnDepth != -1) {
			actions.add(AC_RETURN);
		}
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		if (action == AC_SET || action == AC_RETURN) {

			if (Dungeon.bossLevel() || Dungeon.depth>24) {
				hero.spend(LloydsBeacon.TIME_TO_USE);
				GLog.w(TXT_PREVENTING);
				return;
			}

			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				if (Actor.findChar(hero.pos + PathFinder.NEIGHBOURS8[i]) != null) {
					GLog.w(TXT_CREATURES);
					return;
				}
			}
		}

		if (action == AC_SET) {

			returnDepth = Dungeon.depth;
			returnPos = hero.pos;

			hero.spend(LloydsBeacon.TIME_TO_USE);
			hero.busy();

			hero.sprite.operate(hero.pos);
			Sample.INSTANCE.play(Assets.SND_BEACON);

			GLog.i(TXT_RETURN);

		} else if (action == AC_RETURN) {

			if (returnDepth == Dungeon.depth) {
				WandOfBlink.appear(hero, returnPos);
				Dungeon.level.press(returnPos, hero);
				Dungeon.observe();
				GameScene.updateFog();
			} else {

				Buff buff = Dungeon.hero
						.buff(TimekeepersHourglass.timeFreeze.class);
				if (buff != null)
					buff.detach();

				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
					if (mob instanceof DriedRose.GhostHero)
						mob.destroy();

				InterlevelScene.mode = InterlevelScene.Mode.RETURN;
				InterlevelScene.returnDepth = returnDepth;
				InterlevelScene.returnPos = returnPos;
				Game.switchScene(InterlevelScene.class);
			}

		} else {

			super.execute(hero, action);

		}
	}

	public void reset() {
		returnDepth = -1;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	private static final Glowing WHITE = new Glowing(0xFFFFFF);

	@Override
	public Glowing glowing() {
		return returnDepth != -1 ? WHITE : null;
	}

	@Override
	public String info() {
		return TXT_INFO
				+ (returnDepth == -1 ? "" : Utils.format(TXT_SET, returnDepth));
	}
}
