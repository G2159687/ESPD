
package com.github.epd.sprout.items;

import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.artifacts.Artifact;
import com.github.epd.sprout.items.scrolls.ScrollOfUpgrade;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndBag;

import java.util.ArrayList;

public class UpgradeBlobRed extends Item {

	private static final String TXT_SELECT = Messages.get(UpgradeBlobRed.class, "prompt");
	private static final String TXT_UPGRADED = Messages.get(UpgradeBlobRed.class, "upgraded");

	private static final float TIME_TO_INSCRIBE = 2;

	private static final int upgrades = 3;


	private static final String AC_INSCRIBE = Messages.get(UpgradeBlobRed.class, "ac");

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.UPGRADEGOO_RED;

		stackable = true;

		bones = true;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_INSCRIBE);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action == AC_INSCRIBE) {

			curUser = hero;
			GameScene.selectItem(itemSelector, WndBag.Mode.UPGRADEABLE,
					TXT_SELECT);

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

	private void upgrade(Item item) {

		detach(curUser.belongings.backpack);

		if (item instanceof Artifact) {
			if (item.level < ((Artifact) item).levelCap || item.level >= 50) {
				GLog.w(Messages.get(ScrollOfUpgrade.class, "cannot"));
				new UpgradeBlobRed().collect();
				return;
			}
		}

		GLog.p(TXT_UPGRADED, item.name());

		if (item.reinforced) {
			item.upgrade(upgrades);
		} else {
			item.upgrade(Math.min(upgrades, 15 - item.level));
		}

		curUser.sprite.operate(curUser.pos);
		curUser.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);

		curUser.spend(TIME_TO_INSCRIBE);
		curUser.busy();

	}

	@Override
	public int price() {
		return 30 * quantity;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}

	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null) {
				UpgradeBlobRed.this.upgrade(item);
			}
		}
	};
}
