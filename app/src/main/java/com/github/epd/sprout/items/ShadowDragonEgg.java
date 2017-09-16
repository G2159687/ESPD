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

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.pets.PET;
import com.github.epd.sprout.actors.mobs.pets.ShadowDragon;
import com.github.epd.sprout.effects.Pushing;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ShadowDragonEgg extends Item {

	private static final String TXT_NOTREADY = Messages.get(Egg.class, "notready");
	private static final String TXT_YOLK = Messages.get(Egg.class, "yolk");
	private static final String TXT_HATCH = Messages.get(Egg.class, "hatch");
	private static final String TXT_KICKS = Messages.get(Egg.class, "kick");
	private static final String TXT_SLOSH = Messages.get(Egg.class, "slosh");

	public static final float TIME_TO_USE = 1;

	public static final String AC_BREAK = Messages.get(Egg.class, "ac_break");
	public static final String AC_SHAKE = Messages.get(Egg.class, "ac_shake");

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.EGG;
		stackable = false;
	}

	public int startMoves = 0;
	public int moves = 0;
	public int burns = 0;
	public int freezes = 0;
	public int poisons = 0;
	public int lits = 0;
	public int summons = 0;

	private static final String STARTMOVES = "startMoves";
	private static final String MOVES = "moves";
	private static final String BURNS = "burns";
	private static final String FREEZES = "freezes";
	private static final String POISONS = "poisons";
	private static final String LITS = "lits";
	private static final String SUMMONS = "summons";


	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(STARTMOVES, startMoves);
		bundle.put(MOVES, moves);
		bundle.put(BURNS, burns);
		bundle.put(FREEZES, freezes);
		bundle.put(POISONS, poisons);
		bundle.put(LITS, lits);
		bundle.put(SUMMONS, summons);

	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		startMoves = bundle.getInt(STARTMOVES);
		moves = bundle.getInt(MOVES);
		burns = bundle.getInt(BURNS);
		freezes = bundle.getInt(FREEZES);
		poisons = bundle.getInt(POISONS);
		lits = bundle.getInt(LITS);
		summons = bundle.getInt(SUMMONS);

	}

	public int checkMoves() {
		return moves;
	}

	@Override
	public boolean doPickUp(Hero hero) {

		GLog.i(Messages.get(Egg.class, "pick1"));

		Egg egg = hero.belongings.getItem(Egg.class);
		if (egg != null) {
			GLog.w(Messages.get(Egg.class, "pick2"));
		}

		return super.doPickUp(hero);
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_BREAK);
		actions.add(AC_SHAKE);

		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		/*
	    if (action == AC_BREAK) {

			if (Dungeon.depth>26) {
				hero.spend(EasterEgg.TIME_TO_USE);
				GLog.w(TXT_PREVENTING);
				return;
			}			
		}
		*/

		if (action == AC_BREAK) {

			boolean hatch = false;

			if (checkMoves() >= 10) {
				ShadowDragon pet = new ShadowDragon();
				eggHatch(pet);
				hatch = true;
				//spawn bat
			}

			if (!hatch) {
				detach(Dungeon.hero.belongings.backpack);
				GLog.n(TXT_YOLK);
			}

			hero.next();

		} else if (action == AC_SHAKE) {

			boolean alive = false;
			if (checkMoves() >= 10) {
				GLog.w(TXT_KICKS);
				alive = true;
				//spawn bunny
			}

			if (!alive) {
				GLog.i(TXT_SLOSH);
			}

		} else {

			super.execute(hero, action);

		}


	}

	public int getSpawnPos() {
		int newPos;
		int pos = Dungeon.hero.pos;
		ArrayList<Integer> candidates = new ArrayList<Integer>();
		boolean[] passable = Level.passable;

		for (int n : PathFinder.NEIGHBOURS8) {
			int c = pos + n;
			if (passable[c] && Actor.findChar(c) == null) {
				candidates.add(c);
			}
		}

		newPos = candidates.size() > 0 ? Random.element(candidates) : -1;

		return newPos;
	}


	public void eggHatch(PET pet) {

		int spawnPos = getSpawnPos();
		if (spawnPos != -1 && !Dungeon.hero.haspet) {

			pet.spawn(1);
			pet.HP = pet.HT;
			pet.pos = spawnPos;
			pet.state = pet.HUNTING;

			GameScene.add(pet);
			Actor.addDelayed(new Pushing(pet, Dungeon.hero.pos, spawnPos), -1f);

			pet.sprite.alpha(0);
			pet.sprite.parent.add(new AlphaTweener(pet.sprite, 1, 0.15f));

			detach(Dungeon.hero.belongings.backpack);
			GLog.p(TXT_HATCH);
			Dungeon.hero.haspet = true;

			assignPet(pet);

		} else {

			Dungeon.hero.spend(ShadowDragonEgg.TIME_TO_USE);
			GLog.w(TXT_NOTREADY);

		}
	}

	private void assignPet(PET pet) {

		Dungeon.hero.petType = pet.type;
		Dungeon.hero.petLevel = pet.level;
		Dungeon.hero.petKills = pet.kills;
		Dungeon.hero.petHP = pet.HP;
		Dungeon.hero.petExperience = pet.experience;
		Dungeon.hero.petCooldown = pet.cooldown;
	}

	@Override
	public int price() {
		return 500 * quantity;
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
