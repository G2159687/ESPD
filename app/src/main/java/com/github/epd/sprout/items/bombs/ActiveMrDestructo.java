
package com.github.epd.sprout.items.bombs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.MrDestructo;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ActiveMrDestructo extends Item {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.ACTIVEMRD;
		defaultAction = AC_ACTIVATETHROW;
		stackable = true;
		usesTargeting = true;
	}


	private static boolean activate = false;

	private static final String AC_ACTIVATETHROW = Messages.get(ActiveMrDestructo.class, "ac");


	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_ACTIVATETHROW);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_ACTIVATETHROW)) {
			activate = true;
			action = AC_THROW;
		} else
			activate = false;

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
				MrDestructo.spawnAt(newCell);
			} else {
				Dungeon.level.drop(this, newCell).sprite.drop(cell);
			}

		} else if (!Level.pit[cell] && activate) {
			MrDestructo.spawnAt(cell);
		} else {

			super.onThrow(cell);
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
