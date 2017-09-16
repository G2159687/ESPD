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
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.OrbOfZotMob;
import com.github.epd.sprout.effects.particles.ElmoParticle;
import com.github.epd.sprout.items.journalpages.Town;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class OrbOfZot extends Item {

	//private static final String AC_END = "END THE GAME";

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.ORBOFZOT;
		defaultAction = AC_ACTIVATETHROW;
		unique = true;
		usesTargeting = true;
	}

	private static boolean activate = false;

	private static final String AC_ACTIVATETHROW = Messages.get(OrbOfZot.class, "ac1");
	private static final String AC_BREAK = Messages.get(OrbOfZot.class, "ac2");

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_ACTIVATETHROW);
		actions.add(AC_BREAK);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		if (action.equals(AC_ACTIVATETHROW)) {
			activate = true;
			action = AC_THROW;
		} else {
			activate = false;
		}

		if (action.equals(AC_BREAK)) {
			Dungeon.level.drop(new Town(), Dungeon.hero.pos).sprite.drop(Dungeon.hero.pos);
			this.detachAll(Dungeon.hero.belongings.backpack);
			Sample.INSTANCE.play(Assets.SND_BLAST);
			hero.sprite.emitter().burst(ElmoParticle.FACTORY, 12);
		}

		super.execute(hero, action);
	}

	@Override
	protected void onThrow(int cell) {

		if (Actor.findChar(cell) != null) {
			ArrayList<Integer> candidates = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS8)
				if (Level.passable[cell + i])
					candidates.add(cell + i);
			int newCell = candidates.isEmpty() ? cell : Random
					.element(candidates);

			if (!Level.pit[newCell] && activate) {
				OrbOfZotMob.spawnAt(newCell);
			} else {
				Dungeon.level.drop(this, newCell).sprite.drop(cell);
			}

		} else if (!Level.pit[cell] && activate) {
			OrbOfZotMob.spawnAt(cell);
		} else {

			super.onThrow(cell);
		}

	}

	@Override
	public boolean doPickUp(Hero hero) {
		if (super.doPickUp(hero)) {

			return true;
		} else {
			return false;
		}
	}


	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
