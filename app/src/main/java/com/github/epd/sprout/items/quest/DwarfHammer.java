
package com.github.epd.sprout.items.quest;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;

import java.util.ArrayList;

public class DwarfHammer extends Item {

	private static final String TXT_PREVENTING = Messages.get(DwarfHammer.class, "prevent");
	private static final String TXT_UNSEAL = Messages.get(DwarfHammer.class, "unseal");

	public static final float TIME_TO_USE = 1;

	public static final String AC_BREAK = Messages.get(DwarfHammer.class, "ac");

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.DWARFHAMMER;
		unique = true;

	}


	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_BREAK);

		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		if (action == AC_BREAK) {

			if (Dungeon.bossLevel()) {
				hero.spend(DwarfHammer.TIME_TO_USE);
				GLog.w(TXT_PREVENTING);
				return;
			}

			if (!Dungeon.visible[Dungeon.level.exit]) {
				hero.spend(DwarfHammer.TIME_TO_USE);
				GLog.w(TXT_PREVENTING);
				return;
			}


		}

		if (action == AC_BREAK) {

			if (hero.pos != Dungeon.level.exit && (Dungeon.depth == 22 || Dungeon.depth == 23)) {

				if (!(Dungeon.level.map[Dungeon.level.exit] == Terrain.EXIT)) {
					GLog.w(TXT_UNSEAL);
					detach(Dungeon.hero.belongings.backpack);
				}

				Dungeon.level.sealedlevel = false;

				Dungeon.level.map[Dungeon.level.exit] = Terrain.EMPTY;
				GameScene.updateMap(Dungeon.level.exit);
				Dungeon.observe();

				Dungeon.level.map[Dungeon.level.exit] = Terrain.EXIT;
				GameScene.updateMap(Dungeon.level.exit);
				Dungeon.observe();

			}

		} else {
			GLog.w(TXT_PREVENTING);
			super.execute(hero, action);
		}
	}

	@Override
	public int price() {
		return 10 * quantity;
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
