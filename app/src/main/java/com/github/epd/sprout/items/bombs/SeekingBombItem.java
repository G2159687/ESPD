
package com.github.epd.sprout.items.bombs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.npcs.MirrorImage;
import com.github.epd.sprout.actors.mobs.npcs.SeekingBombNPC;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SeekingBombItem extends Item {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.SEEKING_BOMB;
		defaultAction = AC_LIGHTTHROW;
		stackable = true;
	}

	public MirrorImage mirrorimage;


	private static boolean seek = false;

	private static final String AC_LIGHTTHROW = Messages.get(SeekingBombItem.class, "ac");


	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_LIGHTTHROW);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_LIGHTTHROW)) {
			seek = true;
			action = AC_THROW;
		} else {
			seek = false;
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

			if (!Level.pit[newCell] && seek) {
				SeekingBombNPC.spawnAt(newCell);
			} else {
				Dungeon.level.drop(this, newCell).sprite.drop(cell);
			}

		} else if (!Level.pit[cell] && seek) {
			SeekingBombNPC.spawnAt(cell);

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
	public int price() {
		return 20 * quantity;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}


}
